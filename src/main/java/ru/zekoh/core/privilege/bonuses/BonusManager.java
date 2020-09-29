package ru.zekoh.core.privilege.bonuses;

import ru.zekoh.db.CheckObject;

import java.util.List;

// Применяет к чеку бонусы клиента
public class BonusManager {
    private static BonusManager instance;

    public static BonusManager getInstance() {
        if (instance == null) {
            instance = new BonusManager();
        }
        return instance;
    }

    void apply(Bonus bonus, CheckObject check){
        bonus.pay(check);
    }

    void apply(List<Bonus> bonusList, CheckObject check){
        bonusList = sort(bonusList);
        for (Bonus bonus : bonusList) {
            bonus.pay(check);
        }
    }

    // Сортируем лист поднимая бонусы типа GiftBonuses в начало
    // для корректного расчета и применения EarnedBonuses
    // сначало должны применить все GiftBonuses
    List<Bonus> sort(List<Bonus> bonusList){
        return bonusList;
    }
}
