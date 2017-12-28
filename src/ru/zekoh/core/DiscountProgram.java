package ru.zekoh.core;

import ru.zekoh.db.Check;
import ru.zekoh.db.Data;
import ru.zekoh.db.entity.Goods;

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

    //5 и 10 круасанов по цене 175 (Классификатор 4)
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
        if (count >= 10) {
            int temp = count / 10;
            if (temp > 0) {

                //На столько круассанов надо сделать скидку
                count = 10 * temp;
            }

            //Счетчик кол-ва продукции на которую надо сделать
            int counterThing = 1;

            for (int i = 0; i < goods.size(); i++) {
                if (goods.get(i).getClassifier() == classifier) {
                    if (counterThing <= count) {
                        Double priceFromThePriceList = goods.get(i).getPriceFromThePriceList();
                        goods.get(i).setPriceAfterDiscount(41.5);
                        goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                    }
                }
            }

            //Если есть еще круасаны но меньше 10
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
                                goods.get(i).setPriceAfterDiscount(42.0);
                                goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                                counterThingFor5++;
                            }
                        }
                    }
                }

            }


            check.setDiscountOnGoods(true);
            newCheck = check;
        } else {
            newCheck = promotion(check, 4, 5, 42.0);
            check.setDiscountOnGoods(true);
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

        //Текущая дата


        return null;
    }
}
