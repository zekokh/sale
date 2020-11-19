package ru.zekoh.core.DiscountProgram;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShifrinaDiscountProgram implements DiscountInterface {
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

    //Скидка 30% на выпечку после 8 вечера
    //Список классификаторов
    public static CheckObject timeDiscount(CheckObject check) {

        String afterTime = "20:00:00";
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

            //Делаем 30% скидку на выпечку
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Цена на текущий товар по прайсу
                Double priceFromThePriceList = goods.getPriceFromThePriceList();

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == 13 || classifier == 4 || classifier == 36) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * amountOfDiscount;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    priceAfterDiscount = new BigDecimal(priceAfterDiscount).setScale(1, RoundingMode.HALF_UP).doubleValue();

                    //Устанавливаем цену со скидкой
                    goods.setPriceAfterDiscount(priceAfterDiscount);

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

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

    // Скидка 30% на салат, багет провонцаль и ачму с курицей или овощами
    public static CheckObject timeDiscountonSalad(CheckObject check) {

        String afterTime = "18:00:00";
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

            //Делаем 30% скидку на выпечку
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Цена на текущий товар по прайсу
                Double priceFromThePriceList = goods.getPriceFromThePriceList();

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == 32) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * amountOfDiscount;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    priceAfterDiscount = new BigDecimal(priceAfterDiscount).setScale(1, RoundingMode.HALF_UP).doubleValue();

                    //Устанавливаем цену со скидкой
                    goods.setPriceAfterDiscount(priceAfterDiscount);

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

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

                if (classifier == classificatorForPromo && goods.getPriceAfterDiscount() > 0.0) {
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
                    if (classifier == classificatorForPromo && goods.getPriceAfterDiscount() > 0.0) {

                        //Устанавливаем цену со скидкой
                        goods.setPriceAfterDiscount(price);

                        //Количество товара
                        Double countProduct = goods.getCount();

                        //Считаем продажную цену умножая цену после скидки на кол-во товара
                        Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

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

                // Нашли ачму
                if (productId == 13) {
                    countAchma++;
                }

                //Классификатор товара
                int classifier = goods.getClassifier();

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

                    // Нашли ачму
                    if (productId == 13) {
                        if (countAchma > 0) {


                            goods.setPriceAfterDiscount(29.0);

                            //Количество товара
                            Double countProduct = goods.getCount();

                            //Считаем продажную цену умножая цену после скидки на кол-во товара
                            Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

                            //Устанавливаем продажную цену товара
                            goods.setSellingPrice(sellingPrice);

                            countAchma--;
                        }
                    }

                    //Классификатор товара
                    int classifier = goods.getClassifier();

                    // Нашли чай
                    if (classifier == 17) {

                        if (countTea > 0) {

                            goods.setPriceAfterDiscount(20.0);

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

                if (count - countPalmie > 0) {
                    countAmericaner = count - countPalmie;
                } else {
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

                if (count - countPalmie > 0) {
                    countAmericaner = count - countPalmie;
                } else {
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

                // Нашли ачму
                if (classifier == 4) {
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

                    // Нашли ачму
                    if (classifier == 4) {

                        if (countCroissant > 0) {


                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 8;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

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

                                goods.setPriceAfterDiscount(20.0);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

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

                // Нашли эскарго / круассан
                if (classifier == 4) {
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

                    // Нашли ачму
                    if (classifier == 4) {

                        if (countCroissant > 0) {


                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 6;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

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

                                Double price = goods.getPriceFromThePriceList() - 5;

                                goods.setPriceAfterDiscount(price);

                                //Количество товара
                                Double countProduct = goods.getCount();

                                //Считаем продажную цену умножая цену после скидки на кол-во товара
                                Double sellingPrice = countProduct * goods.getPriceAfterDiscount();

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



    // Промоакция кофе + выпечка
    // К любому кофе от 90 руб. в подарок круассан или выпечка не превышающая 47 руб.
    public static void coffeeGift(CheckObject check) {
        // В чеке должен быть хотя бы один товар
        if (check.getGoodsList().size() > 0) {

            // Количество кофеных напитков соответствующих условию промоакции
            // Классификатор 35
            int numberOfCoffee = 0;

            // Вычисляю количество кофейных напитков попадающих под акцию
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                // Товар
                Goods goods = check.getGoodsList().get(i);

                //Классификатор товара
                int classifier = goods.getClassifier();

                // Вычисляю количество кофейных напитков соответствующих условию промоакции
                if (classifier == 35) {
                    numberOfCoffee++;
                }
            }

            // Если в чеке есть кофейные напитки соответствующих условию промоакции
            if (numberOfCoffee > 0) {

                // Повторяем скидку на выпечку количество раз равную количество кофейных напитков
                // соответствующих условию промоакции
                for (int x = 0; x < numberOfCoffee; x++) {

                    // Ищем выпечку соответствующих условию промоакции
                    for (int n = 0; n < check.getGoodsList().size(); n++) {

                        // Товар
                        Goods g = check.getGoodsList().get(n);

                        // Круассан, Маффин, Эскарго, Ватрушка = 47 руб. (классификатор 4),  Симмит (id: 32), Пирожок (id: 24/25), Ачма(id: 13)
                        // if (g.getClassifier() == 4 || g.getProductId() == 32 || g.getProductId() == 24 || g.getProductId() == 25 || g.getProductId() == 13) {
                        // g.getClassifier() == 36 - Пироги
                        if (g.getClassifier() == 4 || g.getClassifier() == 36) {

                            // Проверяем действия акции на товар
                            if (g.getPriceAfterDiscount() > 0.0) {
                                productDiscount(g, 0.0);

                                numberOfCoffee--;
                                if (x == numberOfCoffee) {
                                    return;
                                }
                            } else {
                                numberOfCoffee--;
                                if (x == numberOfCoffee) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static void teaAndProduct(CheckObject check) {

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
                if (classifier == 4 || classifier == 5 || classifier == 9) {
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
                    if (classifier == 4 || classifier == 5 || classifier == 9) {
                        if (countBriosh > 0) {
                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {
                                Double price = goods.getPriceFromThePriceList() - 9;
                                productDiscount(goods, price);

                                countBriosh--;
                            }
                        }
                    }


                    // Нашли чай
                    if (classifier == 17) {

                        if (countTea > 0) {

                            if (areEqualDouble(goods.getPriceFromThePriceList(), goods.getPriceAfterDiscount(), 2)) {

                                Double price = goods.getPriceFromThePriceList() - 9;
                                productDiscount(goods, price);

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

    // Скидка на продукт в чеке
    private static void productDiscount(Goods good, Double newPrice) {

        Double price = newPrice;

        good.setPriceAfterDiscount(price);

        //Количество товара
        Double countProduct = good.getCount();

        //Считаем продажную цену умножая цену после скидки на кол-во товара
        Double sellingPrice = countProduct * good.getPriceAfterDiscount();

        //Устанавливаем продажную цену товара
        good.setSellingPrice(sellingPrice);
    }

    //Скидка 30% на ачму, салат, провонсаль после 19:00 вечера
    //Список классификаторов
    public static CheckObject timeDiscountAfterSeven(CheckObject check) {

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

            //Делаем 20% скидку на выпечку
            for (int i = 0; i < check.getGoodsList().size(); i++) {

                //Текущий товар
                Goods goods = check.getGoodsList().get(i);

                //Цена на текущий товар по прайсу
                Double priceFromThePriceList = goods.getPriceFromThePriceList();

                //Классификатор товара
                int classifier = goods.getClassifier();

                if (classifier == 32) {

                    //Сумма скидки
                    Double discountAmount = priceFromThePriceList * amountOfDiscount;

                    //Цена на товар со скидкой
                    Double priceAfterDiscount = priceFromThePriceList - discountAmount;

                    priceAfterDiscount = new BigDecimal(priceAfterDiscount).setScale(1, RoundingMode.HALF_UP).doubleValue();

                    //Устанавливаем цену со скидкой
                    goods.setPriceAfterDiscount(priceAfterDiscount);

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

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

            // Вернуть ценны по прайсу
            //List<Goods> goods = check.getGoodsList();

            for (int i = 0; i < goodsList.size(); i++) {

                goodsList.get(i).setPriceAfterDiscount(goodsList.get(i).getPriceFromThePriceList());
            }


            CheckObject tempCheck = timeDiscount(check);
            if (tempCheck == null) {

                coffeeGift(check);

                // 5 круаасан по цене 228р.
                discountOnCountProductInCheck(check, 4, 5, 45.6);

                teaAndProduct(check);
            }else {
                coffeeGift(check);
            }

            timeDiscountAfterSeven(check);

            // Акции которые не с чем не пересикаются
            // 6 эклеров по цене 5 (6 эклеров = 195р)
            discountOnCountProductInCheck(check, 5, 6, 32.5);

            // 10 макаронс по цене 315
            discountOnCountProductInCheck(check, 21, 10, 31.5);



        }
    }
}
