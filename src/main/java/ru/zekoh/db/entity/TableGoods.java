package ru.zekoh.db.entity;

import java.util.function.DoubleUnaryOperator;

public class TableGoods {

    private  int id;
    private String name;
    private Double price;
    private Double count;
    private Double priceAfterDiscount;
    private Double sellingPrice;

    public TableGoods(int id, String name, Double price, Double count, Double priceAfterDiscount, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.priceAfterDiscount = priceAfterDiscount;
        this.sellingPrice = sellingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
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
