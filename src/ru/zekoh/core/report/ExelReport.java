package ru.zekoh.core.report;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ExelReport {

    private static List<Report> getData(TextArea textArea) {



        //Текущий день
        String today = "" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + "";

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();
        List<Report> reportList = new ArrayList<Report>();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `product` WHERE is_a_live ='1' AND folder = 0");
                while (rs.next()) {
                    Report report = new Report();
                    report.setProductId(rs.getLong(1));
                    report.setName(rs.getString(2));
                    reportList.add(report);
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();

        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        for (Report r : reportList) {
            try {
                connection = DataBase.getConnection();
                if (connection != null) {
                    Statement stmt = null;
                    connection.setAutoCommit(false);
                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM `goods`\n" +
                            "INNER JOIN `check` ON `goods`.`check_id` = `check`.`id` WHERE `goods`.`product_id` = " + r.getProductId() + " AND `check`.`payment_state` = 1 AND `check`.`return_status` = 0 AND `check`.`date_of_closing` LIKE '%" + today + "%';");

                    while (rs.next()) {
                        //int iii = rs.getInt("count(*)");

                        r.setCount(rs.getInt(1));
                    }


                    ResultSet rst = stmt.executeQuery("SELECT SUM(total) FROM `goods`\n" +
                            "INNER JOIN `check` ON `goods`.`check_id` = `check`.`id` WHERE `goods`.`product_id` = " + r.getProductId() + " AND `check`.`payment_state` = 1 AND `check`.`return_status` = 0 AND `check`.`date_of_closing` LIKE '%" + today + "%';");

                    while (rst.next()) {
                        //int iii = rs.getInt("count(*)");

                        r.setTotal(rst.getDouble(1));
                    }


                    stmt.close();
                    System.out.println("Рассичтано для " + r.getName());


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textArea.appendText("Рассичтано для " + r.getName() +"\n");

                        }
                    });


                }

                //Комит транзакции
                connection.commit();

            } catch (SQLException e) {
                System.out.println("Exception Message " + e.getLocalizedMessage());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }


        }

        return reportList;
    }

    public static void report(TextArea textArea, Button exitBtn,  ProgressIndicator progressIndicator) {
        List<Report> reportList = getData(textArea);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Отчет по продажам");
        int rownum = 0;
        Cell cell;
        Row row = sheet.createRow(rownum);


        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("id");
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Наименование товара");
        // Salary
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Кол-во");

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Сумма");

        // Data
        for (int i = 0; i < reportList.size(); i++) {
            row = sheet.createRow(i);

            // EmpNo (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(reportList.get(i).getProductId());
            // EmpName (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(reportList.get(i).getName());
            // Salary (C)
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(reportList.get(i).getCount());
            // Grade (D)
            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(reportList.get(i).getTotal());
        }

        //Текущий день
        String today = "" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + "";

       // File file = new File("/Users/zekokh/Desktop/test.xls");
        File file = new File("C:\\Users\\kassa\\Desktop\\report-"+today+".xls");
        file.getParentFile().mkdirs();


        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(file);
            workbook.write(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textArea.clear();
        textArea.appendText("Анализ продаж прошел успешно. Файл сохранен.");
        progressIndicator.setVisible(false);
        exitBtn.setVisible(true);
    }
}
