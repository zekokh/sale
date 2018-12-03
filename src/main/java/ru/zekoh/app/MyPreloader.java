package ru.zekoh.app;

import javafx.application.Preloader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyPreloader extends Preloader {

    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);

        VBox loading = new VBox(20);
        loading.setMaxWidth(Region.USE_PREF_SIZE);
        loading.setMaxHeight(Region.USE_PREF_SIZE);
        loading.setBackground(new Background(new BackgroundFill(Color.rgb(15,18,22), CornerRadii.EMPTY, Insets.EMPTY)));
        ProgressBar progressBar = new ProgressBar();
        progressBar.setStyle("-fx-padding: 0.20em;");

        progressBar.setPrefWidth(518.0f);

        ProgressIndicator progressIndicator = new ProgressIndicator();


        //Добавляем шрифт
        Label label = new Label("ZEKOH TECHNOLOGY");
        try {
            final String urlFont = getClass().getResource("/fonts/Exo.ttf").getFile();
            //final String urlFont = "C:\\Users\\kassa\\Desktop\\sell\\fonts\\Exo.ttf";
            final Font f = Font.loadFont(new FileInputStream(new File(urlFont)), 49);
            label.setFont(f); // use this font with our label
            label.setTextFill(Color.WHITE);
            //pane.getChildren().add(label);
            loading.getChildren().add(label);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BorderPane root = new BorderPane(loading);
        root.setBackground(new Background(new BackgroundFill(Color.rgb(15,18,22), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root);

        //scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        loading.getChildren().add(progressIndicator);

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
