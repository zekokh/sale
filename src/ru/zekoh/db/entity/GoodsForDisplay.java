package ru.zekoh.db.entity;

//Сущность для отображения в ListView
public class GoodsForDisplay {

    //id продукта
    private int productId;

    //Имя товара
    private String name;

    //Количество товара
    private Double count;

    //Цена за одну единицу по прайсу
    private Double priceFromThePriceList;

    //Продажная цена
    private Double sellingPrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
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
}
