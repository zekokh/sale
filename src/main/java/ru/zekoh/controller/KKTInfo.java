package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.properties.Properties;

public class KKTInfo {
    private static final Logger logger = LogManager.getLogger(KKTInfo.class);

    public Label errorLabel;
    public Label info;
    public Button reconnectBtn;
    public Button continueBtn;
    public Button upToPrintBtn;

    // Инициализация
    @FXML
    public void initialize() {
        errorLabel.setText(Properties.kktError.getDescription());
    }

    public void reconnect(ActionEvent event) {
        if (Properties.FPTR != null) {

            try {
                int answer = Properties.FPTR.cancelReceipt();
                String text = "Не удалось отменить чек!";
                if (answer == 0) {
                    text = "Чек успешно отменен в ККТ";
                }
                logger.info("Отменяем чек при повторной печати: " + text);
            } catch (Exception e) {
                logger.error("Ошибка при отмене повторной печати чека с ккт!" + e.getMessage());
            }

            if (Properties.FPTR.isOpened()) {
                Properties.FPTR.close();
                Properties.FPTR = null;
            }
        }

        try {
            KKMOFD.initDriver();
        } catch (Exception e) {
            logger.error("Не удалось создать объект драйвера ККТ!" + e.getMessage());
        }

        // 1 - отправить чек заного на печать
        Properties.KKTErrorInfoAction = 1;
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void continueAction(ActionEvent event) {
        // 2 - закрыть и пользователь сам потом допечатает
        Properties.KKTErrorInfoAction = 2;
        // Отключить отправку документов на печать принтера чека
        Properties.KKM = false;
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void upToPrintAction(ActionEvent event) {
        // 3 - попробовать допечатать
        Properties.KKTErrorInfoAction = 3;
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
