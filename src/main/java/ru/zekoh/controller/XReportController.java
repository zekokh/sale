package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.entity.DailyReport;

public class XReportController {

    private static Logger logger = LogManager.getLogger(XReportController.class);

    @FXML
    public Label label;

    @FXML
    public Button ok;

    @FXML
    public void initialize() {



        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();

            if(dailyReport != null){


            label.setText("Кол-во чеков: " + dailyReport.getNumberOfChecks() + "\n" +
                    "Возврат: " + dailyReport.getReturnPerDay() + " р. \n" +
                    "Наличными: " + dailyReport.getAmountCash() + " р. \n" +
                    "По карте: " + dailyReport.getAmountCard() + " р. \n" +
                    "Доход: " + dailyReport.getSoldPerDay() + " р.");

            }else {
                label.setText("Произошла ошибка при формироании суточного отчета!");
            }
        } catch (Exception e) {
            logger.error("что то пошло c суточным отчетом не так!");
        }
    }

    public void exit(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
