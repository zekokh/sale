package ru.zekoh.core;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.Goods;

import java.util.List;

public class DiscountProgram {
    //Список промоакций

    //1 Скидка на общий чек сотрудника, гостя и т.д.

    //2 9 капкейков по цене 999

    //6 эклеров по цене 195
    public static Check promotion6(Check check) {

        //Количество эклеров
        int count = 0;

        //Классификатор по которому искать эклер
        int classifier = 5;

        //Количество эклеров кратных которым начинается скидка
        int countForPromo = 6;

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
                        Double price = goods.get(i).getPriceFromThePriceList();
                        goods.get(i).setPriceAfterDiscount(10.0);
                        goods.get(i).setSellingPrice(goods.get(i).getCount()*goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                    }
                }
            }

            check.setDiscountOnGoods(true);
        }

        return check;
    }

    //5 круасанов по цене 175

    //10 круасанов по цене 345

    //Комбо отдельно реализуем
}
