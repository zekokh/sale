package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.db.entity.UserEntity;
import ru.zekoh.properties.Properties;

import java.io.IOException;
import java.util.List;

public class PasswordEntryToAccessTheFolderController {

    @FXML
    Label labelInfo;

    @FXML
    Label labelPassword;

    String password = "";
    String hidePassword = "";

    List<UserEntity> users = null;

    @FXML
    void initialize() {
        //todo проверка на подключение к БД

        users = Properties.users;
        labelInfo.setText("Введите пароль для доступа к папке!");

    }

    public void pushOnKey_1(ActionEvent actionEvent) {
        typeChar("1");
    }

    public void pushOnKey_2(ActionEvent actionEvent) {
        typeChar("2");
    }

    public void pushOnKey_3(ActionEvent actionEvent) {
        typeChar("3");
    }

    public void pushOnKey_4(ActionEvent actionEvent) {
        typeChar("4");
    }

    public void pushOnKey_5(ActionEvent actionEvent) {
        typeChar("5");
    }

    public void pushOnKey_6(ActionEvent actionEvent) {
        typeChar("6");
    }

    public void pushOnKey_7(ActionEvent actionEvent) {
        typeChar("7");
    }

    public void pushOnKey_8(ActionEvent actionEvent) {
        typeChar("8");
    }

    public void pushOnKey_9(ActionEvent actionEvent) {
        typeChar("9");
    }

    public void pushOnKey_0(ActionEvent actionEvent) {
        typeChar("0");
    }

    private void typeChar(String symbol) {
        password += symbol;
        hidePassword = "";

        for (int i = 0; i < password.length(); i++) {
            hidePassword += "*";
        }

        labelPassword.setText(hidePassword);

        if (password.length() == 4) {
            labelInfo.setText("");

            for (int i = 0; i < users.size(); i++) {
                if (password.equals(users.get(i).getShortPassword())){

                    goToMenu();
                }
            }

            labelInfo.setText("Нет такого пользователя!");

            password = "";
            hidePassword = "";
            labelPassword.setText("");
        }
    }

    public void removeChar() {
        if (password.length() > 0) {
            password = removeLastChar(password);
            hidePassword = "";
        }

        for (int i = 0; i < password.length(); i++) {
            hidePassword += "*";
        }

        labelPassword.setText(hidePassword);
    }

    private String removeLastChar(String password) {
        return password.substring(0, password.length() - 1);
    }

    public void goToMenu() {

        // Статус поменять

        Properties.correctPasswordEntryToAccessTheFolder = true;
        Stage stage = (Stage) labelInfo.getScene().getWindow();
        stage.close();

    }


    public void exit(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
