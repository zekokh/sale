package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.PresentDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.PresentEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PresentDaoImpl implements PresentDao {
    @Override
    public boolean save(Long presentId, long customerId, int typeId, String typeName, String description, int dateLimit, int dateUse, String value, boolean synchronise) {
        boolean flag = false;
        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("INSERT INTO `PRESENTS` (present_id, customer_id, type_id, type_name, description, date_limit, date_use, value, synchronise) VALUE (" + presentId + "," + customerId + ", " + typeId + "," + typeName + "," + description + "," + dateLimit + ", " + dateUse + ", " + value + ", " + synchronise + ")");
                stmt.close();
                flag = true;
            }

            //Комит транзакции
            connection.commit();

        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return flag;
    }

    @Override
    public PresentEntity getPresentEntity(Long presentId) {
        return null;
    }
}
