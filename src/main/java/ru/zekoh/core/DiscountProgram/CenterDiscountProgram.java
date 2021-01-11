package ru.zekoh.core.DiscountProgram;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CenterDiscountProgram implements DiscountInterface {
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

            CheckObject tempCheck = timeDiscount(check);
            if (tempCheck == null) {



                // 5 круаасан по цене 225р.
                discountOnCountProductInCheck(check, 4, 5, 45.0);
                // 5 круассанов классик по 199р.
                discountOnCountProductInCheck(check, 38, 5, 39.8);

                onCroissant(check);

                cappuccinoAndCroissant(check);

                //DiscountProgram.cappuccinoAndCroissant(check);

                // Скидка на 3 кусков пирога
                //discountOnCountProductInCheck(check, 32, 3, 43.34);
                // Скидка на 6 кусков пирагов
                //discountOnCountProductInCheck(check, 32, 6, 42.5);
                // Скидка на 12 кусков пирагов
                //discountOnCountProductInCheck(check, 32, 12, 41.58);
            }





            onAchmaAndTea(check);

            onBrioshAndTea(check);

            // Акции которые не с чем не пересикаются
            // 6 эклеров по цене 5
            //discountOnCountProductInCheck(check, 5, 6, 40.833);
            discountOneFree(check, 36,6, 5);
            discountOneFree(check, 5,6, 5);
            discountOneFree(check, 37,6, 12);
            //discountOneFree(check, 12,6, 12);

            // Флан натюр по кусочкам
            //DiscountProgram.discountOnCountProductInCheck(check, 9, 8, 62.375);


            // Флан кокос и чернослив
            //DiscountProgram.discountOnCountProductInCheck(check, 6, 8, 81.125);


            // Флан апельсин лимон ягодны шоколад
            //DiscountProgram.discountOnCountProductInCheck(check, 16, 8, 93.625);

            // Акция на панини комбо с 11:00 до 15:00
            // DiscountProgram.initPaniniWithTimeLimit(check);

        }

    }

    public static void initPaniniWithTimeLimit(CheckObject check) {

        String startTime = "11:00:00";
        String endTime = "15:00:00";

        //Сегодняшняя дата
        Date dateToday = new Date();

        //Формат для сегодняшней даты
        SimpleDateFormat formatForDateLimit = new SimpleDateFormat("dd.MM.yyyy");

        //Сохранем отформатированную сегодняшнюю дату в переменной
        String dateTodayString = formatForDateLimit.format(dateToday);

        //Создаем лимит после какой даты и врмени можно будет сделать скидку
        Date startTimeLimit = null;
        try {
            startTimeLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayString + " " + startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Сегодняшняя дата
        Date dateToday1 = new Date();

        //Формат для сегодняшней даты
        SimpleDateFormat formatForDateEndLimit = new SimpleDateFormat("dd.MM.yyyy");

        //Сохранем отформатированную сегодняшнюю дату в переменной
        String dateTodayStringEnd = formatForDateEndLimit.format(dateToday1);

        //Создаем лимит после какой даты и врмени можно будет сделать скидку
        Date endTimeLimit = null;
        try {
            endTimeLimit = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTodayStringEnd + " " + endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Создаем текущее дату и время
        Date curentDate = new Date();

        //Сравниваем текщую дату с лимитом
        if (curentDate.after(startTimeLimit) && curentDate.before(endTimeLimit)) {

            onPanini(check);
            onPaniniForPule(check);
        }


    }

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }

    //Скидка 40% на выпечку после 8 вечера
    //Список классификаторов
    public static CheckObject timeDiscount(CheckObject check) {

        String afterTime = "19:20:00";
        Double amountOfDiscount = 0.4;
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

                if (classifier == 13 || classifier == 4 || classifier == 32 || classifier == 39 || classifier == 38) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * amountOfDiscount;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    if(goods.isUnit()){
                        priceAfterDiscount = roundUp(priceAfterDiscount);
                    }else {

                        priceAfterDiscount = roundUpNotUniqProduct(priceAfterDiscount);
                    }

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


    // Скидка по кол-ву продукции в чеке
    public static CheckObject discountOnCountProductInCheck(CheckObject check, int classificatorSet, int countInTheCheckSet, Double priceSet) {

        int classificatorForPromo = classificatorSet;
        int countInTheCheck = countInTheCheckSet;
        Double price = priceSet;

        if (check.getGoodsList().size() > 0) {

            int count = 0;

            // Считаем общее число товара нужного нам классификатора
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == classificatorForPromo) {
                    count++;
                }

            }

            // Если проукта в чеке больше то применяем акцию
            if (count >= countInTheCheck) {

                // Считаем к скольки продуктам надо применить эту акциюю
                int countProductWhichNeedDiscount = (count / countInTheCheck) * countInTheCheck;

                // Считаем общее число товара нужного нам классификатора
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    //Текущий товар
                    Goods goods = check.getGoodsList().get(i);

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    if (countProductWhichNeedDiscount == 0) {
                        return null;
                    }
                    if (classifier == classificatorForPromo) {

                        //Устанавливаем цену со скидкой
                        goods.setPriceAfterDiscount(price);

                        //Количество товара
                        Double countProduct = goods.getCount();

                        //Считаем продажную цену умножая цену после скидки на кол-во товара
                        Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                        // Округляем результат до десятых
                        sellingPrice = roundUp(sellingPrice);

                        //Устанавливаем продажную цену товара
                        goods.setSellingPrice(sellingPrice);

                        countProductWhichNeedDiscount--;
                    }

                }

            }
        }

        return check;
    }

    public static void onAchmaAndTea(CheckObject check) {

        if (check.getGoodsList().size() > 0) {
            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countAchma = 0;
            int countTea = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                // id товара
                int productId = goods.getProductId();
                //Классификатор товара
                int classifier = goods.getClassifier();

                // Нашли ачму
                if (productId == 13 || classifier == 38) {
                    countAchma++;
                }



                // Нашли чай
                if (classifier == 17) {
                    countTea++;
                }
            }

            if (countAchma > 0 && countTea > 0) {

                // Нахожу что наименьшее
                if (countAchma == countTea) {
                    count = countAchma;
                } else {

                    count = countAchma;

                    if (count > countTea) {
                        count = countTea;
                    }
                }

                //count = count * 2;
                countAchma = count;
                countTea = count;

                // Делаем скидку
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(i);

                    // id товара
                    int productId = goods.getProductId();
                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    // Нашли ачму
                    if (productId == 13 || classifier == 38) {
                        if (countAchma > 0) {


                            goods.setPriceAfterDiscount(29.0);

                            //Количество товара
                            Double countProduct = goods.getCount();

                            //Считаем продажную цену умножая цену после скидки на кол-во товара
                            Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                            // Округляем результат
                            sellingPrice = roundUp(sellingPrice);

                            //Устанавливаем продажную цену товара
                            goods.setSellingPrice(sellingPrice);

                            countAchma--;
                        }
                    }



                    // Нашли чай
                    if (classifier == 17) {

                        if (countTea > 0) {

                            goods.setPriceAfterDiscount(30.0);

                            //Количество товара
                            Double countProduct = goods.getCount();

                            //Считаем продажную цену умножая цену после скидки на кол-во товара
                            Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                            //Устанавливаем продажную цену товара
                            goods.setSellingPrice(sellingPrice);

                            countTea--;
                        }
                    }

                    if (countAchma == 0 && countTea == 0) {
                        return;
                    }
                }

            }
        }
    }

    public static void onPanini(CheckObject check) {

        if (check.getGoodsList().size() > 0) {

            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countPanini = 0;
            int countTea = 0;
            int countPalmie = 0;
            int countAmericaner = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                // id товара
                int productId = goods.getProductId();

                //Классификатор товара
                int classifier = goods.getClassifier();

                // Нашли панини
                if (classifier == 15) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countPanini++;
                    }
                }

                // Нашли чай
                if (classifier == 17) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countTea++;
                    }
                }

                // Нашли пальмие
                if (productId == 23) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countPalmie++;
                    }
                }

                // Американер
                if (productId == 250) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countAmericaner++;
                    }
                }

            }

            // Складываем кол-во американера и пальмие
            int countAmericanerAndPalmie = countAmericaner + countPalmie;

            if (countPanini > 0 && countTea > 0 && countAmericanerAndPalmie > 0) {

                // Нахожу наименьшее значение
                count = countPanini;

                if (count > countTea) {
                    count = countTea;
                }

                if (count > countAmericanerAndPalmie) {
                    count = countAmericanerAndPalmie;
                }

                //count = count * 3;

                countPanini = count;
                countTea = count;


                /*if (count < countPalmie) {
                    countPalmie = count;
                }
                if (count < countAmericaner) {
                    countAmericaner = count;
                }*/

                if(count-countPalmie > 0) {
                    countAmericaner = count-countPalmie;
                }else {
                    countAmericaner = 0;
                }



                int amountCount = count * 3;

                // Делаем скидку равномерно на все три позиции
                for (int y = 0; y < check.getGoodsList().size(); y++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(y);

                    // id товара
                    int productId = goods.getProductId();

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    // Нашли панини
                    if (classifier == 15) {
                        if (countPanini > 0) {


                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 15.0;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countPanini--;

                                amountCount--;
                            }
                        }
                    }

                    // Нашли чай
                    if (classifier == 17) {
                        if (countTea > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countTea--;
                                amountCount--;
                            }
                        }

                    }

                    // Нашли пальмие
                    if (productId == 23) {
                        if (countPalmie > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countPalmie--;
                                amountCount--;
                            }
                        }
                    }

                    if (amountCount == 0) {
                        return;
                    }

                    // Американер
                    if (productId == 250) {

                        if (countAmericaner > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countAmericaner--;
                                amountCount--;
                            }
                        }
                    }

                    if (amountCount == 0) {
                        return;
                    }
                }

            }

        }
    }

    public static void onPaniniForPule(CheckObject check) {

        if (check.getGoodsList().size() > 0) {
            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countPanini = 0;
            int countTea = 0;
            int countPalmie = 0;
            int countAmericaner = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                // id товара
                int productId = goods.getProductId();

                //Классификатор товара
                int classifier = goods.getClassifier();

                // Нашли панини
                if (classifier == 18) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countPanini++;
                    }
                }

                // Нашли чай
                if (classifier == 17) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countTea++;
                    }
                }

                // Нашли пальмие
                if (productId == 23) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countPalmie++;
                    }
                }

                // Американер
                if (productId == 250) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countAmericaner++;
                    }
                }

            }

            // Складываем кол-во американера и пальмие
            int countAmericanerAndPalmie = countAmericaner + countPalmie;

            if (countPanini > 0 && countTea > 0 && countAmericanerAndPalmie > 0) {

                // Нахожу наименьшее значение
                count = countPanini;

                if (count > countTea) {
                    count = countTea;
                }

                if (count > countAmericanerAndPalmie) {
                    count = countAmericanerAndPalmie;
                }

                // count = count * 3;

                countPanini = count;
                countTea = count;

                if(count-countPalmie > 0) {
                    countAmericaner = count-countPalmie;
                }else {
                    countAmericaner = 0;
                }

                int amountCount = count * 3;

                // Делаем скидку равномерно на все три позиции
                for (int y = 0; y < check.getGoodsList().size(); y++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(y);

                    // id товара
                    int productId = goods.getProductId();

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    // Нашли панини
                    if (classifier == 18) {

                        if (countPanini > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 5.0;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countPanini--;
                                amountCount--;
                            }
                        }
                    }

                    // Нашли чай
                    if (classifier == 17) {
                        if (countTea > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countTea--;
                                amountCount--;
                            }
                        }
                    }

                    // Нашли пальмие
                    if (productId == 23) {
                        if (countPalmie > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countPalmie--;
                                amountCount--;
                            }
                        }
                    }

                    if (amountCount == 0) {
                        return;
                    }

                    // Американер
                    if (productId == 250) {
                        if (countAmericaner > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countAmericaner--;
                                amountCount--;
                            }
                        }
                    }

                    if (amountCount == 0) {
                        return;
                    }
                }

            }
        }
    }

    public static void onCroissant(CheckObject check) {

        if (check.getGoodsList().size() > 0) {
            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countCroissant = 0;
            int countTea = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Классификатор товара
                int classifier = goods.getClassifier();
                int product_id = goods.getProductId();

                // Нашли ачму
                if (classifier == 4 || product_id == 21 || product_id == 387) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countCroissant++;
                    }
                }

                // Нашли чай
                if (classifier == 17) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countTea++;
                    }
                }
            }

            if (countCroissant > 0 && countTea > 0) {

                // Нахожу что наименьшее
                if (countCroissant == countTea) {
                    count = countCroissant;
                } else {

                    count = countCroissant;

                    if (count > countTea) {
                        count = countTea;
                    }
                }

                countCroissant = count;
                countTea = count;

                // Делаем скидку
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(i);


                    //Классификатор товара
                    int classifier = goods.getClassifier();
                    int product_id = goods.getProductId();

                    // Нашли ачму
                    if (classifier == 4 ||  product_id == 21 || product_id == 387) {

                        if (countCroissant > 0) {


                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 8;



                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countCroissant--;
                            }
                        }
                    }


                    // Нашли чай
                    if (classifier == 17) {

                        if (countTea > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 8;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countTea--;
                            }
                        }
                    }

                    if (countCroissant == 0 && countTea == 0) {
                        return;
                    }
                }
            }
        }
    }

    public static void onBrioshAndTea(CheckObject check) {

        if (check.getGoodsList().size() > 0) {
            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countBriosh = 0;
            int countTea = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                // id товара
                int productId = goods.getProductId();

                //Классификатор товара
                int classifier = goods.getClassifier();

                // Нашли бриошь
                if (productId == 340 || productId == 341) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countBriosh++;
                    }
                }



                // Нашли чай
                if (classifier == 17) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countTea++;
                    }
                }
            }

            if (countBriosh > 0 && countTea > 0) {

                // Нахожу что наименьшее
                if (countBriosh == countTea) {
                    count = countBriosh;
                } else {

                    count = countBriosh;

                    if (count > countTea) {
                        count = countTea;
                    }
                }

                //count = count * 2;
                countBriosh = count;
                countTea = count;

                // Делаем скидку
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(i);

                    // id товара
                    int productId = goods.getProductId();

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    // Нашли бриошь
                    if (productId == 340 || productId == 341) {
                        if (countBriosh > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                goods.setPriceAfterDiscount(19.0);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countBriosh--;
                            }
                        }
                    }



                    // Нашли чай
                    if (classifier == 17) {

                        if (countTea > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                goods.setPriceAfterDiscount(30.0);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countTea--;
                            }
                        }
                    }

                    if (countBriosh == 0 && countTea == 0) {
                        return;
                    }
                }

            }
        }
    }

    public static void cappuccinoAndCroissant(CheckObject check) {

        if (check.getGoodsList().size() > 0) {
            // Считаю кол-во ачмы и кол-во чая нахожу минимальное значение и на это значение делаю скидку на ачму и чай

            int countCroissant = 0;
            int countTea = 0;
            int count = 0;

            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Классификатор товара
                int classifier = goods.getClassifier();
                int product_id = goods.getProductId();

                // Нашли эскарго / круассан
                if (classifier == 4 || product_id == 455 || product_id == 459 || product_id == 460 || product_id == 461 || product_id == 462) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countCroissant++;
                    }
                }

                // Нашли капучино
                if (classifier == 31) {
                    if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                        countTea++;
                    }
                }
            }

            if (countCroissant > 0 && countTea > 0) {

                // Нахожу что наименьшее
                if (countCroissant == countTea) {
                    count = countCroissant;
                } else {

                    count = countCroissant;

                    if (count > countTea) {
                        count = countTea;
                    }
                }

                countCroissant = count;
                countTea = count;

                // Делаем скидку
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    // Текущий товар
                    Goods goods = check.getGoodsList().get(i);


                    //Классификатор товара
                    int classifier = goods.getClassifier();
                    int product_id = goods.getProductId();

                    // Нашли ачму
                    if (classifier == 4 || product_id == 455 || product_id == 459 || product_id == 460 || product_id == 461 || product_id == 462) {

                        if (countCroissant > 0) {


                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 14;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countCroissant--;
                            }
                        }
                    }


                    // Нашли чай
                    if (classifier == 31) {

                        if (countTea > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 11;

                                // КОстыль
                                if (product_id == 72) {
                                    price = goods.getPriceFromThePriceList() - 12;
                                }
                                if (product_id == 74) {
                                    price = goods.getPriceFromThePriceList() - 17;
                                }

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                                // Округляем до десятых
                                sellingPrice = roundUp(sellingPrice);

                                //Устанавливаем продажную цену товара
                                goods.setSellingPrice(sellingPrice);

                                countTea--;
                            }
                        }
                    }

                    if (countCroissant == 0 && countTea == 0) {
                        return;
                    }
                }
            }
        }
    }

    // Метод округления
    private static Double roundUp(Double numeral){

        numeral = new BigDecimal(numeral).setScale(1, RoundingMode.HALF_UP).doubleValue();

        return numeral;
    }

    // Берешь опредленное количество и один в подарок
    public static CheckObject discountOneFree(CheckObject check, int classificatorSet, int countInTheCheckSet, int presentClassifier) {
        if (check.getGoodsList().size() > 0) {

            int count = 0;

            // Считаем общее число товара нужного нам классификатора
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == classificatorSet) {
                    count++;
                }

            }

            // Если проукта в чеке больше то применяем акцию
            if (count >= countInTheCheckSet) {

                // Считаем к скольки продуктам надо применить эту акциюю
                int countProductWhichNeedDiscount = count / countInTheCheckSet;

                // Считаем общее число товара нужного нам классификатора
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    //Текущий товар
                    Goods goods = check.getGoodsList().get(i);

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    if (countProductWhichNeedDiscount == 0) {
                        return null;
                    }
                    if (classifier == classificatorSet || classifier == presentClassifier) {

                        //Устанавливаем цену со скидкой
                        goods.setPriceAfterDiscount(0.0);

                        //Количество товара
                        Double countProduct = goods.getCount();

                        //Считаем продажную цену умножая цену после скидки на кол-во товара
                        Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                        // Округляем результат до десятых
                        sellingPrice = roundUp(sellingPrice);

                        //Устанавливаем продажную цену товара
                        goods.setSellingPrice(sellingPrice);

                        countProductWhichNeedDiscount--;
                    }

                }

            }
        }

        return check;
    }

    // Метод округления для кол-венных товаров
    private static Double roundUpNotUniqProduct(Double numeral){

        numeral = new BigDecimal(numeral).setScale(3, RoundingMode.HALF_UP).doubleValue();

        return numeral;
    }
}
