package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.db.HibernateSessionFactory;

import ru.zekoh.db.entity.UserEntity;
import ru.zekoh.properties.Properties;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.List;

public class Login {
    private static final Logger logger = LogManager.getLogger(Login.class);

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

        if(Properties.FPTR != null){
            if(Properties.FPTR.isOpened()){
                Properties.FPTR.close();
                Properties.FPTR = null;
            }
        }

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
                   Properties.currentUser = users.get(i);
                   KKMOFD.name = Properties.currentUser.getName();
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

        //Если пользователь найден в системе и пароль совпадает то переходим на окно меню.
        Stage stage = (Stage) labelInfo.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/MenuWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public void exit(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        System.exit(0);
    }
}
