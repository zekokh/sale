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

        return promotion(check, 5, 6, 36.667);
    }

    //6 эклеров по цене 5 (Классификатор 12)
    public static Check promotionMini6(Check check) {

        return promotion(check, 12, 6, 24.17);
    }

    //Флан классификатор 6
    public static Check promotion1(Check check) {

        return promotion(check, 6, 8, 81.125);
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
                        goods.get(i).setPriceAfterDiscount(39.9);
                        goods.get(i).setSellingPrice(goods.get(i).getCount() * goods.get(i).getPriceAfterDiscount());
                        counterThing++;
                        check.setDiscounСroissant(true);
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
                                goods.get(i).setPriceAfterDiscount(41.8);
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
            newCheck = promotion(check, 4, 5, 41.8);
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
                        if (classifier == 4){
                            check.setDiscounСroissant(true);
                        }
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
            dateLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " 19:20:00");
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

                if (classifier == 13 || classifier == 4) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * 0.40;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    //Устанавливаем цену со скидкой
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

    //Скидкак на 5 мафинов
    public static Check maffins(Check check) {
        return promotion(check, 14, 5, 42.0);
    }

    // Комбо и ланчи
    public static Check combo(Check check) {

        check = promotion(check, 4, 5, 39.8);


        //Сегодняшняя дата
        Date dateToday = new Date();
        Date maxValue = new Date();
        Date minValue = new Date();

        // Счетчик для чая
        int teaCounter = 0;

        // Ачма
        int achmaCounter = 0;

        // Счетчик для панини + чай + американер/мини пальмие
        int hardCounter = 0;

        // Счетчик для круассана/эскарго + чай
        int hard2Counter = 0;

        //Формат для сегодняшней даты
        SimpleDateFormat formatForDateLimit = new SimpleDateFormat("dd.MM.yyyy");

        //Сохранем отформатированную сегодняшнюю дату в переменной
        String dateTodayString = formatForDateLimit.format(dateToday);

        //Создаем лимит после какой даты и врмени можно будет сделать скидку
        Date dateLimit = null;
        try {
            dateLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " 19:20:00");
            maxValue = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " 15:00:00");
            minValue = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " 11:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Создаем текущее дату и время
        Date curentDate = new Date();

        //Сравниваем текщую дату с лимитом
        if (!curentDate.after(dateLimit)) {
            if (curentDate.before(maxValue) && curentDate.after(minValue)) {
                // Ланч
                // Проходим по списку ищем сколько чая в чеке

                List<Goods> goods = check.getGoodsList();

                for (int i = 0; i < goods.size(); i++) {
                    if (goods.get(i).getProductId() == 76 || goods.get(i).getProductId() == 77) {
                        teaCounter++;
                    }
                }

                // смотрим по приоритетам что есть из списка ланча потом комбо
                if (teaCounter > 0) {
                    for (int x = 0; x <= teaCounter; x++) {
                        for (int i = 0; i < goods.size(); i++) {
                            Goods currentGoods = goods.get(i);

                            // Приоритизация
                            // Если есть Ачма + Чай она в приоритете
                            if (currentGoods.getProductId() == 13) {
                                if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                    Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 26.0;
                                    currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                    Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                    currentGoods.setSellingPrice(sellingPrice);
                                    achmaCounter++;
                                    break;
                                }
                            }
                        }
                    }


                    if (teaCounter > achmaCounter) {
                        int counter = teaCounter - achmaCounter;
                        int americanrAndPalmyeCounter = 0;

                        // Проверить есть ли мини пальмье или американер
                        for (int i = 0; i < goods.size(); i++) {
                            Goods currentGoods = goods.get(i);
                            if (currentGoods.getProductId() == 250 || currentGoods.getProductId() == 23) {
                                americanrAndPalmyeCounter++;
                            }
                        }

                        int maxEach = counter;
                        if (maxEach > americanrAndPalmyeCounter) {
                            maxEach = americanrAndPalmyeCounter;
                        }
                        if (americanrAndPalmyeCounter > 0) {
                            for (int x = 0; x < maxEach; x++) {
                                for (int i = 0; i < goods.size(); i++) {
                                    Goods currentGoods = goods.get(i);

                                    // Приоритизация
                                    // Если есть Фромаж Тартар Шаркутри + Чай + мини Пальмье или Американер
                                    if (currentGoods.getClassifier() == 15) {
                                        if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                            Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 25.0;
                                            currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                            Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            currentGoods.setSellingPrice(sellingPrice);
                                            hardCounter++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    }

                    if ((teaCounter - achmaCounter - hardCounter) > 0) {

                        if (check.isDiscounСroissant()) {
                            int count = 0;

                            for (int i = 0; i < goods.size(); i++) {
                                Goods currentGoods = goods.get(i);

                                // Приоритизация
                                // Если есть Круассан/Эскарго + Чай она в приоритете
                                if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                    count++;
                                }
                            }

                            if (count > 5) {
                                for (int i = 0; i < goods.size(); i++) {
                                    Goods currentGoods = goods.get(i);

                                    // Приоритизация
                                    // Если есть Круассан/Эскарго + Чай она в приоритете
                                    if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                        if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                            Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 16.0;
                                            currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                            Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            currentGoods.setSellingPrice(sellingPrice);
                                            hard2Counter++;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {

                            for (int i = 0; i < goods.size(); i++) {
                                Goods currentGoods = goods.get(i);

                                // Приоритизация
                                // Если есть Круассан/Эскарго + Чай она в приоритете
                                if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                    if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                        Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 16.0;
                                        currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                        Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                        currentGoods.setSellingPrice(sellingPrice);
                                        hard2Counter++;
                                        break;
                                    }
                                }
                            }
                        }







/*                        for (int i = 0; i < goods.size(); i++) {
                            Goods currentGoods = goods.get(i);

                            // Приоритизация
                            // Если есть Круассан/Эскарго + Чай она в приоритете
                            if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                    Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 20.0;
                                    currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                    Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                    currentGoods.setSellingPrice(sellingPrice);
                                    hard2Counter++;
                                    break;
                                }
                            }
                        }*/
                    }


                    if ((teaCounter - achmaCounter - hardCounter - hard2Counter) > 0) {
                        int americanrAndPalmyeCounter = 0;

                        // Проверить есть ли мини пальмье или американер
                        for (int i = 0; i < goods.size(); i++) {
                            Goods currentGoods = goods.get(i);
                            if (currentGoods.getProductId() == 210 || currentGoods.getProductId() == 23) {
                                americanrAndPalmyeCounter++;
                            }
                        }

                        int maxEach = teaCounter - achmaCounter - hardCounter - hard2Counter;
                        if (maxEach > americanrAndPalmyeCounter) {
                            maxEach = americanrAndPalmyeCounter;
                        }

                        if (americanrAndPalmyeCounter > 0) {

                            for (int x = 0; x < maxEach; x++) {
                                for (int i = 0; i < goods.size(); i++) {
                                    Goods currentGoods = goods.get(i);

                                    // Приоритизация
                                    // Если есть Пуле + Чай + мини Пальмье или Американер
                                    if (currentGoods.getProductId() == 81) {
                                        if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                            Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 15.0;
                                            currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                            Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            currentGoods.setSellingPrice(sellingPrice);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                List<Goods> goods = check.getGoodsList();

                for (int i = 0; i < goods.size(); i++) {
                    if (goods.get(i).getProductId() == 76 || goods.get(i).getProductId() == 77) {
                        teaCounter++;
                    }
                }

                // смотрим по приоритетам что есть из списка ланча потом комбо
                if (teaCounter > 0) {
                    for (int x = 0; x <= teaCounter; x++) {
                        for (int i = 0; i < goods.size(); i++) {
                            Goods currentGoods = goods.get(i);

                            // Приоритизация
                            // Если есть Ачма + Чай она в приоритете
                            if (currentGoods.getProductId() == 13) {
                                if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                    Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 26.0;
                                    currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                    Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                    currentGoods.setSellingPrice(sellingPrice);
                                    achmaCounter++;
                                    break;
                                }
                            }
                        }
                    }

                    if ((teaCounter - achmaCounter) > 0) {

                        if (check.isDiscounСroissant()) {
                            int count = 0;

                            for (int i = 0; i < goods.size(); i++) {
                                Goods currentGoods = goods.get(i);

                                // Приоритизация
                                // Если есть Круассан/Эскарго + Чай она в приоритете
                                if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                    count++;
                                }
                            }

                            if (count > 6) {
                                for (int i = 0; i < goods.size(); i++) {
                                    Goods currentGoods = goods.get(i);

                                    // Приоритизация
                                    // Если есть Круассан/Эскарго + Чай она в приоритете
                                    if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                        if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                            Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 16.0;
                                            currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                            Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                            currentGoods.setSellingPrice(sellingPrice);
                                            hard2Counter++;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {

                            for (int i = 0; i < goods.size(); i++) {
                                Goods currentGoods = goods.get(i);

                                // Приоритизация
                                // Если есть Круассан/Эскарго + Чай она в приоритете
                                if (currentGoods.getProductId() == 14 || currentGoods.getProductId() == 15 || currentGoods.getProductId() == 17 || currentGoods.getProductId() == 18 || currentGoods.getProductId() == 19 || currentGoods.getProductId() == 20 || currentGoods.getProductId() == 33 || currentGoods.getProductId() == 35) {
                                    if (currentGoods.getPriceAfterDiscount() == currentGoods.getPriceFromThePriceList()) {
                                        Double priceAfterDiscount = currentGoods.getPriceFromThePriceList() - 16.0;
                                        currentGoods.setPriceAfterDiscount(priceAfterDiscount);
                                        Double sellingPrice = new BigDecimal(priceAfterDiscount * currentGoods.getCount()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                        currentGoods.setSellingPrice(sellingPrice);
                                        hard2Counter++;
                                        break;
                                    }
                                }
                            }
                        }

                    }

                }
            }


            // Тут комбо
        }

        return check;
    }
}

