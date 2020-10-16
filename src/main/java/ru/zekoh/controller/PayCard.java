package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import ru.zekoh.core.printing.Acquiring;
import ru.zekoh.properties.Properties;

public class PayCard {

    public void ok(ActionEvent actionEvent) {
        Properties.isPayCard = true;

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void exit(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
