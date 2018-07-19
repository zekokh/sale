package ru.zekoh.properties;


import ru.atol.drivers10.fptr.IFptr;

import java.io.FileInputStream;
import java.io.IOException;

public class Properties {

    //Адрес для подключения к БД
    public static String HOST;

    //Логин для подключения к БД
    public static String LOGIN;

    //Пароль для подключения к БД
    public static String PASSWORD;

    //Печать на ККМ
    public static Boolean KKM;

    //Порт для подключения ККМ
    public static int PRINTER_PORT;

    // Драйвер принтера
    public static IFptr FPTR = null;

    //Инициализация данных из проперти файла
    public static void initData(){

        FileInputStream file;
        java.util.Properties property = new java.util.Properties();

        try {
            file = new FileInputStream("src/ru/zekoh/properties/config.properties");
            property.load(file);

            HOST = property.getProperty("db.host");
            LOGIN = property.getProperty("db.login");
            PASSWORD = property.getProperty("db.password");
            KKM = Boolean.valueOf(property.getProperty("kkm"));
            PRINTER_PORT = Integer.parseInt(property.getProperty("kkm.port"));

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    //Инициализация данных из проперти файла
    public static void initDataWithoutFile(){
        HOST = "jdbc:mysql://localhost:3306/sale?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        LOGIN = "root";
        PASSWORD = "heroin";
        KKM = true;
        PRINTER_PORT = 3;
    }
}
