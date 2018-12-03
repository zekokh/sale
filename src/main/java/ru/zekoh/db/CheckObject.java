package ru.zekoh.db;

import ru.zekoh.db.entity.Goods;

import java.util.ArrayList;
import java.util.List;

public class CheckObject {

    //Список товаров
    private List<Goods> goodsList = new ArrayList<Goods>();

    //Итоговая стоимость за весь чек с учетом скидок
    private Double sellingPrice = 0.00;

    //Сумма чека по прайсу
    private Double amountByPrice;

    //Статус оплаты
    private boolean paymentState = false;

    //Вид оплаты (Наличка (1), безналичный(2), промоушен(3), бонусами(4))
    private int typeOfPayment;

    //Дата создания чека (фиксируем дату создания когда в чеке появляетя первый товар)
    private String dateOfCreation;

    //Дата закрытие чека
    private String dateOfClosing;

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getAmountByPrice() {
        return amountByPrice;
    }

    public void setAmountByPrice(Double amountByPrice) {
        this.amountByPrice = amountByPrice;
    }

    public boolean isPaymentState() {
        return paymentState;
    }

    public void setPaymentState(boolean paymentState) {
        this.paymentState = paymentState;
    }

    public int getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(int typeOfPayment) {
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
}
