package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.core.printing.Acquiring;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.properties.Properties;

public class ZReportController {
    private static Logger logger = LogManager.getLogger(ZReportController.class);

    @FXML
    public Label label;



    @FXML
    public void initialize() {

    }


    public void ok(ActionEvent actionEvent) {
/*        try{
            // Закрыть интегрированный терминал
            if(Properties.ELECTRONIC_PAY_IN_INTEGRATION_TERMINAL){
                Acquiring.report();
                String text = Acquiring.readPFile();
                if(text.length() > 1){
                    KKMOFD.printSMTH(Properties.FPTR, text);
                }
            }
        }catch (Exception e){
            System.out.println("Проблема при закрытии смены на POS Сбербанк");
            System.out.println(e);
        }


        //*/
        KKMOFD.closeShift(Properties.FPTR);

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
