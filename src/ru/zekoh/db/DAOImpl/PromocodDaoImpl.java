package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.ProductDao;
import ru.zekoh.db.DAO.PromocodDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.DiscountForEmployees;
import ru.zekoh.db.entity.Product;
import ru.zekoh.db.entity.Promocod;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromocodDaoImpl implements PromocodDao {

    @Override
    public Promocod getPromocodByNumber(int number) {

        Promocod promocod = new Promocod();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `promotion` WHERE `is_a_live` ='1' AND `number` = '" + number + "'");
                while (rs.next()) {
                    promocod.setNumber(rs.getInt(1));
                    promocod.setUse(rs.getBoolean(2));
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
        return promocod;
    }

    @Override
    public boolean use(Promocod promocod) {
        //Флаг статуса выполнения закрытия сессии
        boolean flag = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();
                stmt.execute("UPDATE `promotion` SET `date`=NOW(), `use`='1' WHERE `number` = '" + promocod.getNumber() + "';");
                stmt.close();
            }

            connection.commit();

            //Меняем статус на положительный
            flag = true;
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

    //Выбрать промокод для печати на принтере
    @Override
    public Promocod getPromocodForPrint() {

        Promocod promocod = new Promocod();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `promotion` WHERE `is_a_live` ='1' AND `use`= '0' AND `printed` = '0' LIMIT 1");
                while (rs.next()) {
                    promocod.setNumber(rs.getInt(1));
                    promocod.setUse(rs.getBoolean(2));
                }

                stmt.execute("UPDATE `promotion` SET `printed`='1' WHERE `number` = '" +  promocod.getNumber() + "';");
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
        return promocod;
    }

    //Проверка промокода
    @Override
    public boolean checkPromoFromBild(int promocod) {

        boolean flag = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `promotion` WHERE `is_a_live` ='1' AND `printed`= '1' AND `number`='" + promocod + "'");
                while (rs.next()) {
                    flag = !rs.getBoolean(2);
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

        return flag;
    }
}
