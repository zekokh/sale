package ru.zekoh.db.entity;

public class Goods {

    //id чека
    private int checkId;

    //id товара
    private int productId;

    //id для синхранизации с сервером статистика
    private int generalId;

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

    public Goods() {

    }

    public Goods(int productId, int generalId, String productName, Double count, Double priceFromThePriceList, Double priceAfterDiscount, Double sellingPrice) {
        this.productId = productId;
        this.generalId = generalId;
        this.productName = productName;
        this.count = count;
        this.priceFromThePriceList = priceFromThePriceList;
        this.priceAfterDiscount = priceAfterDiscount;
        this.sellingPrice = sellingPrice;
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
}
