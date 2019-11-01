package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.core.KKTError;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.properties.Properties;

public class ModalWhilePrintCheck {
    private static final Logger logger = LogManager.getLogger(ModalWhilePrintCheck.class);

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
                @Override
                public Void call() {
                    KKTError status = KKMOFD.sendToKKM(Properties.checkObject, Properties.ModalWhilePrintCheckBool);
                    if (status.isStatus()) {
                        Properties.kktError = status;
                        Properties.statusPrinted = true;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage stage = (Stage) okBtn.getScene().getWindow();
                                stage.close();
                            }
                        });

                    } else {
                        Properties.kktError = status;
                        Properties.statusPrinted = false;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                info.setText("Ошибка при печати. Чек пробит но не фискализирован! " + Properties.kktError.getDescription());
                                okBtn.setVisible(true);
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

    public void exit(ActionEvent actionEvent) {

    }

}
