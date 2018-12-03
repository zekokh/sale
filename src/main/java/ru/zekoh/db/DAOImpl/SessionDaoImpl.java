package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Session;
import ru.zekoh.db.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SessionDaoImpl implements SessionDao {

    //Создать сессию
    @Override
    public boolean createSession(User user) {

        //Флаг статуса выполнения открытие сессии
        boolean createSessionStatus = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("INSERT INTO SESSION (user_id, name, role_id, role) VALUES('" + user.getId() + "', '" + user.getName() + "', '" + user.getRoleId() + "','" + user.getRole() + "')");
                stmt.close();
            }

            //Комит транзакции
            connection.commit();

            //Меняем статус на положительный
            createSessionStatus = true;
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
        return createSessionStatus;
    }

    //Закрыть сесссию
    @Override
    public boolean closeSession(User user) {

        //Флаг статуса выполнения закрытия сессии
        boolean closeSessionStatus = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();
                stmt.execute("UPDATE SESSION SET date_and_time_of_session_closing=NOW(), is_a_live='0' WHERE user_id = '" + user.getId() + "';");
                stmt.close();
            }

            connection.commit();

            //Меняем статус на положительный
            closeSessionStatus = true;
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
        return closeSessionStatus;
    }

    //Получить вче открытые сессии
    @Override
    public List<Session> getAllOpenSessions() {

        //Создаем Список сессий
        ArrayList<Session> sessionArrayList = new ArrayList<Session>();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        if (connection != null) {
            try {
                Session session = new Session();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `SESSION` WHERE is_a_live = '1';");
                while (rs.next()) {
                    session.setId(rs.getLong(1));
                    session.setUserId(rs.getLong(2));
                    session.setName(rs.getString(3));
                    session.setRoleId(rs.getLong(4));
                    session.setRole(rs.getString(5));
                    session.setDateAndTimeOfSessionCreation(rs.getString(5));
                    session.setDateAndTimeOfSessionClosing(rs.getString(6));
                    session.setALive(rs.getBoolean(7));

                    //Добавляем сессию в список сессий
                    sessionArrayList.add(session);
                }

                //Закрываем соединение с Базой Данных
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        //Возвращаем список сессий
        return sessionArrayList;
    }

    //Завершить все открытые сессии
    @Override
    public boolean closeAllOpenSessions() {

        //Флаг статуса выполнения закрытия сессии
        boolean closeSessionStatus = false;

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                Statement stmt = connection.createStatement();
                stmt.execute("UPDATE SESSION SET date_and_time_of_session_closing=NOW(), is_a_live='0' WHERE is_a_live='1';");
                stmt.close();
            }

            connection.commit();

            //Меняем статус на положительный
            closeSessionStatus = true;
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
        return closeSessionStatus;
    }

    //Получить последнии открытый сеанс
    @Override
    public Session getLastOpenSeesion() {
       SessionDao sessionDao = new SessionDaoImpl();
       List<Session> temp = sessionDao.getAllOpenSessions();

        //todo может быть что пользователь не последний и их несколько обработать и брать последнего
        return  sessionDao.getAllOpenSessions().get(0);
    }
}
