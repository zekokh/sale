package ru.zekoh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.zekoh.properties.Properties.*;

public class DataBase {
    public static Connection getConnection() {


        Connection dbConnection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден");
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(HOST, LOGIN, PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println("Драйверменеджер не запущен");
            System.out.println(e.getMessage());
        }

        return dbConnection;
    }
}
