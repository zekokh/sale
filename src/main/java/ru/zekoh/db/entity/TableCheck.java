package ru.zekoh.db.entity;

public class TableCheck {
    private int id;
    private String date;
    private Double total;
    private Double priceOfprice;
    private Double amountOfbonus;
    private String typePayment;
    private String returnStatus;

    public TableCheck(int id, String date, Double total, Double priceOfprice, Double amountOfbonus, String typePayment, String returnStatus) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.priceOfprice = priceOfprice;
        this.amountOfbonus = amountOfbonus;
        this.typePayment = typePayment;
        this.returnStatus = returnStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getPriceOfprice() {
        return priceOfprice;
    }

    public void setPriceOfprice(Double priceOfprice) {
        this.priceOfprice = priceOfprice;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }

    public Double getAmountOfbonus() {
        return amountOfbonus;
    }

    public void setAmountOfbonus(Double amountOfbonus) {
        this.amountOfbonus = amountOfbonus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }
}
