package ru.zekoh.core;

public class KKTError {
    private boolean status = false;
    private int number = 0;
    private String description = "Чек не напечатан";

    public KKTError() {
    }

    public KKTError(boolean status, int number, String description) {
        this.status = status;
        this.number = number;
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
