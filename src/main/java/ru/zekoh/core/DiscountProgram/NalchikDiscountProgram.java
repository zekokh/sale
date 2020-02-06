package ru.zekoh.core.DiscountProgram;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NalchikDiscountProgram implements DiscountInterface {
    public static CheckObject timeDiscount(CheckObject check) {

        String afterTime = "19:00:00";
        Double amountOfDiscount = 0.3;
        boolean flag = false;

        //Сегодняшняя дата
        Date dateToday = new Date();

        //Формат для сегодняшней даты
        SimpleDateFormat formatForDateLimit = new SimpleDateFormat("dd.MM.yyyy");

        //Сохранем отформатированную сегодняшнюю дату в переменной
        String dateTodayString = formatForDateLimit.format(dateToday);

        //Создаем лимит после какой даты и врмени можно будет сделать скидку
        Date dateLimit = null;
        try {
            dateLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " " + afterTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Создаем текущее дату и время
        Date curentDate = new Date();

        //Сравниваем текщую дату с лимитом
        if (curentDate.after(dateLimit)) {

            //Делаем 40% скидку на выпечку
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Цена на текущий товар по прайсу
                Double priceFromThePriceList = goods.getPriceFromThePriceList();

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == 1) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * amountOfDiscount;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    priceAfterDiscount = roundUp(priceAfterDiscount);

                    //Устанавливаем цену со скидкой
                    goods.setPriceAfterDiscount(priceAfterDiscount);

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

                    // Округляем продажную цену
                    sellingPrice = roundUp(sellingPrice);

                    //Устанавливаем продажную цену товара
                    goods.setSellingPrice(sellingPrice);

                    flag = true;
                }
            }
        }

        if (flag) {
            return check;
        } else {
            return null;
        }
    }

    // Метод округления
    private static Double roundUp(Double numeral) {

        numeral = new BigDecimal(numeral).setScale(1, RoundingMode.HALF_UP).doubleValue();

        return numeral;
    }

    @Override
    public void applyDiscounts(CheckObject check, List<Goods> goodsList) {
        boolean discount_flag = false;
        if (check.getDiscount() != null) {
            if (check.getDiscount().getDiscountRole() == 1){
                discount_flag = true;
            }
        }
        // Промоушены
        if (check.getDiscount() == null || discount_flag) {
            for (int i = 0; i < goodsList.size(); i++) {
                goodsList.get(i).setPriceAfterDiscount(goodsList.get(i).getPriceFromThePriceList());
            }

            NalchikDiscountProgram.timeDiscount(check);
        }
    }
}
