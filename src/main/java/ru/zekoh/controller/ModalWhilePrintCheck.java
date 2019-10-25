package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
    public Button repeatBtn;
    public Label info;
    Task task = null;

    // Инициализация
    @FXML
    public void initialize() {
        Properties.statusPrinted = false;
        okBtn.setVisible(false);
        repeatBtn.setVisible(false);

        if (Properties.checkObject != null) {

            task = new Task<Void>() {
                @Override
                public Void call() {
                    if (KKMOFD.sendToKKM(Properties.checkObject)) {
                        System.out.println("C принтера пришло true!");
                        Properties.statusPrinted = true;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = (Stage) okBtn.getScene().getWindow();
                                stage.close();
                            }
                        });

                    } else {
                        System.out.println("C принтера пришло false!");

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                info.setText("Ошибка при печати. Чек пробит но не фискализирован!");
                                okBtn.setVisible(true);
                                repeatBtn.setVisible(true);
                            }
                        });
                    }

                    return null;
                }
            };

            new Thread(task).start();
        }
    }

    public void ok(ActionEvent actionEvent) {
        if (task != null) {
            task.cancel();
        }
        Properties.checkObject = null;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void repeat(ActionEvent actionEvent) {
        if (task != null) {
            task.cancel();
        }

        Properties.statusPrinted = false;
        okBtn.setVisible(false);
        repeatBtn.setVisible(false);

        new Thread(task).start();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void exit(ActionEvent actionEvent) {

    }
}
