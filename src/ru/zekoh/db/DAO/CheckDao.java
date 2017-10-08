package ru.zekoh.db.DAO;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.Goods;

public interface CheckDao {

    //Создаем чек
    public Check createCheck();

    //Добавить товар в чек
    public boolean addGoodsToCheck(Check check, Goods goods);

    //Обновляем данные в чеке
    public boolean updateCheck(Check check);

    //Удалить товар из чека
    public boolean deleteGoodsFromCheck(Goods goods);

    //Удалить чек
    public boolean deleteCheck(Check check);

}
