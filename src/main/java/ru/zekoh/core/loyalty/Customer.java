package ru.zekoh.core.loyalty;

public class Customer implements StoreCard {
    // id

    private long id;
    // Почта
    private String  mail;

    // Имя
    private String  name;

    // Баланс
    private Double balance;

    // Скидка
    private Double discount;

    // Истекает
    private String gracePeriod;

    // Уровень
    private int level;

    //todo Подарки и акции


    public Customer(long id, String mail, String name, Double balance, Double discount, String gracePeriod, int level) {
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.balance = balance;
        this.discount = discount;
        this.gracePeriod = gracePeriod;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
