package ru.zekoh.db.DAO;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.Goods;

public interface CheckDao {

    //Создаем чек
    public Check createCheck();

    //Добавить товар в чек
    public boolean addGoodsToCheck(Check check, Goods goods);

    //Обновляем данные в чеке
    public boolean updateCheck(Check check);

    //Продано за день
    public DailyReport soldPerDay();

    //Продано за день
    public int numberOfChecks();

    //Возвратов на сумму за день;
    //Продано за день
    public Double returnPerDay();

    //Удалить товар из чека
    public boolean deleteGoodsFromCheck(Goods goods);

    //Удалить чек
    public boolean deleteCheck(Check check);

}
