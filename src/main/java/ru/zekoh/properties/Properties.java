package ru.zekoh.properties;


import org.hibernate.SessionFactory;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.core.KKTError;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Properties {

    //Адрес для подключения к БД
    public static String HOST;

    //Логин для подключения к БД
    public static String LOGIN;

    //Пароль для подключения к БД
    public static String PASSWORD;

    //Печать на ККМ
    public static Boolean KKM;

    // Драйвер принтера
    public static IFptr FPTR = null;

    // Пользователи системы
    public static List<UserEntity> users = null;

    // Текущий пользователь системы
    public static UserEntity currentUser = null;

    // Список продуктов и товаров
    public static List<Product> products = null;

    public static CheckObject checkObject = null;

    public static boolean statusPrinted = false;


    // Ошибка с ККТ
    public static KKTError kktError = null;
    public static int KKTErrorInfoAction = 0;
    public  static boolean ModalWhilePrintCheckBool = true;


    public static boolean isPayCard = false;

    public static SessionFactory sessionFactory = null;

    public static boolean cancelModalView = false;

    public static boolean returnStatusPassword = false;

    public static boolean isSmall = false;

    public static boolean updateDataFromServer = false;

    public static int heght = 0;

    public static int width = 0;

    public static int modalHeight = 0;

    public static int modalWidth = 0;

    public static boolean correctPasswordEntryToAccessTheFolder = false;

    public static String comPort = "4";

    public static int bakaryId = 3;

    public static String updateUrl = "";




    //Инициализация данных из проперти файла
    public static void initData() {

        FileInputStream file;
        java.util.Properties property = new java.util.Properties();

        try {
            file = new FileInputStream("src/ru/zekoh/properties/config.properties");
            property.load(file);

            HOST = property.getProperty("db.host");
            LOGIN = property.getProperty("db.login");
            PASSWORD = property.getProperty("db.password");
            KKM = Boolean.valueOf(property.getProperty("kkm"));
            comPort = String.valueOf(Integer.parseInt(property.getProperty("kkm.port")));

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    //Инициализация данных из проперти файла
    public static void initDataWithoutFile() {
        HOST = "jdbc:mysql://localhost:3306/center?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        LOGIN = "root";
        PASSWORD = "heroin";
        KKM = true;
        statusPrinted = true;
    }

    //Инициализация данных для пекарни Восход
    public static void initDataVoshod() {
        initDataWithoutFile();

        comPort = "4";
        updateUrl = "https://voshod.jacq.ru";

    }

    //Инициализация данных для пекарни на Шифрина
    public static void initDataShifrina() {
        initDataWithoutFile();
        comPort = "5";
        updateUrl = "";

    }

    // Майкоп Первомайская 193 актуальный порт
    //comPort = "6";

    // Краснодар
    // comPort = "3";

    // Майкоп Первомайская 193
    //comPort = "7";

    // Генерала Шифрина 1
    //comPort = "5";
}
