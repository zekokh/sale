package ru.zekoh.core.synchronisation;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CloseThread extends Thread {
    public void run(Pane pane){
        System.out.println("в close");
        while (true){
            if(!SData.isInTheWork()){
                Stage stage = (Stage) pane.getScene().getWindow();
                stage.close();
            }
        }
    }
}
