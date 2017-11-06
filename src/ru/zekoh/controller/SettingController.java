package ru.zekoh.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.DAOImpl.SessionDaoImpl;
import ru.zekoh.db.DAOImpl.UserDaoImpl;
import ru.zekoh.db.entity.DailyReport;

import java.io.*;

public class SettingController {

    //Поле для ввода Хоста
    @FXML
    public TextField hostLabel;

    //Поля для ввода логина
    @FXML
    public TextField loginLabel;

    //Поле для ввода пароля
    @FXML
    public TextField passwordLabel;

    //Поля для ввода статуса ККМ
    @FXML
    public ChoiceBox kkmChoiceBox;

    //Лейбл для вывода ошибок
    @FXML
    public Label errorLabel;

    //Лейбл для ввода порта для подключения принтера
    @FXML
    public TextField portLabel;

    //Инициализация
    @FXML
    public void initialize() {

        kkmChoiceBox.setItems(FXCollections.observableArrayList(
                "Подключен", "Не подключен")
        );

        FileInputStream file;
        java.util.Properties property = new java.util.Properties();

        try {
            file = new FileInputStream("src/ru/zekoh/properties/config.properties");
            property.load(file);

            hostLabel.setText(property.getProperty("db.host"));
            loginLabel.setText(property.getProperty("db.login"));
            passwordLabel.setText(property.getProperty("db.password"));
            if (Boolean.valueOf(property.getProperty("kkm"))) {
                kkmChoiceBox.setValue("Подключен");
            } else {
                kkmChoiceBox.setValue("Не подключен");
            }
            portLabel.setText(property.getProperty("kkm.port"));


        } catch (IOException e) {
            errorLabel.setText("ОШИБКА: Файл свойств отсуствует!");
        }

        CheckDao checkDao = new CheckDaoImpl();
        DailyReport dailyReport12 = checkDao.soldPerDay("2017-10-12");
        System.out.println("12 октября");
        System.out.println("Доход: "+dailyReport12.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport12.getAmountCash());
        System.out.println("Карта: "+dailyReport12.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport13 = checkDao.soldPerDay("2017-10-13");
        System.out.println("13 октября");
        System.out.println("Доход: "+dailyReport13.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport13.getAmountCash());
        System.out.println("Карта: "+dailyReport13.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport14 = checkDao.soldPerDay("2017-10-14");
        System.out.println("14 октября");
        System.out.println("Доход: "+dailyReport14.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport14.getAmountCash());
        System.out.println("Карта: "+dailyReport14.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport15 = checkDao.soldPerDay("2017-10-15");
        System.out.println("15 октября");
        System.out.println("Доход: "+dailyReport15.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport15.getAmountCash());
        System.out.println("Карта: "+dailyReport15.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport16 = checkDao.soldPerDay("2017-10-16");
        System.out.println("16 октября");
        System.out.println("Доход: "+dailyReport16.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport16.getAmountCash());
        System.out.println("Карта: "+dailyReport16.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport17 = checkDao.soldPerDay("2017-10-17");
        System.out.println("17 октября");
        System.out.println("Доход: "+dailyReport17.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport17.getAmountCash());
        System.out.println("Карта: "+dailyReport17.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport18 = checkDao.soldPerDay("2017-10-18");
        System.out.println("18 октября");
        System.out.println("Доход: "+dailyReport18.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport18.getAmountCash());
        System.out.println("Карта: "+dailyReport18.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport19 = checkDao.soldPerDay("2017-10-19");
        System.out.println("19 октября");
        System.out.println("Доход: "+dailyReport19.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport19.getAmountCash());
        System.out.println("Карта: "+dailyReport19.getAmountCard());
        System.out.println("----------");
        System.out.println("");

        DailyReport dailyReport20 = checkDao.soldPerDay("2017-10-20");
        System.out.println("20 октября");
        System.out.println("Доход: "+dailyReport20.getSoldPerDay());
        System.out.println("Наличка: "+dailyReport20.getAmountCash());
        System.out.println("Карта: "+dailyReport20.getAmountCard());
        System.out.println("----------");
        System.out.println("");


    }

    //Сохранение изменений в проперти файле
    public void saveProperties(ActionEvent actionEvent) {

        OutputStream output = null;
        java.util.Properties property = new java.util.Properties();

        try {
            output = new FileOutputStream("src/ru/zekoh/properties/config.properties");
            property.setProperty("db.host", hostLabel.getText());
            property.setProperty("db.login", loginLabel.getText());
            property.setProperty("db.password", passwordLabel.getText());
            if((String) kkmChoiceBox.getValue() == "Подключен"){
                property.setProperty("kkm", "true");
            }else {
                property.setProperty("kkm", "false");
            }
            property.setProperty("kkm.port", portLabel.getText());

            property.store(output, null);

            errorLabel.setText("Изменения успешно сохранены!");
        } catch (IOException e) {
            errorLabel.setText("ОШИБКА: Попробуйте позже!");
        }

    }

    //Переход на экран аутентификации
    public void goToLoginWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/LoginWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e);

        }
    }

}
