package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.SellReportDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.SellReport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SellReportDaoImpl implements SellReportDao {

    @Override
    public List<SellReport> findAll() {

        List<SellReport> sellReports = new ArrayList<SellReport>();

        //Текущий день
        //String today = "" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + "";

        String today = "2018-08-11";

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `product` WHERE `folder` = 0 AND live = 1");
                while (rs.next()) {

                    System.out.println("rs: " + rs.getString(1));
                    SellReport sellReport = new SellReport();
                    sellReport.setId(rs.getLong(1));
                    sellReport.setName(rs.getString(2));
                    sellReports.add(sellReport);
                }

                for (SellReport sell : sellReports) {
                    ResultSet count = stmt.executeQuery("SELECT COUNT(`goods`.`selling_price`) FROM `goods` INNER JOIN `check` ON `goods`.`check_id` = `check`.`id` WHERE `goods`.`product_id` = " + sell.getId() + " AND `check`.`payment_state` = 1 AND `check`.`return_status` = 0 AND `check`.`date_of_closing` LIKE '%" + today + "%'");
                    int count_var = 0;
                    while (count.next()) {
                        count_var = count.getInt(1);
                        if (count_var > 0) {
                            sell.setCount(count_var);
                        } else {
                            //sellReports.remove(sell);
                        }
                    }
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
        return sellReports;
    }
}
