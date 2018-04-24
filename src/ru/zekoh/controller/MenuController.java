package ru.zekoh.controller;

import com.sun.deploy.net.HttpResponse;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.omg.CORBA.NameValuePair;
import ru.zekoh.core.printing.KKM;
import ru.zekoh.core.synchronisation.SData;
import ru.zekoh.core.synchronisation.Synchronisation;
import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.*;
import ru.zekoh.db.DAOImpl.*;
import ru.zekoh.db.Data;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.DailyReport;
import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static javafx.css.StyleOrigin.USER_AGENT;

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

    @FXML
    public ProgressBar bar;

    @FXML
    public Button reloadBtn;

    //Инициализация
    @FXML
    public void initialize() {
        saleBtn.setDisable(false);
        reportBtn.setDisable(false);
        blockBtn.setDisable(false);
        bar.setVisible(false);

        /*if(SData.isInTheWork()){
            System.out.println("Идет синхронизация ...");
        } else {
            Synchronisation synchronisation = new Synchronisation();
            synchronisation.start();
        }*/

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

        CheckDao checkDao = new CheckDaoImpl();

        try {
            DailyReport dailyReport = checkDao.soldPerDay();
            KKM.report(dailyReport);
        } catch (Exception e) {
            System.out.println("что то пошло не так с ккм");
            System.out.println(e);
        }
    }

    // обновить данные с сервера админ панели
    public void reload(ActionEvent actionEvent) throws IOException, InterruptedException {

        //Runtime.getRuntime().exec("cmd /c FullFileName.bat");

        Task task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        saleBtn.setVisible(false);
                        reportBtn.setVisible(false);
                        blockBtn.setVisible(false);
                        reloadBtn.setVisible(false);
                        bar.setVisible(true);
                        errorLabel.setVisible(true);
                        errorLabel.setText("Идет синхронизация подождите!");
                    }
                });

                Runtime runtime = Runtime.getRuntime();
                try {
                    Process p1 = runtime.exec("cmd /c start /B C:\\Users\\ен\\Desktop\\Sell\\demon.bat");
                    InputStream is = p1.getInputStream();
                    int i = 0;
                    while( (i = is.read() ) != -1) {
                        System.out.print((char)i);
                    }
                } catch(IOException ioException) {
                    System.out.println(ioException.getMessage() );
                }

  /*              try {
                    Process p = Runtime.
                            getRuntime().
                            exec("cmd /c start \"\" C:\\Users\\ен\\Desktop\\Sell\\demon.bat");
                    p.waitFor();
                }catch (Exception e){
                    System.out.println("Ошибка синхронизации с JARVISом");
                    System.out.println(e);
                }*/


               /* Runtime run = Runtime.getRuntime();
                Process p = null;
                String cmd = "C:\\Users\\ен\\Desktop\\Sell\\demon.bat";
                try {
                    p = run.exec("cmd.exe /c " + cmd);
                    // p = run.exec(cmd);
                    InputStream stderr = p.getErrorStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    System.out.println("<ERROR>");
                    while ((line = br.readLine()) != null)
                        System.out.println(line);
                    System.out.println("</ERROR>");
                    int exitVal = p.exitValue();
                    System.out.println("Process exitValue: " + exitVal);
                    //TODO ; have to incorporate waitFor() properly
                    //p.waitFor();
                    //System.out.println(p.exitValue());
                    //System.out.println(p.waitFor());
                    System.out.println("RUN.COMPLETED.SUCCESSFULLY");
                } catch (IOException e) {
                    System.out.println("ERROR.RUNNING.CMD");
                    e.printStackTrace();
                    p.destroy();
                    p.exitValue();

                } finally {
                    p.destroy();
                }
*/



                //Получаю папки и сохраняю в оперативки
                FolderDao folderDao = new FolderDaoImpl();
                Data.setFoldersSortedByLevel(folderDao.getFoldersSortedByLevel());

                //Получаю продукты и сохраняю в оперативки
                ProductDao productDao = new ProductDaoImpl();
                Data.setProductsSortedByLevel(productDao.getProductsSortedByLevel());

/*                final int max = 10;
                for (int i = 1; i <= max; i++) {
                    Thread.sleep(10);
                    updateProgress(i, max);
                }*/

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        saleBtn.setVisible(true);
                        reportBtn.setVisible(true);
                        blockBtn.setVisible(true);
                        reloadBtn.setVisible(true);
                        errorLabel.setVisible(false);
                        bar.setVisible(false);
                    }
                });

                return null;
            }
        };

        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();


         /*if (DataBase.getConnection() != null) {

             saleBtn.setVisible(false);
             reportBtn.setVisible(false);
             blockBtn.setVisible(false);
             errorLabel.setVisible(true);
             errorLabel.setText("Идет синхронизация подождите!");

           //Получаю папки и сохраняю в оперативки
            FolderDao folderDao = new FolderDaoImpl();
            Data.setFoldersSortedByLevel(folderDao.getFoldersSortedByLevel());

            //Получаю продукты и сохраняю в оперативки
            ProductDao productDao = new ProductDaoImpl();
            Data.setProductsSortedByLevel(productDao.getProductsSortedByLevel());

            for (int i = 0; i< 100; i++) {
                Thread.sleep(100);
                System.out.println(i);
            }

            saleBtn.setVisible(true);
            reportBtn.setVisible(true);
            blockBtn.setVisible(true);
            errorLabel.setVisible(true);
            errorLabel.setText("Синхронизация прошла успешно!");

            //todo Если сегодня 1 ое число месяца то обновляем баланс сотрудникво
        }*/
    }
}
