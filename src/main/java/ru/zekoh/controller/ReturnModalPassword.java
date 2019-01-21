package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.zekoh.properties.Properties;

public class ReturnModalPassword {


    public TextField label;

    public void cancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void ok(ActionEvent event) {


        // Проверить если все гуд сделать возврат если нет отобразить ошибку
        if(label.getLength() > 0){
            if(Properties.currentUser != null) {
                if(label.getText().equals(Properties.currentUser.getShortPassword())){
                    Properties.returnStatusPassword = true;
                }
            }

        }

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }
}
