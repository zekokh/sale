package ru.zekoh.core.loyalty;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Present;

import java.util.List;

public class CustomerCard extends Card implements LoyaltyCard {
    long id;
    String mail;
    String name;
    Double balance;
    boolean payWithBonuses = false;
    int level;
    boolean existPresents;
    List<Present> presentList;

    public CustomerCard(long id, String mail, String name, Double balance, int level, boolean existPresents) {
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.balance = balance;
        this.level = level;
        this.existPresents = existPresents;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    // Использовать алгоритм скидок для этого типа карт
    public void usePrivileges(CheckObject checkObject) {
        // Если необходимо списать бонусы
        // Получить
    }

    @Override
    public boolean saveInDB() {
        return false;
    }

    @Override
    public boolean pushOnServer() {
        return false;
    }

    @Override
    public boolean isExistPresents() {
        return existPresents;
    }

    @Override
    public String getInfoText() {
        String text = "Всего бонусов: " + balance + "\n" + "Вы можете оплатить бонусами: ";
        return null;
    }

    @Override
    public Double getBalance() {
        return balance;
    }

    public void setExistPresents(boolean existPresents) {
        this.existPresents = existPresents;
    }
}
