package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.DiscountForEmployeesDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.DiscountForEmployees;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DiscountForEmployeesDaoImpl implements DiscountForEmployeesDao {

    //Получить скидочную карту по id
    @Override
    public DiscountForEmployees getDiscountCard(long numberCard) {


        DiscountForEmployees discountForEmployees = new DiscountForEmployees();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `discount_for_employees` WHERE is_a_live ='1' AND number_card = '"+ numberCard +"'");
                while (rs.next()) {
                    discountForEmployees.setId(rs.getInt(1));
                    discountForEmployees.setNumberCard(rs.getLong(2));
                    discountForEmployees.setName(rs.getString(3));
                    discountForEmployees.setBudgetForTheMonth(rs.getDouble(4));
                    discountForEmployees.setAmountOfDiscount(rs.getDouble(5));
                    discountForEmployees.setIs_a_live(rs.getBoolean(6));
                    discountForEmployees.setBalance(rs.getDouble(7));
                    discountForEmployees.setRole(rs.getInt(8));
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

        return discountForEmployees;
    }

    //Запись в бд данных о скидки
    @Override
    public boolean passed(Check check, DiscountForEmployees discountForEmployees) {
        //Флаг статуса выполнения открытие сессии
        boolean flag = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        Double balance = discountForEmployees.getBalance() + check.getAmountByPrice();

        try {
            if (connection != null) {
                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                //todo Костыль если тут 4744500 то я понимаю что это скидка из приложения и не обновляю в бд
                if(discountForEmployees.getBudgetForTheMonth() != 9720445.73){
                    stmt.execute("UPDATE discount_for_employees SET balance = "+balance+" WHERE id ='"+ discountForEmployees.getId() +"';");
                }

                //stmt.execute("UPDATE discount_for_employees SET balance = "+ balance +" WHERE 'id'='"+ discountForEmployees.getId()+"'");
                stmt.execute("INSERT INTO `discount_history` (check_id,employer_id) VALUES('"+ check.getId() +"', '"+ discountForEmployees.getId() +"')");
                stmt.close();
            }

            //Комит транзакции
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

        //Возвращаем статус открытия сессии
        return flag;
    }
}
