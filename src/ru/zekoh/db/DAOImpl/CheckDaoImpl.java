package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Goods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckDaoImpl implements CheckDao {

    //Создаем чек
    @Override
    public Check createCheck() {

        //Создаем новый объект типа Чек
        Check check = new Check();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("INSERT INTO `CHECK` (is_a_live) VALUE (1)");
                ResultSet rs = stmt.executeQuery("SELECT * FROM `CHECK` ORDER BY id DESC LIMIT 1");
                while (rs.next()) {
                    check.setId(rs.getInt(1));
                    check.setContainGoods(rs.getBoolean(12));
                }
                stmt.close();
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
        return check;
    }

    //Добавить товар в чек
    @Override
    public boolean addGoodsToCheck(Check check, Goods goods) {
/*

        //Флаг статуса добавления нового товара в чек
        boolean addGoodsStatus = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                if(!check.isContainGoods()){
                    stmt.execute("INSERT INTO `CHECK` (date_of_creation, contain_goods) VALUE (NOW(), 1)");
                }
                stmt.execute("INSERT INTO `CHECK` (is_a_live) VALUE (1)");
                ResultSet rs = stmt.executeQuery("SELECT * FROM `CHECK` ORDER BY id DESC LIMIT 1");
                while (rs.next()) {
                    check.setId(rs.getInt(1));
                }
                stmt.close();
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

*/

        return false;
    }

    //Удалить товар из чека
    @Override
    public boolean deleteGoodsFromCheck(Goods goods) {
        return false;
    }

    //Удалить чек
    @Override
    public boolean deleteCheck(Check check) {
        return false;
    }
}
