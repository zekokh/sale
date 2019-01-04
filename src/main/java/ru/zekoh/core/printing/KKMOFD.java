package ru.zekoh.core.printing;

import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.CheckEntity;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.db.entity.GoodsForDisplay;
import ru.zekoh.properties.Properties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KKMOFD {
    public static String name = Properties.currentUser.getName();
    public static String inn = "";
    public static String COM_PORT = "7";

    public static boolean initDriver(){

        try{
            // Инициализация драйвера
            IFptr fptr = new Fptr();


            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_20F));
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_COM));
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_COM_FILE, COM_PORT);
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
            fptr.applySingleSettings();

            Properties.FPTR = fptr;

        }catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean sendToKKM(CheckObject check){
        if(Properties.FPTR != null){
            IFptr fptr = Properties.FPTR;


            try{
                // Открыть соединение
                fptr.open();

                if(fptr.isOpened()) {

                    // Регистрация операции
                    fptr.setParam(1021, name);
                    fptr.setParam(1203, inn);
                    fptr.operatorLogin();

                    // Чек прихода
                    fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
                    fptr.openReceipt();

                    List<GoodsForDisplay> goodsForDisplays = convert(check.getGoodsList());

                    // Печать продукции
                    for (int i = 0; i < goodsForDisplays.size(); i++) {
                        fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, goodsForDisplays.get(i).getName());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, goodsForDisplays.get(i).getPriceFromThePriceList());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, goodsForDisplays.get(i).getCount());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_POSITION_SUM, goodsForDisplays.get(i).getSellingPrice());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
                        fptr.registration();
                    }

                    // Закрыть чек и напечатать
                    int typePaymaent = IFptr.LIBFPTR_PT_ELECTRONICALLY;

                    if (check.getTypeOfPayment() == 1) {
                        typePaymaent = IFptr.LIBFPTR_PT_CASH;
                    }

                    fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, typePaymaent);
                    fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, check.getSellingPrice());
                    fptr.payment();

                    // Зарегистрировать ИТОГ
                    fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, check.getSellingPrice());
                    fptr.receiptTotal();

                    // Закрыть чек
                    int answer = fptr.closeReceipt();

                    if (answer >= 0) {
                        if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_CLOSED)) {
                            // Документ не закрылся. Требуется его отменить (если это чек) и сформировать заново
                            fptr.cancelReceipt();
                            return false;
                        }

                        return true;
                    }else {
                        fptr.cancelReceipt();
                        return false;
                    }


                }else {
                    System.out.println("Соединение с ККМ не открыто!");
                    return false;
                }
            }catch (Exception e){

                return false;
            }
        }else {

            System.out.println("Драйвер принтера чека не инициализирован!");
            return false;
        }
    }

    // Создать и распечатать чек (Объект драйвера открытый, Чек, Тип платежа)
    public static boolean printCheck(IFptr fptr, Check check, List<GoodsForDisplay> goodsForDisplays) {

        // Флаг состояния
        boolean flag = true;

        // Если объект существует и открыт
        if (fptr != null) {


            try {
                fptr.setParam(1021, name);
                fptr.setParam(1203, inn);
                fptr.operatorLogin();

                // Чек прихода
                fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
                fptr.openReceipt();

                // Печать продукции
                for (int i = 0; i < goodsForDisplays.size(); i++) {
                    fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, goodsForDisplays.get(i).getName());
                    fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, goodsForDisplays.get(i).getPriceFromThePriceList());
                    fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, goodsForDisplays.get(i).getCount());
                    fptr.setParam(IFptr.LIBFPTR_PARAM_POSITION_SUM, goodsForDisplays.get(i).getSellingPrice());
                    fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
                    fptr.registration();
                }

                // Закрыть чек и напечатать
                int typePaymaent = IFptr.LIBFPTR_PT_ELECTRONICALLY;

                if (check.getTypeOfPayment() == 1) {
                    typePaymaent = IFptr.LIBFPTR_PT_CASH;
                }

                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, typePaymaent);
                fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_SUM, check.getTotal());
                fptr.payment();

                // Зарегистрировать ИТОГ
                fptr.setParam(IFptr.LIBFPTR_PARAM_SUM, check.getTotal());
                fptr.receiptTotal();

                // Закрыть чек
                int answer = fptr.closeReceipt();

                if (answer >= 0) {

                }else {

                }

                while (fptr.checkDocumentClosed() < 0) {
                    // Не удалось проверить состояние документа. Вывести пользователю текст ошибки, попросить устранить неполадку и повторить запрос
                    System.out.println(fptr.errorDescription());
                    flag = false;
                    continue;
                }

                if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_CLOSED)) {
                    // Документ не закрылся. Требуется его отменить (если это чек) и сформировать заново
                    fptr.cancelReceipt();
                    flag = false;
                    return flag;
                }

                if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_PRINTED)) {
                    // Можно сразу вызвать метод допечатывания документа, он завершится с ошибкой, если это невозможно
                    while (fptr.continuePrint() < 0) {
                        // Если не удалось допечатать документ - показать пользователю ошибку и попробовать еще раз.
                        System.out.println(String.format("Не удалось напечатать документ (Ошибка \"%s\"). Устраните неполадку и повторите.", fptr.errorDescription()));
                        flag = false;
                        continue;
                    }
                }

            } catch (Exception e) {
                System.out.println("Ошибка! " + e.getMessage().toString());
                flag = false;
            }
        } else {

            //Если объекта нет
            flag = false;
        }
        return flag;
    }

    // Инициализация драйвера
    public static IFptr create() {

        try{
            // Инициализация драйвера
            IFptr fptr = new Fptr();


            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_30F));
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_COM));
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_COM_FILE, COM_PORT);
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
            fptr.applySingleSettings();

            fptr.open();

            System.out.println("Соединение с ККТ: " + fptr.isOpened());

            return fptr;
        }catch (NullPointerException e){
            System.out.println("Ошибка создания объекта драйвера!");
            System.out.println(e.getMessage().toString());
        }

        return null;
    }

    //Закрытие драйвера
    public static boolean close(IFptr fptr) {
        try{
            fptr.close();
            fptr.destroy();
            System.out.println("Объект драйвера удален!");
        }catch (Exception e){
            System.out.println("Не смог удалить объект драйвера!"+e.getMessage().toString());
            return false;
        }

        return true;
    }



    // Допечатать документ
    public static IFptr continuePrint(IFptr fptr) {

        // Закрыть чек
        fptr.closeReceipt();

        // Допечатать
        fptr.continuePrint();

        return fptr;
    }

    // x - отчет
    public static IFptr xReport(IFptr fptr) {

        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_X);
        fptr.report();

        return fptr;
    }

    //Закрыть смену и сделать Z-Отчет
    public static boolean closeShift(IFptr fptr) {
        if(fptr != null){
            fptr.setParam(1021, name);
            fptr.setParam(1203, inn);
            fptr.operatorLogin();

            fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_CLOSE_SHIFT);
            fptr.report();

            System.out.println("Закрытие смены: "+fptr.checkDocumentClosed());
        }else {
            System.out.println("Не удалось закрыть смену!");
            System.out.println("Драйвер пустой! Нужно сперва создать объект драйвера ККТ!");
            return false;
        }


        return true;
    }

    // Печать информации о ККТ
    public static IFptr reportAboutKKT(IFptr fptr) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_KKT_INFO);
        fptr.report();
        return fptr;
    }

    // Диагностика соединения с ОФД
    public static boolean ofdTest(IFptr fptr) {
        if (fptr != null) {
            try {
                fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_OFD_TEST);
                fptr.report();
            } catch (Exception e) {
                System.out.printf("Ошибка диагностики с ОФД!" + e.getMessage().toString());
                return false;
            }
        } else {
            System.out.println("Драйвер пустой! Нужно сперва создать объект драйвера ККТ!");
            return false;
        }


        return true;
    }

    //Конвертирует GoodsList в лист для отображении в ListView
    public static List<GoodsForDisplay> convert(List<Goods> goods) {

        // Сортируем товары по id что при удалении они не скакали по ListView а с самого начала лежали рядом друг с другом
        // goods.sort(Comparator.comparingDouble(Goods::getProductId)
        //        .reversed());

        //Список товаров для вывода на дисплей
        List<GoodsForDisplay> goodsForDisplayList = new ArrayList<GoodsForDisplay>();


        for (int i = 0; i < goods.size(); i++) {

            int temp = checkContain(goodsForDisplayList, goods.get(i));

            if (temp == -1) {

                //Создаем новый объект goods для отображения в ListView
                GoodsForDisplay goodsForDisplay = new GoodsForDisplay();

                //Добавляем id продукта
                goodsForDisplay.setProductId(goods.get(i).getProductId());

                //Добавляем имя продука
                goodsForDisplay.setName(goods.get(i).getProductName());

                //Добавляем количетсво товара
                goodsForDisplay.setCount(goods.get(i).getCount());

                //Цена по прайсу
                goodsForDisplay.setPriceFromThePriceList(goods.get(i).getPriceFromThePriceList());

                //Сумма за позици
                Double sellingPriceDouble = new BigDecimal(goods.get(i).getSellingPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsForDisplay.setSellingPrice(sellingPriceDouble);

                //Цена после скидки
                goodsForDisplay.setPriceAfterDiscount(goods.get(i).getPriceAfterDiscount());

                //Добавляем объект goodsForDisplay в список объектов
                goodsForDisplayList.add(goodsForDisplay);
            } else {

                //Временная переменная для удобной работы с позицией в чеке которая уже есть для увелечения ее показателей
                GoodsForDisplay goodsTemp = goodsForDisplayList.get(temp);

                //Количество
                Double count = goodsTemp.getCount() + goods.get(i).getCount();
                goodsTemp.setCount(count);

                //Продажная цена
                Double sellingPrice = goodsTemp.getSellingPrice() + (goods.get(i).getCount() * goods.get(i).getSellingPrice());
                Double sellingPriceDouble = new BigDecimal(sellingPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsTemp.setSellingPrice(sellingPriceDouble);


            }
        }

        return goodsForDisplayList;
    }

    //МЕтод который проверяет наличие в Листе и возвращает
    public static int checkContain(List<GoodsForDisplay> goodsList, Goods goods) {

        //Проверяем что бы в нашем объекте что было иначе восприним как создание нового продукта в пустом листе
        if (goodsList.size() > 0) {
            for (int i = 0; i < goodsList.size(); i++) {

                //Если продукт уже есть в списке продуктов
                if (goods.getProductId() == goodsList.get(i).getProductId()) {

                    //Возвращаем индекс в листе
                    return i;
                }
            }
        }


        //Если в листе для отображения пользователю нет такого товара отправляем 0
        return -1;
    }

}