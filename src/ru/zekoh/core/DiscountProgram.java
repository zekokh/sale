package ru.zekoh.core;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.Goods;

import java.util.List;

public class DiscountProgram {
    //Список промоакций

    //1 Скидка на общий чек сотрудника, гостя и т.д.

    //2 9 капкейков по цене 999

    //6 эклеров по цене 195 (Классификатор 5)
    public static Check promotion6(Check check) {

        return promotion(check, 5, 6, 37.0);
    }

    //5 круасанов по цене 175 (Классификатор 6)
    public static Check promotion2(Check check) {

        return promotion(check, 6, 5, 37.0);
    }

    //Комбо отдельно реализуем


    //Классификатор по которому искать эклер
    //Количество эклеров кратных которым начинается скидка
    //Цену которую нужно установить
    public static Check promotion(Check check, int classifier, int countForPromo, Double price){

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

                //На столько эклеров нао сделать скидку
                count = countForPromo * temp;
            }

            //Счетчик кол-ва продукции на которую надо сделать
            int counterThing = 1;

            for (int i = 0; i < goods.size(); i++) {
                if (goods.get(i).getClassifier() == classifier) {
                    if(counterThing <= count){
                        Double priceFromThePriceList = goods.get(i).getPriceFromThePriceList();
                        goods.get(i).setPriceAfterDiscount(price);
                        goods.get(i).setSellingPrice(goods.get(i).getCount()*goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                    }
                }
            }

            check.setDiscountOnGoods(true);
        }

        return check;
    }
}
