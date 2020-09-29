package ru.zekoh.db.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Goods {

    //id чека
    private int checkId;

    //id товара
    private int productId;

    //id для синхранизации с сервером статистика
    private int generalId;

    //Классификатор
    private int classifier;

    //Наименования товара
    private String productName;

    //Цена по прайсу
    private Double priceFromThePriceList;

    //Продажная цена (цена со скидкой)
    private Double sellingPrice;

    //Цена после скидок
    private Double priceAfterDiscount;

    //Количество товара для дробных товаров
    private Double count = 1.0;

    // Весовой товар
    private boolean unit;

    // id классификатора
    private int parentId;

    // Участвует ли продукт в промоакциях
    private boolean participatesInpromotions = true;

    //Товар добавлен в чек в подарок
    private boolean gift = false;

    //todo Персональная скидка на товар


    // Максимальная скидка которая может быть установлена на продукт
    private Double maxDiscount = 50.0;

    public Goods() {

    }

    public Goods(int productId, int generalId, String productName, int classifier, Double count, Double priceFromThePriceList, Double priceAfterDiscount, Double sellingPrice, boolean unit, int parentId, boolean participatesInpromotions) {
        this.productId = productId;
        this.generalId = generalId;
        this.productName = productName;
        this.classifier = classifier;
        this.count = count;
        this.priceFromThePriceList = priceFromThePriceList;
        this.priceAfterDiscount = priceAfterDiscount;
        this.sellingPrice = sellingPrice;
        this.unit = unit;
        this.parentId = parentId;
        this.participatesInpromotions = participatesInpromotions;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPriceFromThePriceList() {
        return priceFromThePriceList;
    }

    public void setPriceFromThePriceList(Double priceFromThePriceList) {
        this.priceFromThePriceList = priceFromThePriceList;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(Double priceAfterDiscount) {
        // Устанавливаем стоимость товара после скидки за единицу
        this.priceAfterDiscount = priceAfterDiscount;

        // Перещитываем стоимость sellingPrice
        // Округляем до десятых
        Double total = priceAfterDiscount * count;
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        setSellingPrice(total);
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public int getClassifier() {
        return classifier;
    }

    public void setClassifier(int classifier) {
        this.classifier = classifier;
    }

    public boolean isUnit() {
        return unit;
    }

    public void setUnit(boolean unit) {
        this.unit = unit;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isParticipatesInpromotions() {
        return participatesInpromotions;
    }

    public void setParticipatesInpromotions(boolean participatesInpromotions) {
        this.participatesInpromotions = participatesInpromotions;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }
}
