package ru.zekoh.core.synchronisation;

import ru.zekoh.db.DB;
import ru.zekoh.db.entity.LastSynchronization;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ru.zekoh.core.synchronisation.SData.flag;
import static ru.zekoh.core.synchronisation.SData.inTheWork;

public class Synchronisation extends Thread {
/*
    public void run() {

*//*        while (flag) {
            System.out.println("Поток запущен...");

            synchro();

            try {
                System.out.println("остановка на 2 минуты");
                Thread.sleep(600000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //Окончательная выгрузка данных
        synchro();*//*
        inTheWork = false;

    }

    public static boolean synchro() {
        inTheWork = true;
        boolean successFlag = false;

        System.out.println("Начало транзакций");
        Connection localConnection = DB.getLocalConnection();
        Connection remoteConnection = DB.getRemoteConnection();
        LastSynchronization lastSynchronization = new LastSynchronization();

        if (localConnection != null) {

            try {
                Statement stmt = localConnection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `synchronization` WHERE `status` = '1';");
                while (rs.next()) {
                    lastSynchronization.setDate(rs.getString(2));
                }
                System.out.println("Чек последней удачной операция синхронизации был закрыт в: " + lastSynchronization.getDate());
            } catch (Exception e) {
                System.out.println("Ошибка при извлечении последней удачной операции!");
                System.out.println(e);
            }

            if (lastSynchronization.getDate() != null) {
                try {
                    Statement stmt = localConnection.createStatement();
                    localConnection.setAutoCommit(false);
                    remoteConnection.setAutoCommit(false);
                    ResultSet rs = stmt.executeQuery("SELECT * FROM `check` WHERE `is_a_live` = '0' AND `payment_state` = '1' AND `date_of_closing` BETWEEN '" + lastSynchronization.getDate() + "' AND '" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()) + "';");
                    Statement stmtRemote = remoteConnection.createStatement();
                    while (rs.next()) {
                        if (!rs.getString(9).equals(lastSynchronization.getDate())) {

                            try {
                                System.out.println("Внесение записи на сервер: " + rs.getInt(1));
                                stmtRemote.execute("INSERT INTO `CHECK` (id, " +
                                        "amount_by_price, " +
                                        "total, " +
                                        "payment_state, " +
                                        "discount_on_goods," +
                                        "discount_on_check," +
                                        "type_of_payment, " +
                                        "date_of_creation, " +
                                        "date_of_closing, " +
                                        "return_status, " +
                                        "is_a_live, " +
                                        "contain_goods) " +
                                        "VALUE ('" + rs.getInt(1) + "', " +
                                        "'" + rs.getDouble(2) + "'," +
                                        "'" + rs.getDouble(3) + "'," +
                                        "'" + rs.getInt(4) + "', " +
                                        "'" + rs.getInt(5) + "', " +
                                        "'" + rs.getInt(6) + "'," +
                                        "'" + rs.getInt(7) + "'," +
                                        "'" + rs.getString(8) + "'," +
                                        "'" + rs.getString(9) + "'," +
                                        "'" + rs.getInt(10) + "'," +
                                        "'" + rs.getInt(11) + "'," +
                                        "'" + rs.getInt(12) + "')");
                            } catch (Exception e) {
                                System.out.println("Пропустить запись с id: " + rs.getInt(1));
                                System.out.println(e);
                            }


                            if (rs.isLast()) {
                                System.out.println("Внесение записи в локальную БД о последней операции синхронизации!");
                                Connection con = DB.getLocalConnection();
                                Statement stmtToLocalSynch = con.createStatement();
                                stmtToLocalSynch.execute("INSERT INTO `synchronization` (date_close_last_check, status) VALUE ('" + rs.getString(9) + "', 1)");
                                con.close();
                            }

                        } else {
                            System.out.println("Запись была добавлена исходя из даты закрытия чека! " + rs.getInt(1));
                        }
                    }

                    localConnection.commit();
                    remoteConnection.commit();
                    localConnection.close();
                    remoteConnection.close();

                } catch (SQLException e) {
                    System.out.println("Ошшибка в коннектах!");
                    System.out.println(e);
                }
            } else {
                System.out.println("дата закрытия чека последней операции пустая!");
            }


        } else {
            System.out.println("Нет подключения к локальной БД!");
        }


        inTheWork = false;

        return successFlag;
    }*/
}

