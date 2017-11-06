package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Promocod;

import java.util.List;

public interface PromocodDao {

    //Получить вcе активные папки
    public Promocod getPromocodByNumber(int number);
    public boolean use(Promocod promocod);
}
