package ru.zekoh.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.zekoh.core.report.ExelReport;

public class ExelController {

    @FXML
    public TextArea log;

    @FXML
    public Button exitBtn;

    @FXML
    public VBox vBox;

    @FXML
    public void initialize() {
        log.setEditable(false);
        exitBtn.setVisible(false);
        log.appendText("Идет анализ данных... \n");

        log.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                log.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        vBox.getChildren().add(progressIndicator);

        Task task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                    ExelReport.report(log, exitBtn, progressIndicator);
                return null;
            }
        };

        new Thread(task).start();
        // запуск во втором потоке с передачек text area
    }


    public void exit(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
