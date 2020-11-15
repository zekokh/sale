package ru.zekoh.properties;


import org.hibernate.SessionFactory;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.core.KKTError;
import ru.zekoh.core.loyalty.StoreCard;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.*;
import ru.zekoh.subtotal.Subtotal;

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
    // 6 - Майкоп, Пролетарская, 449
    // 7 - 2-я касса на Краснодар, Российская, 74
    public static int bakaryId = 6;

    // Путь до шрифта
    public static String fontPath = "";

    // Адрес системы лояльности
    public static String loyaltyUrl = "";

    // Адрес обновление карты в системе лояльности
    public static String loyaltyUrlUpdate = "";

    // Сообщение при обновлении
    public static String loyaltyUrlUpdateMsg = "";

    // Путь до файлов fxml
    public static String pathToFXML = "small";

    //Адрес для подключения к БД
    public static String HOST;

    //Логин для подключения к БД
    public static String LOGIN;

    //Пароль для подключения к БД
    public static String PASSWORD;

    //Печать на ККМ
    public static boolean KKM;

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

    // Размер шрифта
    public static Double fontFolderAndProduct = 17.5;

    //Количество папок и продуктов в строке
    public static int countFolderAndProductInRow = 5;

    //Ширина кнопок с папками и продуктами
    public static int btnWigth = 165;

    //Высота кнопок с папками и продуктами
    public static int btnHight = 98;

    // id запароленной папки
    public static int protectedFolder = 370;

    // Переменная для передачи карты клиента через модальное окно
    public static StoreCard modalStoreCard = null;

    public static String modalNumberCard = null;

    // id багета для того что бы оплатить покупку бонусами
    public static int bagetId = 0;

    // Электронный адрес от учетной записи subtotal
    public static String subtotalMail;

    // Пароль от учетной записи subtotal
    public static String subtotalPassword;

    // Глобальный объект для взаимодействия с серверами subtotal
    public static Subtotal subtotal = null;

    // Текст для модальных окон
    public static String infoModalText;

    // Автоматическая печать на электронный теримнал
    public  static boolean ELECTRONIC_PAY_IN_INTEGRATION_TERMINAL = false;

    public  static boolean checkPayCardWithSberPOS = false;
    public static String textFromFileP = "";


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
        KKM = false;

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
            case (5):
                initDataNalchik();
                break;
            case (6):
                initDataPhenix();
                break;
            case (7):
                initDataRossiyskayaSecondCashPoint();
                break;
        }
    }

    // Инициализация данных для пекарни на Майкоп, Пролетарская, 449
    public static void initDataCenter() {
        comPort = "6";
        updateUrl = "https://center.jacq.ru";
        pathToFXML = "center";
        fontFolderAndProduct = 16.0;
        countFolderAndProductInRow = 4;
        btnWigth = 145;
        btnHight = 90;
        protectedFolder = 370;
        fontPath = "C:\\Users\\User\\Desktop\\sell\\fonts\\Exo.ttf";
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
    }

    // Инициализация данных для пекарни на Российская
    public static void initDataRossiyskaya() {
        comPort = "4";
        updateUrl = "https://krasnodar.jacq.ru";
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
        bagetId = 133;
    }

    // Инициализация данных для пекарни на Российская
    public static void initDataRossiyskayaSecondCashPoint() {
        comPort = "6";
        updateUrl = "https://krasnodar.jacq.ru";
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
        bagetId = 133;
        // loyaltyUrl = "http://localhost:3000/customer/search/";
    }

    // Инициализация данных для пекарни Восход
    public static void initDataVoshod() {
        comPort = "4";
        //comPort = "5";
        updateUrl = "https://voshod.jacq.ru";
        protectedFolder = 402;
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
    }

    // Инициализация данных для пекарни на Шифрина
    public static void initDataShifrina() {
        comPort = "5";
        updateUrl = "https://shifrina.jacq.ru";
        fontPath = "C:\\Users\\ен\\Desktop\\sell\\fonts\\Exo.ttf";
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
        bagetId = 133;
    }

    // Инициализация данных для пекарни в Нальчике
    public static void initDataNalchik() {
        comPort = "8";
        updateUrl = "https://nalchik.jacq.ru";
        fontPath = "C:\\Users\\ен\\Desktop\\sell\\fonts\\Exo.ttf";
        //loyaltyUrl = "https://loyalty.jacq.ru/customer/search/";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
    }

    // Инициализация данных для пекарни на Фениксе
    public static void initDataPhenix() {
        comPort = "3";
        updateUrl = "https://phenix.jacq.ru";
        fontPath = "C:\\Users\\a\\Desktop\\sell\\fonts\\Exo.ttf";
        loyaltyUrl = "https://club.jacques-andre.ru/customer/search/";
        loyaltyUrlUpdate = "https://club.jacques-andre.ru/card/update/";
    }
}
