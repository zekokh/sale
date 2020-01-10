package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.properties.Properties;

public class SettingMenuController {

    private static final Logger logger = LogManager.getLogger(MenuController.class);

    @FXML
    public Button kktBtn;

    //Инициализация
    @FXML
    public void initialize() {
        toggleText();
    }

    // Отключение и включение связи с ККТ
    public void toggle(ActionEvent event) {
        if (Properties.KKM) {

            Properties.KKM = false;

        }else {

            Properties.KKM = true;
        }

        toggleText();
    }

    private void toggleText(){
        if (Properties.KKM) {
            // ККТ включен
            kktBtn.setText("Выключить печать на ККТ");

        }else {
            // ККТ выключен
            kktBtn.setText("Включить печать на ККТ");
        }
    }

    public void back(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/" + Properties.pathToFXML + "/MenuWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            logger.error("Не удается отобразить окно с возвратами!");
        }
    }
}
