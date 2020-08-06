package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.properties.Properties;

public class ModalWhileSendData {

        public Button okBtn;
        public Label info;
        Task task = null;

        // Инициализация
        @FXML
        public void initialize() {
            Properties.statusPrinted = false;
            okBtn.setVisible(false);

                task = new Task<Void>() {
                    @Override public Void call() {
                        System.out.println("Подготовка данных к отправке в subtotal!");
                        if (Properties.subtotal.sendSalesToSubtotal()) {

                            Properties.sendDataFromServer = true;

                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    /*Stage stage = (Stage) okBtn.getScene().getWindow();
                                    stage.close();*/
                                    info.setText("Данные успешно отправлены!");
                                    okBtn.setVisible(true);
                                }
                            });

                        } else {
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    info.setText("Ошибка при отправке данных, обратитесь к Руслану!");
                                    okBtn.setVisible(true);
                                }
                            });
                        }

                        return null;
                    }
                };

                new Thread(task).start();

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
