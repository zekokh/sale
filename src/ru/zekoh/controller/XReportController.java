package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.entity.DailyReport;

public class XReportController {

    @FXML
    public Label label;

    @FXML
    public Button ok;

    @FXML
    public void initialize() {

        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();
            label.setText("Кол-во чеков: " + dailyReport.getNumberOfChecks() + "\n" +
                    "Возврат: " + dailyReport.getReturnPerDay() + " р. \n" +
                    "Наличными: " + dailyReport.getAmountCash() + " р. \n" +
                    "По карте: " + dailyReport.getAmountCard() + " р. \n" +
                    "Доход: " + dailyReport.getSoldPerDay() + " р.");
        } catch (Exception e) {
            System.out.println("что то пошло c суточным отчетом не так!");
            System.out.println(e);
        }
    }

    public void exit(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
