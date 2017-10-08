package ru.zekoh.core.printing;

import com.atol.drivers.fptr.Fptr;
import com.atol.drivers.fptr.IFptr;
import ru.zekoh.db.Check;
import ru.zekoh.db.entity.GoodsForDisplay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class KKM {
    private static final boolean USE_SHOWPROPERTIES = false;
    private static final boolean USE_FZ54 = false;
    private static final boolean PRINT_FISCAL_CHECK = false;
    private static final boolean PRINT_NONFISCAL_CHECK = true;

    private static void checkError(IFptr fptr) throws DriverException {
        int rc = fptr.get_ResultCode();
        if (rc < 0) {
            String rd = fptr.get_ResultDescription(), bpd = null;
            if (rc == -6) {
                bpd = fptr.get_BadParamDescription();
            }
            if (bpd != null)
                throw new DriverException(String.format("[%d] %s (%s)", rc, rd, bpd));
            else
                throw new DriverException(String.format("[%d] %s", rc, rd));
        }
    }

    private static void printText(IFptr fptr, String text, int alignment, int wrap) throws DriverException {
        if (fptr.put_Caption(text) < 0)
            checkError(fptr);
        if (fptr.put_TextWrap(wrap) < 0)
            checkError(fptr);
        if (fptr.put_Alignment(alignment) < 0)
            checkError(fptr);
        if (fptr.PrintString() < 0)
            checkError(fptr);
    }

    private static void printText(IFptr fptr, String text) throws DriverException {
        printText(fptr, text, IFptr.ALIGNMENT_CENTER, IFptr.WRAP_LINE);
    }

    private static void openCheck(IFptr fptr, int type) throws DriverException {
        if (fptr.put_Mode(IFptr.MODE_REGISTRATION) < 0)
            checkError(fptr);
        if (fptr.SetMode() < 0)
            checkError(fptr);
        if (fptr.put_CheckType(type) < 0)
            checkError(fptr);
        if (fptr.OpenCheck() < 0)
            checkError(fptr);
    }

    private static void closeCheck(IFptr fptr, int typeClose) throws DriverException {
        if (fptr.put_TypeClose(typeClose) < 0)
            checkError(fptr);
        if (fptr.CloseCheck() < 0)
            checkError(fptr);
    }

    private static void registration(IFptr fptr, String name, double price, double quantity) throws DriverException {
        if (fptr.put_Quantity(quantity) < 0)
            checkError(fptr);
        if (fptr.put_Price(price) < 0)
            checkError(fptr);
        if (fptr.put_TextWrap(IFptr.WRAP_WORD) < 0)
            checkError(fptr);
        if (fptr.put_Name(name) < 0)
            checkError(fptr);
        if (fptr.Registration() < 0)
            checkError(fptr);
    }

    private static void registrationFZ54(IFptr fptr, String name, double price, double quantity,
                                         double positionSum, int taxNumber) throws DriverException {
        if (fptr.put_PositionSum(positionSum) < 0)
            checkError(fptr);
        if (fptr.put_Quantity(quantity) < 0)
            checkError(fptr);
        if (fptr.put_Price(price) < 0)
            checkError(fptr);
        if (fptr.put_TaxNumber(taxNumber) < 0)
            checkError(fptr);
        if (fptr.put_TextWrap(IFptr.WRAP_WORD) < 0)
            checkError(fptr);
        if (fptr.put_Name(name) < 0)
            checkError(fptr);
        if (fptr.Registration() < 0)
            checkError(fptr);
    }


    private static void payment(IFptr fptr, double sum, int type) throws DriverException {
        if (fptr.put_Summ(sum) < 0)
            checkError(fptr);
        if (fptr.put_TypeClose(type) < 0)
            checkError(fptr);
        if (fptr.Payment() < 0)
            checkError(fptr);
        System.out.println(String.format("Remainder: %.2f, Change: %.2f", fptr.get_Remainder(), fptr.get_Change()));
    }

    private static void reportZ(IFptr fptr) throws DriverException {
        if (fptr.put_Mode(IFptr.MODE_REPORT_CLEAR) < 0)
            checkError(fptr);
        if (fptr.SetMode() < 0)
            checkError(fptr);
        if (fptr.put_ReportType(IFptr.REPORT_Z) < 0)
            checkError(fptr);
        if (fptr.Report() < 0)
            checkError(fptr);
    }

    private static void printFooter(IFptr fptr) throws DriverException {
        if (fptr.put_Mode(IFptr.MODE_REPORT_NO_CLEAR) < 0)
            checkError(fptr);
        if (fptr.SetMode() < 0)
            checkError(fptr);
        if (fptr.PrintFooter() < 0)
            checkError(fptr);
    }

    private static void printBarcode(IFptr fptr, int type, String barcode, double scale) throws DriverException {
        if (fptr.put_Alignment(IFptr.ALIGNMENT_CENTER) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeType(type) < 0)
            checkError(fptr);
        if (fptr.put_Barcode(barcode) < 0)
            checkError(fptr);
        if (fptr.put_Height(0) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeVersion(0) < 0)
            checkError(fptr);
        if (fptr.put_BarcodePrintType(IFptr.BARCODE_PRINTTYPE_AUTO) < 0)
            checkError(fptr);
        if (fptr.put_PrintBarcodeText(false) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeControlCode(true) < 0)
            checkError(fptr);
        if (fptr.put_Scale(scale) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeCorrection(0) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeColumns(3) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeRows(1) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeProportions(50) < 0)
            checkError(fptr);
        if (fptr.put_BarcodeUseProportions(true) < 0)
            checkError(fptr);
        if (fptr.put_BarcodePackingMode(IFptr.BARCODE_PDF417_PACKING_MODE_DEFAULT) < 0)
            checkError(fptr);
        if (fptr.put_BarcodePixelProportions(300) < 0)
            checkError(fptr);
        if (fptr.PrintBarcode() < 0)
            checkError(fptr);
    }

    private static void discount(IFptr fptr, double sum, int type, int destination) throws DriverException {
        if (fptr.put_Summ(sum) < 0)
            checkError(fptr);
        if (fptr.put_DiscountType(type) < 0)
            checkError(fptr);
        if (fptr.put_Destination(destination) < 0)
            checkError(fptr);
        if (fptr.Discount() < 0)
            checkError(fptr);
    }

    private static void charge(IFptr fptr, double sum, int type, int destination) throws DriverException {
        if (fptr.put_Summ(sum) < 0)
            checkError(fptr);
        if (fptr.put_DiscountType(type) < 0)
            checkError(fptr);
        if (fptr.put_Destination(destination) < 0)
            checkError(fptr);
        if (fptr.Charge() < 0)
            checkError(fptr);
    }

    public static boolean doIt(List<GoodsForDisplay> goodsForDisplays, Check check) throws DriverException {
        boolean flag = false;

        Random random = new Random();
        IFptr fptr = new Fptr();

        try {
            fptr.create();

            // Выставляем рабочий каталог. В нем дККМ будет искать требуемые ему библиотеки.
            fptr.put_DeviceSingleSetting(fptr.SETTING_SEARCHDIR, System.getProperty("java.library.path"));
            fptr.ApplySingleSettings();

            if (USE_SHOWPROPERTIES) {
                // Для настройки драйвера можно вызвать графическое окно настроек
                if (fptr.ShowProperties() < 0)
                    checkError(fptr);
            } else {
                // Или настроить без него
                // COM17
                if (fptr.put_DeviceSingleSetting(IFptr.SETTING_PORT, 3) < 0)
                    checkError(fptr);
                // USB. Можно указать положение на шине (USB$1-1.3, например - брать из /sys/bus/usb/devices/),
                // но тогда не нужно указывать Vid и Pid
                // Работа напрямую с USB - Linux Only!
               /* if (fptr.put_DeviceSingleSetting(IFptr.SETTING_PORT, IFptr.SETTING_PORT_USB) < 0)
                    checkError(fptr); */
                if (fptr.put_DeviceSingleSetting(IFptr.SETTING_VID, 0x2912) < 0)
                    checkError(fptr);
                if (fptr.put_DeviceSingleSetting(IFptr.SETTING_PID, 0x0005) < 0)
                    checkError(fptr);
                if (USE_FZ54) {
                    if (fptr.put_DeviceSingleSetting(IFptr.SETTING_MODEL, IFptr.MODEL_ATOL_55F) < 0)
                        checkError(fptr);
                } else {
                    if (fptr.put_DeviceSingleSetting(IFptr.SETTING_MODEL, IFptr.MODEL_ATOL_30F) < 0)
                        checkError(fptr);
                }
                if (fptr.put_DeviceSingleSetting(IFptr.SETTING_BAUDRATE, 115200) < 0)
                    checkError(fptr);
                if (fptr.ApplySingleSettings() < 0)
                    checkError(fptr);
            }

            // Подключаемся к устройству
            if (fptr.put_DeviceEnabled(true) < 0)
                checkError(fptr);

            // Проверка связи
            if (fptr.GetStatus() < 0)
                checkError(fptr);

            // Убедились, что настройки подходят и касса отвечает - вытаскиваем актуальные настройки из драйвера
            if (true) {
                String settings = fptr.get_DeviceSettings();
                // Тут можно их сохранить в файл, базу, т.п.
                // При следующем запуске их можно передать в драйвер чере put_DeviceSettings()
            }

            // Отменяем чек, если уже открыт. Ошибки "Неверный режим" и "Чек уже закрыт"
            // не являются ошибками, если мы хотим просто отменить чек
            try {
                if (fptr.CancelCheck() < 0)
                    checkError(fptr);
            } catch (DriverException e) {
                int rc = fptr.get_ResultCode();
                if (rc != -16 && rc != -3801)
                    throw e;
            }

            if (PRINT_FISCAL_CHECK) {
                // Открываем чек продажи, попутно обработав превышение смены
                try {
                    openCheck(fptr, IFptr.CHEQUE_TYPE_SELL);
                } catch (DriverException e) {
                    // Проверка на превышение смены
                    if (fptr.get_ResultCode() == -3822) {
                        reportZ(fptr);
                        openCheck(fptr, IFptr.CHEQUE_TYPE_SELL);
                    } else {
                        throw e;
                    }
                }

                BigDecimal sum = new BigDecimal(0);
                for (int i = -2; i < 5; ++i) {
                    if (USE_FZ54) {
                        double price = Math.pow(10, i), quantity = 1;
                        registrationFZ54(fptr, String.format("Позиция %d", i + 3), price, quantity, price * quantity, IFptr.TAX_VAT_18);
                        sum = sum.add(new BigDecimal(price).multiply(new BigDecimal(quantity)));
                    } else {
                        double price = Math.pow(10, i), quantity = 1;
                        registration(fptr, String.format("Позиция %d", i + 3), price, quantity);
                        sum = sum.add(new BigDecimal(price).multiply(new BigDecimal(quantity)));
                        // Скидка на позицию
                        if (i % 2 == 0) {
                            discount(fptr, 1, IFptr.DISCOUNT_SUMM, IFptr.DESTINATION_POSITION);
                        } else {
                            charge(fptr, 1, IFptr.DISCOUNT_PERCENT, IFptr.DESTINATION_POSITION);
                        }
                    }
                }
                // Скидка на чек
                if (USE_FZ54) {
                    // Можно сбрасывать в пределах копеек. При подаче 0 копейки отбрасываются полностью
                    discount(fptr, 0, IFptr.DISCOUNT_SUMM, IFptr.DESTINATION_CHECK);
                } else {
                    discount(fptr, 1, IFptr.DISCOUNT_PERCENT, IFptr.DESTINATION_CHECK);
                }
                // Оплачиваем
                payment(fptr, sum.divide(new BigDecimal(4)).doubleValue(), 1);
                payment(fptr, sum.divide(new BigDecimal(4)).doubleValue(), 2);
                payment(fptr, sum.divide(new BigDecimal(4)).doubleValue(), 3);
                payment(fptr, sum.doubleValue(), 0);
                // Закрываем чек
                closeCheck(fptr, 0);
            }

            // Нефискальный чек
            // Его можно не открывать, а сразу начинать печатать
            if (PRINT_NONFISCAL_CHECK) {

                printText(fptr, "Жак-Андрэ");
                printText(fptr, "");
                for (int i = 0; i < goodsForDisplays.size(); i++) {
                    String text = goodsForDisplays.get(i).getCount() + " * " +
                            goodsForDisplays.get(i).getPriceFromThePriceList() + " р." + " = " +
                            goodsForDisplays.get(i).getSellingPrice() + " р.";

                    printText(fptr, goodsForDisplays.get(i).getName(), IFptr.ALIGNMENT_LEFT, IFptr.WRAP_WORD);
                    printText(fptr, text);
                }

                if (check.isDiscountOnCheck() || check.isDiscountOnGoods()) {
                    Double discount = check.getTotal() - check.getAmountByPrice();
                    printText(fptr, "Скидка: " + discount + " р.", IFptr.ALIGNMENT_LEFT, IFptr.WRAP_WORD);
                }
                printText(fptr, "Итого: " + check.getTotal() + " р.", IFptr.ALIGNMENT_LEFT, IFptr.WRAP_WORD);
                //printFooter(fptr);

                //Печать пустых строк
                printText(fptr, "");
                printText(fptr, "");
                printText(fptr, "");
            }
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        } finally {
            fptr.destroy();
        }

        return flag;
    }

    private static class DriverException extends Exception {
        private static final long serialVersionUID = 6164921645357791803L;

        public DriverException(String msg) {
            super(msg);
        }
    }
}
