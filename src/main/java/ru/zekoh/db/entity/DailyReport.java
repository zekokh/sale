package ru.zekoh.db.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DailyReport {

    //Доход за день
    private Double soldPerDay;

    //Количество чеков за день
    private int numberOfChecks;

    //Возвратов на сумму
    private Double returnPerDay;

    //Сумма наличкой
    private Double amountCash;

    //Сумма картой
    private Double amountCard;

    public Double getSoldPerDay() {
        Double temp = new BigDecimal(soldPerDay).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return temp;
    }

    public void setSoldPerDay(Double soldPerDay) {
        this.soldPerDay = soldPerDay;
    }

    public int getNumberOfChecks() {
        return numberOfChecks;
    }

    public void setNumberOfChecks(int numberOfChecks) {
        this.numberOfChecks = numberOfChecks;
    }

    public Double getReturnPerDay() {
        Double temp = new BigDecimal(returnPerDay).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return temp;
    }

    public void setReturnPerDay(Double returnPerDay) {
        this.returnPerDay = returnPerDay;
    }

    public Double getAmountCash() {
        Double temp = new BigDecimal(amountCash).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return temp;
    }

    public void setAmountCash(Double amountCash) {
        this.amountCash = amountCash;
    }

    public Double getAmountCard() {
        Double temp = new BigDecimal(amountCard).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return temp;
    }

    public void setAmountCard(Double amountCard) {
        this.amountCard = amountCard;
    }
}
