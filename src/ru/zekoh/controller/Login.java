package ru.zekoh.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.zekoh.core.synchronisation.SData;
import ru.zekoh.core.synchronisation.Synchronisation;
import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DAOImpl.SessionDaoImpl;
import ru.zekoh.db.DAOImpl.UserDaoImpl;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Session;
import ru.zekoh.db.entity.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {


    @FXML
    Label labelInfo;

    @FXML
    Label labelPassword;

    String password = "";
    String hidePassword = "";

    @FXML
    void initialize() {

    }

    public void exit(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();


    }

    public void pushOnKey_1(ActionEvent actionEvent){
        typeChar("1");
    }

    private void typeChar(String symbol) {
        password += symbol;
        hidePassword = "";

        for(int i = 0; i < password.length(); i++) {
            hidePassword += "*";
        }

        labelPassword.setText(hidePassword);

        if (password.length() == 4) {
            labelInfo.setText("");

            if (password == "1234"){
                labelInfo.setText("Добро пожаловать!");
            }else {
                labelInfo.setText("Нет такого пользователя!");
            }

            password = "";
            hidePassword = "";
            labelPassword.setText("");
        }
    }

    public void enter(ActionEvent actionEvent) throws SQLException, NoSuchAlgorithmException {

        //Если пользователь найден в системе и пароль совпадает то переходим на окно меню.

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/MenuWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                System.out.println(e.getMessage().toString());

            }

    }
}
