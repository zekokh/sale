package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.atol.drivers10.fptr.Fptr;
import ru.atol.drivers10.fptr.IFptr;
import ru.zekoh.core.printing.KKM;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.core.synchronisation.SData;
import ru.zekoh.core.synchronisation.Synchronisation;
import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAO.SessionDao;
import ru.zekoh.db.DAO.UserDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.DAOImpl.SellReportDaoImpl;
import ru.zekoh.db.DAOImpl.SessionDaoImpl;
import ru.zekoh.db.DAOImpl.UserDaoImpl;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.SellReport;
import ru.zekoh.db.entity.Session;
import ru.zekoh.properties.Properties;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenuController {

    //Кнопка для перехда в окно продаж
    @FXML
    public Button saleBtn;

    //Кнопка для печати Z-Отчета
    @FXML
    public Button reportBtn;

    //Кнопка для блокировки и перехода на окно регистрации
    @FXML
    public Button blockBtn;

    //Лейбл для отображение информации о ошибках
    @FXML
    public Label errorLabel;

    // Кнопка тестирования ОФД
    @FXML
    public Button testOFD;

    // Кнопка суточный отчет
    public Button xReport;

    private static String[] columns = {"Наименование", "Кол-во"};

    //Инициализация
    @FXML
    public void initialize() {
        saleBtn.setDisable(false);
        reportBtn.setDisable(false);
        blockBtn.setDisable(false);
        testOFD.setDisable(false);
        xReport.setDisable(false);

        if (Properties.FPTR == null) {
            try {
//                Properties.FPTR = KKMOFD.create();
            } catch (Exception e) {
                System.out.println("Ошибка! Не удалось создать объект драйвера ККТ!" + e.getMessage().toString());
            }
        }
    }

    //Переход в окно продаж
    public void goToSaleWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/SaleWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Переход на окно аутентификации
    public void goToLoginWindow(ActionEvent actionEvent) {

        //Создаем объект Session Dao для закрытие сессии
        SessionDao sessionDao = new SessionDaoImpl();

        //Получаем id текущего пользователя из сессии
        Long userId = sessionDao.getLastOpenSeesion().getUserId();

        //Создаем объект UserDao для поиска пользователя по id
        UserDao userDao = new UserDaoImpl();

        //Если сессия закрыта
        if (sessionDao.closeSession(userDao.getUserById(userId))) {

            if (Properties.FPTR != null) {
                KKMOFD.close(Properties.FPTR);
                Properties.FPTR = null;
            }

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/LoginWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                System.out.println(e);
                errorLabel.setText("К сожалению что-то пошло не так! Я не могу выйти из системы.");
            }
        } else {
            errorLabel.setText("К сожалению что-то пошло не так! Я не могу закрыть сессию.");
        }
    }

    public void report(ActionEvent actionEvent) {

        KKMOFD.closeShift(Properties.FPTR);


/*        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();
            KKM.report(dailyReport);
        } catch (Exception e) {
            System.out.println("что то пошло не так с ккм");
            System.out.println(e);
        }*/
    }

    // Диагностика с ОФД
    public void testOfdAction(ActionEvent actionEvent) throws IOException {
        //KKMOFD.ofdTest(Properties.FPTR);

        SellReportDaoImpl sellReportDao = new SellReportDaoImpl();
        List<SellReport> sellReports = sellReportDao.findAll();

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Статистика");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (SellReport sell : sellReports) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(sell.getName());

            row.createCell(1)
                    .setCellValue(sell.getCount());

            //Cell dateOfBirthCell = row.createCell(2);
            //dateOfBirthCell.setCellValue(employee.getDateOfBirth());
            //dateOfBirthCell.setCellStyle(dateCellStyle);

            //row.createCell(3)
            // .setCellValue(employee.getSalary());
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("выгрузка.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

    }

    // Суточный отчет
    public void xReportAction(ActionEvent actionEvent) {
        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();
            errorLabel.setText("Кол-во чеков: " + dailyReport.getNumberOfChecks() + "\n" +
                    "Возврат: " + dailyReport.getReturnPerDay() + " р. \n" +
                    "Наличными: " + dailyReport.getAmountCash() + " р. \n" +
                    "По карте: " + dailyReport.getAmountCard() + " р. \n" +
                    "Доход: " + dailyReport.getSoldPerDay() + " р.");
        } catch (Exception e) {
            System.out.println("что то пошло c суточным отчетом не так!");
            System.out.println(e);
        }
    }
}
