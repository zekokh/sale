package ru.zekoh.db.entity;

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
    private Double count;

    // Весовой товар
    private boolean unit;

    // id классификатора
    private int parentId;

    public Goods() {

    }

    public Goods(int productId, int generalId, String productName, int classifier, Double count, Double priceFromThePriceList, Double priceAfterDiscount, Double sellingPrice, boolean unit, int parentId) {
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
        this.priceAfterDiscount = priceAfterDiscount;
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
}
