package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.zekoh.core.KKTError;
import ru.zekoh.core.loyalty.Loyalty;
import ru.zekoh.core.loyalty.StoreCard;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.properties.Properties;

public class ModalSearchCustomer {
    @FXML
    public Button cancelBtn;
    Task task = null;

    // Инициализация
    @FXML
    public void initialize() {
        Properties.modalStoreCard = null;
        task = new Task<Void>() {
            @Override
            public Void call() {
                StoreCard storeCard = Loyalty.searchByNumber(Properties.modalNumberCard);
                if (storeCard != null) {
                    Properties.modalStoreCard = storeCard;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = (Stage) cancelBtn.getScene().getWindow();
                            stage.close();
                        }
                    });

                } else {
                    Properties.modalStoreCard = null;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Stage stage = (Stage) cancelBtn.getScene().getWindow();
                            stage.close();
                        }
                    });
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    //Выход из модального окна
    public void exit(ActionEvent actionEvent) {
        if (task != null) {
            task.cancel();
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
