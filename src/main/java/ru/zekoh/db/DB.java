package ru.zekoh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    //Адрес для подключения к БД
    public static String HOST;

    //Логин для подключения к БД
    public static String LOGIN;

    //Пароль для подключения к БД
    public static String PASSWORD;

    public static Connection getLocalConnection() {
        HOST = "jdbc:mysql://localhost:3306/sale22122017?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        LOGIN = "root";
        PASSWORD = "heroin";

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

    public static Connection getRemoteConnection() {
        HOST = "jdbc:mysql://mysql.9884744500.myjino.ru/9884744500_jacqtest?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        LOGIN = "046271428_jtest";
        PASSWORD = "1234";

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
