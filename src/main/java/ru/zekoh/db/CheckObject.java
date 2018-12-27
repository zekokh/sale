package ru.zekoh.db;

import ru.zekoh.db.entity.Discount;
import ru.zekoh.db.entity.Goods;

import java.util.ArrayList;
import java.util.List;

public class CheckObject {

    //Список товаров
    private List<Goods> goodsList = new ArrayList<Goods>();

    //Итоговая стоимость за весь чек с учетом скидок
    private Double sellingPrice = 0.0;

    //Сумма чека по прайсу
    private Double amountByPrice = 0.0;

    //Сумма оплаченная бонусами
    private Double amountBonus = 0.0;

    //Статус оплаты
    private boolean paymentState = false;

    //Вид оплаты (Наличка (1), безналичный(2), промоушен(3), бонусами(4))
    private int typeOfPayment;

    //Дата создания чека (фиксируем дату создания когда в чеке появляетя первый товар)
    private String dateOfCreation;

    //Дата закрытие чека
    private String dateOfClosing;

    // Статус жизни чека
    private boolean live = true;

    // Статус отображения окна для ввода скидок
    private boolean panelForFindDiscountCard = false;

    // Скидка по карте
    private boolean discountAccountExist = false;

    // Скидка по карте
    private boolean discountAppExist = false;

    // Скидка
    private Discount discount = null;

    // Блокировка продажи
    private boolean blockForSale = false;


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

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Double getAmountBonus() {
        return amountBonus;
    }

    public void setAmountBonus(Double amountBonus) {
        this.amountBonus = amountBonus;
    }

    public boolean isPanelForFindDiscountCard() {
        return panelForFindDiscountCard;
    }

    public void setPanelForFindDiscountCard(boolean panelForFindDiscountCard) {
        this.panelForFindDiscountCard = panelForFindDiscountCard;
    }

    public boolean isDiscountAccountExist() {
        return discountAccountExist;
    }

    public void setDiscountAccountExist(boolean discountAccountExist) {
        this.discountAccountExist = discountAccountExist;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public boolean isBlockForSale() {
        return blockForSale;
    }

    public void setBlockForSale(boolean blockForSale) {
        this.blockForSale = blockForSale;
    }

    public boolean isDiscountAppExist() {
        return discountAppExist;
    }

    public void setDiscountAppExist(boolean discountAppExist) {
        this.discountAppExist = discountAppExist;
    }
}
