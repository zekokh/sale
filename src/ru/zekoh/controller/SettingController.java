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
import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DAOImpl.SessionDaoImpl;
import ru.zekoh.db.DAOImpl.UserDaoImpl;

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
