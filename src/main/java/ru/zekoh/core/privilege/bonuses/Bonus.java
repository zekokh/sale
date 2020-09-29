package ru.zekoh.core.privilege.bonuses;

import ru.zekoh.db.CheckObject;

public interface Bonus {
    // Возвращает сумму бонусов
    Double getAmountOfBonuses();
    // Возвращает дату до которой действуют бонусы
    Integer getDateLimit();
    // Возвращает описания за что были начислены бонусы
    String getDescription();
    void pay(CheckObject checkObject);
}
