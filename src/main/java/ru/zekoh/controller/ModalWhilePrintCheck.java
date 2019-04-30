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
    public Label info;
    Task task = null;

    // Инициализация
    @FXML
    public void initialize() {
        Properties.statusPrinted = false;
        okBtn.setVisible(false);

        if (Properties.checkObject != null) {

            task = new Task<Void>() {
                @Override public Void call() {
                    if (KKMOFD.sendToKKM(Properties.checkObject)) {
                        System.out.println("C принтера пришло true!");
                        Properties.statusPrinted = true;

                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                Stage stage = (Stage) okBtn.getScene().getWindow();
                                stage.close();
                            }
                        });

                    } else {
                        System.out.println("C принтера пришло false!");
                        Properties.checkObject = null;
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                info.setText("Ошибка при печати!");
                                okBtn.setVisible(true);
                            }
                        });
                    }

                    return null;
                }
            };

            new Thread(task).start();

            boolean flag = true;
            // Отправить на печать и когда будет
            // Создаем новый поток который получает данные о пользователе и отрисовывает их
/*            Thread clientThread = new Thread(() -> {

                try {
                    Platform.runLater(() -> {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        *//*if (KKMOFD.sendToKKM(Properties.checkObject)) {
                            Properties.statusPrinted = true;
                            Stage stage = (Stage) okBtn.getScene().getWindow();
                            stage.close();
                        } else {
                            Properties.checkObject = null;
                            info.setText("Ошибка при печати!");
                            okBtn.setVisible(true);
                        }*//*

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            clientThread.setDaemon(true);

            clientThread.start();*/
        }
    }

    public void ok(ActionEvent actionEvent) {
        if(task != null){
            task.cancel();
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void exit(ActionEvent actionEvent) {

    }
}
