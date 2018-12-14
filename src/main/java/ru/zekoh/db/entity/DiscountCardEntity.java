package ru.zekoh.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "discount_for_employees", schema = "sale")
public class DiscountCardEntity {
    private int id;
    private long number;
    private String name;
    private Double budgetForTheMonth;
    private Double amountOfDiscount;
    private boolean live;
    private Double balance;
    private int role;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Basic
    @Column(name = "number_card")
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "budget_for_the_month")
    public Double getBudgetForTheMonth() {
        return budgetForTheMonth;
    }

    public void setBudgetForTheMonth(Double budgetForTheMonth) {
        this.budgetForTheMonth = budgetForTheMonth;
    }

    @Basic
    @Column(name = "amount_of_discount")
    public Double getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    @Basic
    @Column(name = "is_a_live")
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    @Basic
    @Column(name = "balance")
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "role")
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
