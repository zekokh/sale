package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.CanceledCheckDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.CanceledCheck;
import ru.zekoh.db.entity.CanceledGood;

import java.sql.Connection;
import java.util.List;

public class CanceledCheckDaoImpl implements CanceledCheckDao {
    @Override
    public boolean saveCheck(CanceledCheck canceledCheck, List<CanceledGood> canceledGoodList) {
        // Открыть сессию и начать транзакцию
        Connection connection = DataBase.getConnection();
        // Добавить чек

        // Добавить товары указанные в чеке


        return false;
    }
}
