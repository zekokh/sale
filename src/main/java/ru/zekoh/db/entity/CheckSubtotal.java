package ru.zekoh.db.entity;

import java.util.List;

public class CheckSubtotal {

    private int id;
    // Сумма по прайсу
    private Double amountByPrice;
    // Продажная цена
    private Double total;
    // Тип оплаты
    private Integer typeOfPayment;
    // Дата создания
    private String dateOfCreation;
    // Дата закрытия
    private String dateOfClosing;
    // Возврат
    private Boolean returnStatus;
    // Сумма оплаты бонусами
    private Double payWithBonus;
    // Был ли чек физкализирован
    private boolean printStatus = false;

    public CheckSubtotal(int id, Double amountByPrice, Double total, Integer typeOfPayment, String dateOfCreation, String dateOfClosing, Boolean returnStatus, Double payWithBonus, boolean printStatus,) {
        this.id = id;
        this.amountByPrice = amountByPrice;
        this.total = total;
        this.typeOfPayment = typeOfPayment;
        this.dateOfCreation = dateOfCreation;
        this.dateOfClosing = dateOfClosing;
        this.returnStatus = returnStatus;
        this.payWithBonus = payWithBonus;
        this.printStatus = printStatus;
    }

    // Список товаров в чеке
    private List<Goods> goodsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAmountByPrice() {
        return amountByPrice;
    }

    public void setAmountByPrice(Double amountByPrice) {
        this.amountByPrice = amountByPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(Integer typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDateOfClosing() {
        return dateOfClosing;
    }

    public void setDateOfClosing(String dateOfClosing) {
        this.dateOfClosing = dateOfClosing;
    }

    public Boolean getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(Boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

    public Double getPayWithBonus() {
        return payWithBonus;
    }

    public void setPayWithBonus(Double payWithBonus) {
        this.payWithBonus = payWithBonus;
    }

    public String getDateOfClosingUnix() {
        return dateOfClosingUnix;
    }

    public void setDateOfClosingUnix(String dateOfClosingUnix) {
        this.dateOfClosingUnix = dateOfClosingUnix;
    }

    public boolean isPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(boolean printStatus) {
        this.printStatus = printStatus;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
