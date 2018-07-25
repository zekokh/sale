package ru.zekoh.core.printing;

import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.db.Check;
import ru.zekoh.db.entity.GoodsForDisplay;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class KKMOFD {
    public static String name = "Кассир";
    public static String inn = "";
    public static String COM_PORT = "4";

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
                fptr.closeReceipt();

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
}