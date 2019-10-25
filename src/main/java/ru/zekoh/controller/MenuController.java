package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.core.UpdateFoldersAndProducts;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.properties.Properties;

import java.io.IOException;

import static ru.zekoh.core.Сatalog.generate;

public class MenuController {

    private static final Logger logger = LogManager.getLogger(MenuController.class);

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

    // Кнопка суточный отчет
    @FXML
    public Button xReport;

    private static String[] columns = {"Наименование", "Кол-во"};

    // Кнопка обновления данных
    @FXML
    public Button update;

    // Кнопка возвратов
    @FXML
    public Button returnBtn;

    //Инициализация
    @FXML
    public void initialize() {

        System.out.println(Properties.currentUser.getName());

        saleBtn.setDisable(false);
        reportBtn.setDisable(false);
        blockBtn.setDisable(false);
        update.setDisable(false);
        xReport.setDisable(false);
        returnBtn.setDisable(false);


        if (Properties.FPTR == null) {
            try {
               //Properties.FPTR = KKMOFD.create();
             KKMOFD.initDriver();
            } catch (Exception e) {
                System.out.println("Не удалось создать объект драйвера ККТ!" + e.getMessage());
                logger.error("Не удалось создать объект драйвера ККТ!" + e.getMessage());
            }
        }
    }

    //Переход в окно продаж
    public void goToSaleWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/SaleSmallWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
          logger.error("Не удалось построить интерфейс экрана продаж! \n"+e.toString());
        }
    }

    //Переход на окно аутентификации
    public void goToLoginWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            logger.error("Не удалось построить интерфейс экрана аутентификации! \n" +e.toString());
        }
    }

    // Закрыть смену
    public void report(ActionEvent actionEvent) throws IOException {

        //todo Проверяем если есть не допечатанные чеки, сообщаем об этом


        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Жак-Андрэ Продажи");

        Parent root = FXMLLoader.load(getClass().getResource("/view/zReportWindow.fxml"));

        dialog.setScene(new Scene(root, 700, 300));

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

    }


    // Суточный отчет
    public void xReportAction(ActionEvent actionEvent) throws IOException {

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Жак-Андрэ Продажи");

        Parent root = FXMLLoader.load(getClass().getResource("/view/xReportWindow.fxml"));

        dialog.setScene(new Scene(root, 400, 400));

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();



    }

    public void updateData(ActionEvent actionEvent) throws IOException {

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Жак-Андрэ Продажи");

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModalWhileUpdateDataFromServer.fxml"));

        dialog.setScene(new Scene(root, 700, 220));

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }


    // Возвраты
    public void returnCheck(ActionEvent event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/ReturnWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            logger.error("Не удается отобразить окно с возвратами!");
        }
    }
}
