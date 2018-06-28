package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.core.printing.KKM;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.core.synchronisation.SData;
import ru.zekoh.core.synchronisation.Synchronisation;
import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.DAOImpl.SessionDaoImpl;
import ru.zekoh.db.DAOImpl.UserDaoImpl;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.Session;
import ru.zekoh.properties.Properties;

public class MenuController {

    //Кнопка для перехда в окно продаж
    @FXML
    public Button saleBtn;

    //Кнопка для печати Z-Отчета
    @FXML
    public Button reportBtn;

    //Кнопка для блокировки и перехода на окно регистрации
    @FXML
    public Button blockBtn;

    //Лейбл для отображение информации о ошибках
    @FXML
    public Label errorLabel;

    // Кнопка тестирования ОФД
    @FXML
    public Button testOFD;

    //Инициализация
    @FXML
    public void initialize() {
        saleBtn.setDisable(false);
        reportBtn.setDisable(false);
        blockBtn.setDisable(false);
        testOFD.setDisable(false);

        /*if(SData.isInTheWork()){
            System.out.println("Идет синхронизация ...");
        } else {
            Synchronisation synchronisation = new Synchronisation();
            synchronisation.start();
        }*/

}

    //Переход в окно продаж
    public void goToSaleWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/SaleWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Переход на окно аутентификации
    public void goToLoginWindow(ActionEvent actionEvent) {

        //Создаем объект Session Dao для закрытие сессии
        SessionDao sessionDao = new SessionDaoImpl();

        //Получаем id текущего пользователя из сессии
        Long userId = sessionDao.getLastOpenSeesion().getUserId();

        //Создаем объект UserDao для поиска пользователя по id
        UserDao userDao = new UserDaoImpl();

        //Если сессия закрыта
        if (sessionDao.closeSession(userDao.getUserById(userId))) {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/LoginWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                System.out.println(e);
                errorLabel.setText("К сожалению что-то пошло не так! Я не могу выйти из системы.");
            }
        } else {
            errorLabel.setText("К сожалению что-то пошло не так! Я не могу закрыть сессию.");
        }
    }

    public void report(ActionEvent actionEvent) {

        KKMOFD.closeShift(Properties.FPTR);

/*        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();
            KKM.report(dailyReport);
        } catch (Exception e) {
            System.out.println("что то пошло не так с ккм");
            System.out.println(e);
        }*/
    }

    // Диагностика с ОФД
    public void testOfd(ActionEvent actionEvent) {
        KKMOFD.ofdTest(Properties.FPTR);
    }
}
