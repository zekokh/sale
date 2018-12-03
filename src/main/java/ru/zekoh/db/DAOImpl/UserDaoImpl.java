package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoImpl implements UserDao {

    //Получить всех пользователей
    @Override
    public List<User> getAllUsers() {
        return null;
    }

    //Получить пользователя по его логину
    @Override
    public User getUserByLogin(String login) {
        Connection connection = DataBase.getConnection();
        if (connection != null) {
            try {
                User user = new User();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT user.id, user.name, user.login, user.mail, user.password, user.role_id, role.name FROM USER INNER JOIN ROLE ON user.role_id = role.id WHERE USER.login = '" + login + "';");
                while (rs.next()) {
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setLogin(rs.getString("login"));
                    user.setMail(rs.getString("mail"));
                    user.setPassword(rs.getString("password"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setRole(rs.getString(7));
                }
                connection.close();
                return user;
            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }

    //Получить пользователей по его id
    @Override
    public User getUserById(Long id) {
        User user = new User();
        Connection connection = DataBase.getConnection();
        if (connection != null) {
            try {

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT user.id, user.name, user.login, user.mail, user.password, user.role_id, role.name FROM USER INNER JOIN ROLE ON user.role_id = role.id WHERE USER.id = '" + id + "';");
                while (rs.next()) {
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setLogin(rs.getString("login"));
                    user.setMail(rs.getString("mail"));
                    user.setPassword(rs.getString("password"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setRole(rs.getString(7));
                }
                connection.close();
            } catch (SQLException e) {
                return null;
            }
        }
        return user;
    }

    //Получить пользователей по их статусу
    @Override
    public List<User> getUsersByLiveStatus(boolean status) {
        return null;
    }
}
