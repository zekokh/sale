package ru.zekoh.db.entity;

public class DiscountForEmployees {

    //id
    private int id;

    //Номер карты
    private long numberCard;

    //Имя
    private String name;

    //Бюджет на месяц
    private Double budgetForTheMonth;

    //Сумма скидки в процентах
    private Double amountOfDiscount;

    //Израсходованно в этом месяце
    private Double balance;

    // Кол-во бонусов
    private Double bonus;

    // Уровень в системе Жак-Андрэ Бонус
    private int level;

    //Статус жизни
    private boolean is_a_live;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getNumberCard() {
        return numberCard;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public void setNumberCard(long numberCard) {
        this.numberCard = numberCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBudgetForTheMonth() {
        return budgetForTheMonth;
    }

    public void setBudgetForTheMonth(Double budgetForTheMonth) {
        this.budgetForTheMonth = budgetForTheMonth;
    }

    public Double getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isIs_a_live() {
        return is_a_live;
    }

    public void setIs_a_live(boolean is_a_live) {
        this.is_a_live = is_a_live;
    }
}
