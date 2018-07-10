package ru.zekoh.core;


import ru.zekoh.db.Check;
import ru.zekoh.db.Data;
import ru.zekoh.db.entity.Goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiscountProgram {

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }

    //Список промоакций

    //1 Скидка на общий чек сотрудника, гостя и т.д.

    //2 9 капкейков по цене 999

    //6 эклеров по цене 195 (Классификатор 5)
    public static Check promotion6(Check check) {

        return promotion(check, 5, 6, 49.17);
    }

    //6 эклеров по цене 5 (Классификатор 12)
    public static Check promotionMini6(Check check) {

        return promotion(check, 12, 6, 24.17);
    }

    //Флан классификатор 6
    public static Check promotion1(Check check) {

        return promotion(check, 6, 8, 97.375);
    }

    //5 круассан по цене 4
    public static Check promotion2(Check check) {

        Check newCheck = new Check();

        //Количество продукции
        int count = 0;

        //Классификатор
        int classifier = 4;

        //Количество всего круассан
        int amountCount = 0;

        List<Goods> goods = check.getGoodsList();

        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getClassifier() == classifier) {
                count = (int) (count + goods.get(i).getCount());
            }
        }

        amountCount = count;
        if (count >= 5) {
            int temp = count / 5;
            if (temp > 0) {

                //На столько круассанов надо сделать скидку
                count = 5 * temp;
            }

            //Счетчик кол-ва продукции на которую надо сделать
            int counterThing = 1;

            for (int i = 0; i < goods.size(); i++) {
                if (goods.get(i).getClassifier() == classifier) {
                    if (counterThing <= count) {
                        Double priceFromThePriceList = goods.get(i).getPriceFromThePriceList();
                        goods.get(i).setPriceAfterDiscount(37.6);
                        goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                    }
                }
            }

/*            //Если есть еще круасаны но меньше 10
            int otheThingCount = (amountCount - count);

            if (otheThingCount >= 5) {

                int tempFor5 = otheThingCount / 5;
                if (tempFor5 > 0) {

                    //На столько эклеров нао сделать скидку
                    otheThingCount = 5 * tempFor5;
                }

                //Счетчик кол-ва продукции на которую надо сделать
                int counterThingFor5 = 1;

                for (int i = 0; i < goods.size(); i++) {
                    if (goods.get(i).getClassifier() == classifier) {
                        if (counterThingFor5 <= otheThingCount) {
                            if (areEqualDouble(goods.get(i).getPriceFromThePriceList(), goods.get(i).getPriceAfterDiscount(), 2)) {
                                Double priceFromThePriceList = goods.get(i).getPriceFromThePriceList();
                                goods.get(i).setPriceAfterDiscount(49.0);
                                goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                                counterThingFor5++;
                            }
                        }
                    }
                }

            }*/


            check.setDiscountOnGoods(true);
            newCheck = check;
        } else {
            /*newCheck = promotion(check, 4, 5, 37.6);
            check.setDiscountOnGoods(true);*/

            return check;
        }

        return newCheck;
    }

    //Комбо отдельно реализуем


    //Классификатор по которому искать эклер
    //Количество эклеров кратных которым начинается скидка
    //Цену которую нужно установить
    public static Check promotion(Check check, int classifier, int countForPromo, Double price) {

        //Количество продукции
        int count = 0;

        List<Goods> goods = check.getGoodsList();

        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getClassifier() == classifier) {
                count = (int) (count + goods.get(i).getCount());
            }
        }

        if (count >= countForPromo) {

            int temp = count / countForPromo;
            if (temp > 0) {

                //На столько эклеров надо сделать скидку
                count = countForPromo * temp;
            }

            //Счетчик кол-ва продукции на которую надо сделать
            int counterThing = 1;

            for (int i = 0; i < goods.size(); i++) {
                if (goods.get(i).getClassifier() == classifier) {
                    if (counterThing <= count) {
                        Double priceFromThePriceList = goods.get(i).getPriceFromThePriceList();
                        goods.get(i).setPriceAfterDiscount(price);
                        goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                    }
                }
            }

            check.setDiscountOnGoods(true);
        }

        return check;
    }

    //Скидка 30% на выпечку после 8 вечера
    //Список классификаторов
    public static Check discountOnBakes(Check check) {

        //Сегодняшняя дата
        Date dateToday = new Date();

        //Формат для сегодняшней даты
        SimpleDateFormat formatForDateLimit = new SimpleDateFormat("dd.MM.yyyy");

        //Сохранем отформатированную сегодняшнюю дату в переменной
        String dateTodayString = formatForDateLimit.format(dateToday);

        //Создаем лимит после какой даты и врмени можно будет сделать скидку
        Date dateLimit = null;
        try {
            dateLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " 07:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Создаем текущее дату и время
        Date curentDate = new Date();

        //Сравниваем текщую дату с лимитом
        if (curentDate.after(dateLimit)) {

            //Делаем 30% сктдку на выпечку
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Цена на текущий товар по прайсу
                Double priceFromThePriceList = goods.getPriceFromThePriceList();

                //Классификатор товара
                int classifier = goods.getClassifier();

                if(classifier == 13 || classifier == 4){

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * 0.30;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList-discountAmount;

                    //Устанавливаем цену со скидкой
                    priceAfterDiscount = new BigDecimal(priceAfterDiscount).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    goods.setPriceAfterDiscount(priceAfterDiscount);

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

                    //Устанавливаем продажную цену товара
                    goods.setSellingPrice(sellingPrice);

                    //Указываем что в чеке есть товары со скидкой
                    check.setDiscountOnGoods(true);
                }
            }
        }


        return check;
    }
}
