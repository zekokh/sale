package ru.zekoh.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.zekoh.core.loyalty.Customer;
import ru.zekoh.core.loyalty.Employee;
import ru.zekoh.core.loyalty.Loyalty;
import ru.zekoh.core.loyalty.StoreCard;
import ru.zekoh.properties.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ModalUpdateLoyaltyCardController {
    private static Logger logger = LogManager.getLogger(ModalUpdateLoyaltyCardController.class);

    @FXML
    public Button cancelBtn;
    Task task = null;

    // Инициализация
    @FXML
    public void initialize() {
        Properties.loyaltyUrlUpdateMsg = "Ошибка при обновлении карты лояльности!";

        task = new Task<Void>() {
            @Override
            public Void call() {
                String url = Properties.loyaltyUrlUpdate + Properties.modalNumberCard;
                // Запрашиваю на сервере информацию
                try {
                    ArrayList<Integer> array = new ArrayList<Integer>();
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    int responseCode = con.getResponseCode();

                    if (responseCode == 200) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), "UTF8"));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        JSONObject json = new JSONObject(response.toString());
                        String msg = json.getString("msg");

                        Properties.loyaltyUrlUpdateMsg = msg;

                    } else {
                        logger.error("Ошибка при обнавлении карты!");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    System.out.println("Ошибка при запросе на обновлении карты!");
                    System.out.println(e.getMessage());
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Stage stage = (Stage) cancelBtn.getScene().getWindow();
                        stage.close();
                    }
                });


                return null;
            }
        };

        new Thread(task).start();
    }

    //Выход из модального окна
    public void exit(ActionEvent actionEvent) {
        if (task != null) {
            task.cancel();
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
