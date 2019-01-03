package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.properties.Properties;

public class ModalWhilePrintCheck {

    public Button okBtn;
    public Label info;

    // Инициализация
    @FXML
    public void initialize() {
        Properties.statusPrinted = false;
        okBtn.setVisible(false);

        if (Properties.checkObject != null) {

            boolean flag = true;
            // Отправить на печать и когда будет
            // Создаем новый поток который получает данные о пользователе и отрисовывает их
            Thread clientThread = new Thread(() -> {

                try {
                        Platform.runLater(() -> {
                            if(KKMOFD.sendToKKM(Properties.checkObject)){
                               Properties.statusPrinted = true;
                            }else {
                                Properties.checkObject = null;
                                info.setText("Ошибка при печати!");
                                okBtn.setVisible(true);
                            }

                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            clientThread.setDaemon(true);

            clientThread.start();
        }
    }

    public void ok(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void exit(ActionEvent actionEvent) {

    }
}
