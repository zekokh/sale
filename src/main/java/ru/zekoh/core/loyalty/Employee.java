package ru.zekoh.core.loyalty;

import ru.zekoh.db.entity.Present;

import java.util.List;

public class Employee implements StoreCard {

    // id

    private long id;
    // ФИО
    private String  name;

    // Баланс
    private Double balance;

    // Скидка
    private Double discount;

    // Лимит
    private Double limit;


    // Истекает
    private String gracePeriod;

    public Employee(Long id, String  name, Double balance, Double discount, Double limit, String gracePeriod){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.discount = discount;
        this.limit = limit;
        this.gracePeriod = gracePeriod;
    }

    public Employee() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isExistPresents() {
        return false;
    }

    @Override
    public void setPresents(List<Present> presentList) {

    }

    @Override
    public void setExistPresents(boolean existPresents) {

    }

    @Override
    public List<Present> getPresents() {
        return null;
    }
}
