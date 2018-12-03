package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.Goods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

    //Обновляем данные в чеке
    @Override
    public boolean updateCheck(Check check) {
        boolean flag = false;

        List<Goods> goods = check.getGoodsList();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        //Статус оплаты
        int payment_state = check.isPaymentState() ? 1 : 0;

        //
        int discount_on_goods = check.isDiscountOnGoods() ? 1 : 0;

        int discount_on_check = check.isDiscountOnCheck() ? 1 : 0;

        int return_status = check.isReturnStatus() ? 1 : 0;

        int is_a_live = check.isALive() ? 1 : 0;

        int contain_goods = check.isContainGoods() ? 1 : 0;

        Double withBonus = check.getAmountPaidBonuses();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("UPDATE `check` SET `amount_by_price` = '" + check.getAmountByPrice() + "', `total` = '" + check.getTotal() + "', `payment_state`='" + payment_state + "', `discount_on_goods`='" + discount_on_goods + "', `discount_on_check`='" + discount_on_check + "', `type_of_payment`='" + check.getTypeOfPayment() + "', `date_of_creation`='" + check.getDateOfCreation() + "', `date_of_closing`='" + check.getDateOfClosing() + "', `date_of_closing_unix`='" + check.getDateOfClosingUnix() + "', `return_status`='" + return_status + "',`is_a_live`='" + is_a_live + "',`pay_with_bonus` = '" + withBonus + "',`contain_goods`='" + contain_goods + "' WHERE `id`='" + check.getId() + "';");
                for (int i = 0; i < goods.size(); i++) {
                    stmt.execute("INSERT INTO `goods` (`check_id`, `general_id`, `product_id`, `product_name`, `price_from_the_price_list`, `price_after_discount`, `selling_price`, `quantity`) VALUE ('" + check.getId() + "', '" + goods.get(i).getGeneralId() + "','" + goods.get(i).getProductId() + "', '" + goods.get(i).getProductName() + "', '" + goods.get(i).getPriceFromThePriceList() + "','" + goods.get(i).getPriceAfterDiscount() + "', '" + goods.get(i).getSellingPrice() + "', '" + goods.get(i).getCount() + "')");
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();
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

    @Override
    public DailyReport soldPerDay() {

        DailyReport dailyReport = new DailyReport();

        //Сумма за день
        Double soldPerDay = 0.0;

        //Количество чеков за день
        int numberOfChecks = 0;

        //Возвратов на сумму
        Double returnPerDay = 0.0;

        //Сумма наличкой
        Double amountCash = 0.0;

        //Сумма картой
        Double amountCard = 0.0;

        //Текущий день
        String today = "" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + "";

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `CHECK` WHERE `is_a_live` = '0' AND `return_status` = '0' AND `payment_state`='1' AND `date_of_creation` LIKE '%" + today + "%'");
                while (rs.next()) {
                    numberOfChecks++;
                    soldPerDay = soldPerDay + rs.getDouble(3);

                    if (rs.getInt(7) == 1) {
                        amountCash = amountCash + rs.getDouble(3);
                    } else {
                        amountCard = amountCard + rs.getDouble(3);
                    }
                }


                ResultSet rsReturn = stmt.executeQuery("SELECT * FROM `CHECK` WHERE `is_a_live` = '0' AND `return_status` = '1' AND `payment_state`='1' AND `date_of_creation` LIKE '%" + today + "%'");
                while (rsReturn.next()) {
                    returnPerDay = returnPerDay + rsReturn.getDouble(3);
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();

            dailyReport.setAmountCard(amountCard);
            dailyReport.setAmountCash(amountCash);
            dailyReport.setNumberOfChecks(numberOfChecks);
            dailyReport.setReturnPerDay(returnPerDay);
            dailyReport.setSoldPerDay(soldPerDay);

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
        return dailyReport;
    }

    @Override
    public DailyReport soldPerDay(String date) {

        DailyReport dailyReport = new DailyReport();

        //Сумма за день
        Double soldPerDay = 0.0;

        //Количество чеков за день
        int numberOfChecks = 0;

        //Возвратов на сумму
        Double returnPerDay = 0.0;

        //Сумма наличкой
        Double amountCash = 0.0;

        //Сумма картой
        Double amountCard = 0.0;

        //Текущий день
        String today = date;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `CHECK` WHERE `is_a_live` = '0' AND `return_status` = '0' AND `payment_state`='1' AND `date_of_creation` LIKE '%" + today + "%'");
                while (rs.next()) {
                    numberOfChecks++;
                    soldPerDay = soldPerDay + rs.getDouble(3);

                    if (rs.getInt(7) == 1) {
                        amountCash = amountCash + rs.getDouble(3);
                    } else {
                        amountCard = amountCard + rs.getDouble(3);
                    }
                }


                ResultSet rsReturn = stmt.executeQuery("SELECT * FROM `CHECK` WHERE `is_a_live` = '0' AND `return_status` = '1' AND `payment_state`='1' AND `date_of_creation` LIKE '%" + today + "%'");
                while (rsReturn.next()) {
                    returnPerDay = returnPerDay + rsReturn.getDouble(3);
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();

            dailyReport.setAmountCard(amountCard);
            dailyReport.setAmountCash(amountCash);
            dailyReport.setNumberOfChecks(numberOfChecks);
            dailyReport.setReturnPerDay(returnPerDay);
            dailyReport.setSoldPerDay(soldPerDay);

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
        return dailyReport;
    }

    @Override
    public int numberOfChecks() {
        return 0;
    }

    @Override
    public Double returnPerDay() {
        return null;
    }

    //Добавить товар в чек
    @Override
    public boolean addGoodsToCheck(Check check, Goods goods) {
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
