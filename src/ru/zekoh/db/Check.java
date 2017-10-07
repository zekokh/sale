package ru.zekoh.db;

import ru.zekoh.db.entity.Goods;

import java.util.ArrayList;
import java.util.List;

public class Check {

    //id
    private int id;

    //Список товаров
    private List<Goods> goodsList = new ArrayList<Goods>();

    //Сумма чека по прайсу
    private Double amountByPrice;

    //Итоговая стоимость за весь чек с учетом скидок
    private Double total = 0.00;

    //Статус оплаты
    private boolean paymentState = false;

    //Наличие скидки на товарную позицию
    private boolean discountOnGoods = false;

    //Наличие скидки на весь чек
    private boolean discountOnCheck = false;

    //Вид оплаты (Наличка (1), безналичный(2), промоушен(3), бонусами(4))
    private int typeOfPayment;

    //Дата создания чека (фиксируем дату создания когда в чеке появляетя первый товар)
    private String dateOfCreation;

    //Дата закрытие чеак
    private String dateOfClosing;

    //Статус возврата
    private boolean returnStatus = false;

    //Статус жизни объетка
    private boolean isALive = true;

    //Флаг для фиксации содержит ли чек товары или чек еще пустой
    private boolean containGoods = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
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

    public boolean isPaymentState() {
        return paymentState;
    }

    public void setPaymentState(boolean paymentState) {
        this.paymentState = paymentState;
    }

    public boolean isDiscountOnGoods() {
        return discountOnGoods;
    }

    public void setDiscountOnGoods(boolean discountOnGoods) {
        this.discountOnGoods = discountOnGoods;
    }

    public boolean isDiscountOnCheck() {
        return discountOnCheck;
    }

    public void setDiscountOnCheck(boolean discountOnCheck) {
        this.discountOnCheck = discountOnCheck;
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

    public boolean isReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

    public boolean isALive() {
        return isALive;
    }

    public void setALive(boolean ALive) {
        isALive = ALive;
    }

    public boolean isContainGoods() {
        return containGoods;
    }

    public void setContainGoods(boolean containGoods) {
        this.containGoods = containGoods;
    }
}
