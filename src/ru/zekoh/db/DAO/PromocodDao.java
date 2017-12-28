package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Promocod;

import java.util.List;

public interface PromocodDao {

    //Получить вcе активные папки
    public Promocod getPromocodByNumber(int number);

    //Использовали промокод
    public boolean use(Promocod promocod);

    //Выбрать промокод для печати на принтере
    public Promocod getPromocodForPrint();

    //Проверка промокода
    public boolean checkPromoFromBild(int promocod);
}
