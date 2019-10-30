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

    // id пекарни
    // 1 - Майкоп, Первомайская, 193
    // 2 - Краснодар, Российская, 74
    // 3 - Майкоп, Восход, Шоссейная, 1В
    // 4 - Краснодар, Генерела Шифрина, 1
    // 5 - Нальчик, Московская, 6
    public static int bakaryId = 2;

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
    public static boolean ModalWhilePrintCheckBool = true;


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

    //Инициализация данных при запуске программы
    public static void initDataWithoutFile() {
        HOST = "jdbc:mysql://localhost:3306/center?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        LOGIN = "root";
        PASSWORD = "heroin";
        KKM = true;
        statusPrinted = true;

        switch (bakaryId) {
            case (1):
                initDataCenter();
                break;
            case (2):
                initDataRossiyskaya();
                break;
            case (3):
                initDataVoshod();
                break;
            case (4):
                initDataShifrina();
                break;
        }
    }

    //Инициализация данных для пекарни на Российская
    public static void initDataCenter() {
        comPort = "6";
        updateUrl = "https://center.jacq.ru";

    }

    //Инициализация данных для пекарни на Российская
    public static void initDataRossiyskaya() {
        comPort = "3";
        updateUrl = "https://krasnodar.jacq.ru";

    }

    //Инициализация данных для пекарни Восход
    public static void initDataVoshod() {
        comPort = "4";
        updateUrl = "https://voshod.jacq.ru";

    }

    //Инициализация данных для пекарни на Шифрина
    public static void initDataShifrina() {
        comPort = "5";
        updateUrl = "https://shifrina.jacq.ru";

    }


}
