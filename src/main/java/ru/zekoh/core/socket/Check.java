package ru.zekoh.core.socket;

import java.util.List;

public class Check {

    private List<Good> goods;
    private String sellingPrice;
    private String priceWithoutDiscounts;
    private String discount;

    public Check() {
    }

    public Check(List<Good> goods, String sellingPrice, String priceWithoutDiscounts, String discount) {
        this.goods = goods;
        this.sellingPrice = sellingPrice;
        this.priceWithoutDiscounts = priceWithoutDiscounts;
        this.discount = discount;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getPriceWithoutDiscounts() {
        return priceWithoutDiscounts;
    }

    public void setPriceWithoutDiscounts(String priceWithoutDiscounts) {
        this.priceWithoutDiscounts = priceWithoutDiscounts;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
