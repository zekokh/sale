package ru.zekoh.db.entity;

public class Discount {

    // id пользователя
    private int id;

    // Имя
    private String name;

    // Прцент скидки
    private Double percentDiscount;

    // Оплата бонусами
    private Double bonus = 0.0;

    // Баланс
    private Double balance = 0.0;

    // Бюджет
    private Double budget = 0.0;

    // Устанавливаем роль карты
    private int discountRole = 0;

    private boolean payWithBonus = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(Double percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public int getDiscountRole() {
        return discountRole;
    }

    public void setDiscountRole(int discountRole) {
        this.discountRole = discountRole;
    }

    public boolean isPayWithBonus() {
        return payWithBonus;
    }

    public void setPayWithBonus(boolean payWithBonus) {
        this.payWithBonus = payWithBonus;
    }
}
