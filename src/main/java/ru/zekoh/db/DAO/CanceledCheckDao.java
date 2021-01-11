package ru.zekoh.db.DAO;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.CanceledCheck;
import ru.zekoh.db.entity.CanceledGood;

import java.util.List;

public interface CanceledCheckDao {

    // Сохраняем чек
    public boolean saveCheck(CanceledCheck canceledCheck, List<CanceledGood> canceledGoodList);
}
