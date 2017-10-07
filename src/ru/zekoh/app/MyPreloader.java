package ru.zekoh.app;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyPreloader extends Preloader {

    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        VBox loading = new VBox(20);
        loading.setMaxWidth(Region.USE_PREF_SIZE);
        loading.setMaxHeight(Region.USE_PREF_SIZE);
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300,20);
        loading.getChildren().add(progressBar);
        loading.getChildren().add(new Label("Пожалуйста подождите..."));

        BorderPane root = new BorderPane(loading);
        Scene scene = new Scene(root);

        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(javafx.application.Preloader.StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == javafx.application.Preloader.StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
