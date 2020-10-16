package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.properties.Properties;

public class Info {
    public Label text;

    @FXML
    public void initialize() {
        text.setText(Properties.infoModalText);
    }

    public void ok(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
