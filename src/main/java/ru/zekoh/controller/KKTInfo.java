package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.zekoh.properties.Properties;

public class KKTInfo {

    // Label для описания ошибки с ККТ
    public Label errorLabel;
    public Button okBtn;
    public Label info;
    public Button repeatBtn;

    // Инициализация
    @FXML
    public void initialize() {
        errorLabel.setText(Properties.errorKKTString);
    }

    public void repeat(ActionEvent actionEvent) {
    }

    public void ok(ActionEvent actionEvent) {
    }
}
