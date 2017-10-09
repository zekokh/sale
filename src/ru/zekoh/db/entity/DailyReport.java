package ru.zekoh.db.entity;

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
        return soldPerDay;
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
        return returnPerDay;
    }

    public void setReturnPerDay(Double returnPerDay) {
        this.returnPerDay = returnPerDay;
    }

    public Double getAmountCash() {
        return amountCash;
    }

    public void setAmountCash(Double amountCash) {
        this.amountCash = amountCash;
    }

    public Double getAmountCard() {
        return amountCard;
    }

    public void setAmountCard(Double amountCard) {
        this.amountCard = amountCard;
    }
}
