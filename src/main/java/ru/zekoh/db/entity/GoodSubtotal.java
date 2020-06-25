package ru.zekoh.db.entity;

public class GoodSubtotal {
    private int id;
    private int check_id;
    private int generalId;
    private int productId;
    private String productName;
    private Double quantity;
    private Double priceFromThePriceList;
    private Double priceAfterDiscount;
    private Double sellingPrice;

    public GoodSubtotal(int id, int check_id, int generalId, int productId, String productName, Double quantity, Double priceFromThePriceList, Double priceAfterDiscount, Double sellingPrice) {
        this.id = id;
        this.check_id = check_id;
        this.generalId = generalId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.priceFromThePriceList = priceFromThePriceList;
        this.priceAfterDiscount = priceAfterDiscount;
        this.sellingPrice = sellingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheck_id() {
        return check_id;
    }

    public void setCheck_id(int check_id) {
        this.check_id = check_id;
    }

    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPriceFromThePriceList() {
        return priceFromThePriceList;
    }

    public void setPriceFromThePriceList(Double priceFromThePriceList) {
        this.priceFromThePriceList = priceFromThePriceList;
    }

    public Double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(Double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
