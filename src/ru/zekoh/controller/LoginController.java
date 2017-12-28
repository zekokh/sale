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

public class LoginController {

    //Поле для ввода логина
    @FXML
    public TextField loginInput;

    //Поле для ввода пароля
    @FXML
    public PasswordField passwordInput;

    //Информационный лейбл
    @FXML
    public Label infoLabel;

    @FXML
    public Pane pane;

    @FXML
    public VBox vbox;


    @FXML
    public void initialize() {

        //Создаем объект SessionDao для работы с сессией
        SessionDao sessionDao = new SessionDaoImpl();

        //Если есть открытые сессии значит система была выключена аварийно
        if (sessionDao.getAllOpenSessions().size() > 0) {


            //Оповещаем о некорректном завершении сеанса пользователя
            infoLabel.setText("Прошлый сеанс работы завершился неккоректно!");

            //todo пишем в лог о неккоректном завершении работы

            //Закрываем все сессии и пишем в лог о ошибки
            sessionDao.closeAllOpenSessions();

            if(SData.exitBool){
                Stage stage = (Stage) infoLabel.getScene().getWindow();
                stage.close();
            }
        }

    }

    public void exit(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

/*        //Флаг для остановки синхронизации
        SData.flag = false;

        if (!SData.isInTheWork()){
           // Synchronisation.synchro();
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/ExitLoader.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e);
        }*/

    }

    public void enter(ActionEvent actionEvent) throws SQLException, NoSuchAlgorithmException {

        //Аутентификация пользователя
        boolean checkStatus = login();

        //Если пользователь найден в системе и пароль совпадает то переходим на окно меню.
        if (checkStatus) {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/MenuWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                System.out.println(e);
                infoLabel.setText("Ну удалось построить окно меню! Попробуйте позже.");
            }
        }
    }

    //Метод аутентификации пользователя в системе
    public boolean login() throws NoSuchAlgorithmException, SQLException {

        //Флаг статуса аутентификации пользователя в системе
        boolean checkStatus = false;

        //Если поля с Логином и Паролем не пустые то проверить в Базе Данных пользователя с таким логином
        if (loginInput.getText().length() > 0 && passwordInput.getText().length() > 0) {

            //Логин пользователя
            String login = loginInput.getText();

            //Пароль пользователя
            String password = passwordInput.getText();

            //Условия проверки на содержания кирилици
            Pattern pattern = Pattern.compile(
                    "[" +                   //начало списка допустимых символов
                            "а-яА-ЯёЁ" +    //буквы русского алфавита
                            "\\d" +         //цифры
                            "\\s" +         //знаки-разделители (пробел, табуляция и т.д.)
                            "\\p{Punct}" +  //знаки пунктуации
                            "]" +                   //конец списка допустимых символов
                            "*");                   //допускается наличие указанных символов в любом количестве

            //Проверка на наличии кирилических знаков в поле логина
            Matcher checkForCyrillicContent = pattern.matcher(login);

            //Если кирилических символов нет, то продолжаем аутентификацию
            if (!checkForCyrillicContent.matches()) {

                //Создаем объект UserDao для поиска пользователя с таким логином в Базе Данных
                UserDao userDao = new UserDaoImpl();
                User user = userDao.getUserByLogin(login);

                //Если пользователь с таким логином найден в Базе Данных
                if (user != null) {
                    //Сверяем пароль пользователя (игнорируется размер букв) и если совпадает создаем новую сессию (пользовательский сеанс)
                    try {
                        if (user.getPassword().equalsIgnoreCase(password)) {

                            SessionDao sessionDao = new SessionDaoImpl();

                            //Создаем новую сессию, если созданно успешно возвращается true
                            checkStatus = sessionDao.createSession(user);


                        } else {
                            infoLabel.setText("Пароль не верный!");
                        }
                    } catch (Exception e) {
                        infoLabel.setText("Пароль не верный!");
                    }


                } else {
                    infoLabel.setText("Пользователь не найден!");
                }
            } else {
                infoLabel.setText("Логин не содержит кирилицу!");
            }


            //todo шифровать пароль
            //md5
            /*String hash = "35454B055CC325EA1AF2126E27707052";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();

            myHash.equals(hash);*/
        } else {
            infoLabel.setText("Заполните все поля!");
        }
        return checkStatus;
    }

    //Переход на окно настроек
    public void goToSettingWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/SettingWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e);
            infoLabel.setText("Ну удалось построить окно настроек! Попробуйте позже.");
        }
    }
}
