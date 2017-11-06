package ru.zekoh.db.entity;

public class Promocod {

    //Номер промокода
    private int number;

    //Статус использования промокода
    private boolean use;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
