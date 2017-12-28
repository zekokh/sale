package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.zekoh.core.synchronisation.CloseThread;
import ru.zekoh.core.synchronisation.SData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExitLoader {
    @FXML
    public VBox vbox;
    @FXML
    public Pane pane;

    @FXML
    public void initialize() {
        //Добавляем шрифт
        Label label = new Label("Подождите... идет синхронизация");
        try {
            final String urlFont = getClass().getResource("/fonts/Exo.ttf").getFile();
            final Font f = Font.loadFont(new FileInputStream(new File(urlFont)), 49);
            label.setFont(f); // use this font with our label
            label.setTextFill(Color.BLACK);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ProgressIndicator progressIndicator = new ProgressIndicator();
        vbox.getChildren().add(label);
        vbox.getChildren().add(progressIndicator);

        System.out.println("Окно загрузки...");

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                boolean flag = true;

                while (flag) {
                    System.out.println("...");
                    if (!SData.isInTheWork()) {
                        flag = false;
                        Stage stage = (Stage) pane.getScene().getWindow();
                        Platform.runLater(() -> {
                            stage.close();
                        });
                        return null;
                    }
                    Thread.sleep(10000);
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
