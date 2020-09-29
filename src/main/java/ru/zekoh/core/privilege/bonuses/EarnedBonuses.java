package ru.zekoh.core.privilege.bonuses;

import ru.zekoh.db.CheckObject;

// Бонусы заработанные клиентом в игрофикации системы лоялности
// которыми можно оплатить макимум 30% стоимости продукции
// собственного производства
public class EarnedBonuses implements Bonus {
    // Количество бонусов
    private Double amountOfBonuses = 0.0;

    public EarnedBonuses(Double amountOfBonuses) {
        this.amountOfBonuses = amountOfBonuses;
    }

    @Override
    public Double getAmountOfBonuses() {
        return amountOfBonuses;
    }

    @Override
    public Integer getDateLimit() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void pay(CheckObject checkObject) {
    //todo логика оплаты бонусами и проверка что бы сумма была не более 30% от стоимости
        //товаров собственного производсвта

    }
}
