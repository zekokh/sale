package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import ru.zekoh.properties.Properties;

public class PayCard {
    public void ok(ActionEvent actionEvent) {
        Properties.isPayCard = true;
    }

    public void exit(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
