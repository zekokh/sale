package ru.zekoh.core.printing;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.controller.MenuController;
import ru.zekoh.core.KKTError;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class KKMOFD {
    private static final Logger logger = LogManager.getLogger(KKMOFD.class);
    public static String name = Properties.currentUser.getName();
    public static String inn = "";
    public static String COM_PORT = Properties.comPort;

    public static boolean initDriver() {

        System.out.println("Инициализация драйвера ккт");
        System.out.println("COM: " + COM_PORT);

        try {
            System.out.println("строка 1");
            // Инициализация драйвера
            IFptr fptr = new Fptr();
            System.out.println("строка 2");

            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_MODEL, String.valueOf(IFptr.LIBFPTR_MODEL_ATOL_30F));
            System.out.println("строка 3");
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_PORT, String.valueOf(IFptr.LIBFPTR_PORT_COM));
            System.out.println("строка 4");
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_COM_FILE, COM_PORT);
            System.out.println("строка 5");
            fptr.setSingleSetting(IFptr.LIBFPTR_SETTING_BAUDRATE, String.valueOf(IFptr.LIBFPTR_PORT_BR_115200));
            System.out.println("строка 6");
            fptr.applySingleSettings();

            System.out.println("строка 7");
            Properties.FPTR = fptr;
            System.out.println("строка 8");
            Properties.FPTR.open();

        } catch (Exception e) {
            System.out.println("Ошибка инициализации драйвера ККТ: " + e.getMessage());
            logger.error("Ошибка инициализации драйвера ККТ: " + e.getMessage());
        }
        System.out.println("Стартуем");
        logger.info("Стартуем");

        return false;
    }

    public static KKTError sendToKKM(CheckObject check, boolean print) {

        List<GoodsForDisplay> goodsForDisplays = convert(check.getGoodsList());

        if (Properties.FPTR != null) {

            IFptr fptr = Properties.FPTR;

            try {
                if (fptr.isOpened()) {

                    // Печать в нальчике
                    if (Properties.bakaryId == 5) {
                        if (nonfiscalPrinting(fptr, check, goodsForDisplays)){
                            return new KKTError(true, 1, "Чек напечатан.");
                        } else {
                            return new KKTError(false, 0, "Соединение с ККМ не открыто!!");
                        }
                    } else {
                        if (print) {


                            // Печать в остальных

                            // Регистрация операции
                            fptr.setParam(1021, name);
                            fptr.setParam(1203, inn);
                            fptr.operatorLogin();

                            // Чек прихода
                            fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL);
                            fptr.openReceipt();

                            // List<GoodsForDisplay> goodsForDisplays = convert(check.getGoodsList());

                            // Печать продукции
                            for (int i = 0; i < goodsForDisplays.size(); i++) {
                                fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, goodsForDisplays.get(i).getName());
                                fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, goodsForDisplays.get(i).getPriceFromThePriceList());
                                fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, goodsForDisplays.get(i).getCount());
                                fptr.setParam(IFptr.LIBFPTR_PARAM_POSITION_SUM, goodsForDisplays.get(i).getSellingPrice());
                                fptr.setParam(IFptr.LIBFPTR_PARAM_TAX_TYPE, IFptr.LIBFPTR_TAX_NO);
                                fptr.registration();
                                logger.info("Товар: " + goodsForDisplays.get(i).getName() + " " + goodsForDisplays.get(i).getPriceFromThePriceList() + " " + goodsForDisplays.get(i).getCount() + " " + goodsForDisplays.get(i).getSellingPrice());
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

                            logger.info("Закрываем чек: " + check.getSellingPrice() + " " + check.getDateOfClosing());

                            fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, typePaymaent);
                            fptr.closeReceipt();

                        }
                        return checkPrintedStatus(fptr, check);
                    }
                } else {
                    logger.error("Соединение с ККМ не открыто!");
                    return new KKTError(false, 0, "Соединение с ККМ не открыто!!");
                }
            } catch (Exception e) {
                logger.error("Ошибка! " + e.toString());
                return new KKTError(false, 0, "Ошибка! " + e.toString());
            }
        } else {

            logger.error("Драйвер принтера чека не инициализирован!");
            return new KKTError(false, 0, "Драйвер принтера чека не инициализирован!");
        }


    }

    private static boolean nonfiscalPrinting(IFptr fptr, CheckObject check, List<GoodsForDisplay> goodsForDisplays) {
        try {
            printTextTitle(fptr, "Жак-Андрэ");
            printTextTitle(fptr, "французская пекарня");
            printTextTitle(fptr, "");
            printText(fptr, "Дата: ");
            for (int i = 0; i < goodsForDisplays.size(); i++) {
                String text = goodsForDisplays.get(i).getCount() + " * " +
                        goodsForDisplays.get(i).getPriceFromThePriceList() + " р." + " = " +
                        goodsForDisplays.get(i).getSellingPrice() + " р.";

                printText(fptr, goodsForDisplays.get(i).getName());
                //printTextTitle(fptr, goodsForDisplays.get(i).getName(), com.atol.drivers.fptr.IFptr.ALIGNMENT_LEFT, com.atol.drivers.fptr.IFptr.WRAP_WORD);
                printText(fptr, text);
            }

            printText(fptr, "Итого: " + check.getSellingPrice() + " р.");

            //Печать пустых строк
            printText(fptr, "");
            printText(fptr, "жак-андрэ.рф");
            printText(fptr, "г.Нальчик, ул.Московска, 6");
            printText(fptr, "ИНН: ");
            printText(fptr, "");
            printText(fptr, "");
        } catch (Exception e) {
            logger.error("Ошибка при печати нефискального документа");
            return false;
        }
        return true;
    }

    private static void printTextTitle(IFptr fptr, String s) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, s);
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_CENTER);
        fptr.printText();
    }

    private static void printText(IFptr fptr, String s) {
        fptr.setParam(IFptr.LIBFPTR_PARAM_TEXT, s);
        fptr.setParam(IFptr.LIBFPTR_PARAM_ALIGNMENT, IFptr.LIBFPTR_ALIGNMENT_LEFT);
        fptr.printText();
    }

    public static boolean returnToKKM(List<TableGoods> goods, CheckEntity check) {
        if (Properties.FPTR != null) {
            IFptr fptr = Properties.FPTR;


            try {
                // Открыть соединение
                // fptr.open();

                if (fptr.isOpened()) {

                    // Регистрация операции
                    fptr.setParam(1021, name);
                    fptr.setParam(1203, inn);
                    fptr.operatorLogin();

                    // Чек прихода
                    fptr.setParam(IFptr.LIBFPTR_PARAM_RECEIPT_TYPE, IFptr.LIBFPTR_RT_SELL_RETURN);
                    fptr.openReceipt();


                    // Печать продукции
                    for (int i = 0; i < goods.size(); i++) {
                        fptr.setParam(IFptr.LIBFPTR_PARAM_COMMODITY_NAME, goods.get(i).getName());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_PRICE, goods.get(i).getPrice());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_QUANTITY, goods.get(i).getCount());
                        fptr.setParam(IFptr.LIBFPTR_PARAM_POSITION_SUM, goods.get(i).getSellingPrice());
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
                    fptr.setParam(IFptr.LIBFPTR_PARAM_PAYMENT_TYPE, typePaymaent);
                    fptr.closeReceipt();

                    logger.info("Чек закрылся!");

                    // Данная ошибка возникает при потери связи с принтером
                    while (fptr.checkDocumentClosed() < 0) {
                        // Не удалось проверить состояние документа. Вывести пользователю текст ошибки, попросить устранить неполадку и повторить запрос
                        logger.info("В while!");
                        logger.info(fptr.errorDescription());


                        // Отобразить окно с ошибкой и предложить устранить проблему и нажать повтороить
                        // Если ошибка не устраняется?
                        continue;
                    }

                    if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_CLOSED)) {
                        logger.error("Чек не закрыт в ФН нужно отменить его и заного пробить");
                        // Документ не закрылся. Требуется его отменить (если это чек) и сформировать заново
                        fptr.cancelReceipt();

                        logger.error(fptr.errorDescription());
                        return false;
                    } else {
                        logger.info("Возврат чека закрылся и напечатался!");
                        return true;
                    }

                } else {
                    logger.error("Соединение с ККМ не открыто!");
                    return false;
                }
            } catch (Exception e) {
                logger.error("Ошибка! " + e.toString());
                return false;
            }
        } else {

            logger.error("Драйвер принтера чека не инициализирован!");
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

                } else {

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

        try {
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
        } catch (NullPointerException e) {
            System.out.println("Ошибка создания объекта драйвера!");
            System.out.println(e.getMessage().toString());
        }

        return null;
    }

    //Закрытие драйвера
    public static boolean close(IFptr fptr) {
        try {
            fptr.close();
            fptr.destroy();
            System.out.println("Объект драйвера удален!");
        } catch (Exception e) {
            System.out.println("Не смог удалить объект драйвера!" + e.getMessage().toString());
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
        if (fptr != null) {
            fptr.setParam(1021, name);
            fptr.setParam(1203, inn);
            fptr.operatorLogin();

            fptr.setParam(IFptr.LIBFPTR_PARAM_REPORT_TYPE, IFptr.LIBFPTR_RT_CLOSE_SHIFT);
            fptr.report();

            System.out.println("Закрытие смены: " + fptr.checkDocumentClosed());
        } else {
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
                // Double sellingPrice = (goodsTemp.getCount() + goods.get(i).getCount()) * goods.get(i).getPriceAfterDiscount());
                Double sellingPrice = goodsTemp.getCount() * goodsTemp.getPriceAfterDiscount();
                Double sellingPriceDouble = new BigDecimal(sellingPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsTemp.setSellingPrice(sellingPriceDouble);


            }
        }

        // Округляем
        //roundingGoods(goodsForDisplayList);
        return goodsForDisplayList;
    }

    // Метод округления продажной цены
    protected static void roundingGoods(List<GoodsForDisplay> goodsForDisplays){
       for(int i = 0; i < goodsForDisplays.size(); i++){
           Double priceAfterRounding = roundUp(goodsForDisplays.get(i).getPriceAfterDiscount());
           goodsForDisplays.get(i).setPriceAfterDiscount(priceAfterRounding);
       }
    }

    // Метод округления
    private static Double roundUp(Double numeral) {

        numeral = new BigDecimal(numeral).setScale(1, RoundingMode.HALF_UP).doubleValue();

        return numeral;
    }


    //МЕтод который проверяет наличие в Листе и возвращает
    public static int checkContain(List<GoodsForDisplay> goodsList, Goods goods) {

        //Проверяем что бы в нашем объекте что было иначе восприним как создание нового продукта в пустом листе
        if (goodsList.size() > 0) {
            for (int i = 0; i < goodsList.size(); i++) {

                //Если продукт уже есть в списке продуктов
                if (goods.getProductId() == goodsList.get(i).getProductId() && areEqualDouble(goods.getPriceAfterDiscount(), goodsList.get(i).getPriceAfterDiscount(), 2)) {

                    //Возвращаем индекс в листе
                    return i;
                }
            }
        }


        //Если в листе для отображения пользователю нет такого товара отправляем 0
        return -1;
    }

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }

    public static KKTError checkPrintedStatus(IFptr fptr, CheckObject check) {
        int temp = 0;
        while (fptr.checkDocumentClosed() < 0) {

            // Не удалось проверить состояние документа. Вывести пользователю текст ошибки, попросить устранить неполадку и повторить запрос
            System.out.println("В while!");
            logger.error("Чек " + check.getSellingPrice() + " " + check.getDateOfClosing() + " Зашел в петлю нет связи с принтером чека!");
            logger.error(fptr.errorDescription());
            System.out.println(fptr.errorDescription());
            if (temp == 3) {
                fptr.cancelReceipt();
                System.out.println("Чек не закрылся!");
                return new KKTError(false, 0, "Пропала связь с принтером чека (ККТ)!");
            }
            temp++;
            continue;
        }

        if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_CLOSED)) {
            // Документ не закрылся. Требуется его отменить (если это чек) и сформировать заново
            logger.error("Чек " + check.getSellingPrice() + " " + check.getDateOfClosing() + " Документ не закрылся требуектся отменить и пробить заного!");
            logger.error("Ошибка с ккт" + fptr.errorDescription());
            try {
                int answer = fptr.cancelReceipt();
                String text = "Не удалось отменить чек!";
                if (answer == 0) {
                    text = "Чек успешно отменен в ККТ";
                }
                logger.info("Отменяем чек: " + text);
            } catch (Exception e) {
                logger.error("Ошибка при отмене чека с ккт!" + e.getMessage());
            }

            return new KKTError(false, 1, "Чек не закрылся и был отменен, требуектся пробить заного!");
        }

        // Документ закрылся в ФН требуется его допечатать
        if (!fptr.getParamBool(IFptr.LIBFPTR_PARAM_DOCUMENT_PRINTED)) {
            // Можно сразу вызвать метод допечатывания документа, он завершится с ошибкой, если это невозможно
            while (fptr.continuePrint() < 0) {
                logger.error("Чек " + check.getSellingPrice() + " " + check.getDateOfClosing() + " Документ закрылся в ФН и не допечатался!");
                logger.error(fptr.errorDescription());
                // Если не удалось допечатать документ - показать пользователю ошибку и попробовать еще раз.
                logger.error(String.format("Не удалось допечатать документ (Ошибка \"%s\"). Устраните неполадку и повторите.", fptr.errorDescription()));
                if (temp == 3) {
                    return new KKTError(false, 2, "Не удалось допечатать документ!");
                }
                temp++;
                continue;
            }
        }

        logger.info("Чек закрылся успешно!");
        return new KKTError(true, 1, "Соединение с ККМ не открыто!!");
    }
}