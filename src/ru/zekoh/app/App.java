package ru.zekoh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.zekoh.core.report.ExelReport;
import ru.zekoh.db.DAO.FolderDao;
import ru.zekoh.db.DAO.ProductDao;
import ru.zekoh.db.DAO.PromocodDao;
import ru.zekoh.db.DAOImpl.FolderDaoImpl;
import ru.zekoh.db.DAOImpl.ProductDaoImpl;
import ru.zekoh.db.DAOImpl.PromocodDaoImpl;
import ru.zekoh.db.Data;
import ru.zekoh.db.DataBase;
import ru.zekoh.properties.Properties;

public class App extends Application {
    @Override
    public void init() throws Exception {

        //todo Получаем данные о продуктах из БД и сортируем по уровню
        //Инициализация данных из проперти файла
        // Properties.initData();
        Properties.initDataWithoutFile();

        if (DataBase.getConnection() != null) {

            //Получаю папки и сохраняю в оперативки
            FolderDao folderDao = new FolderDaoImpl();
            Data.setFoldersSortedByLevel(folderDao.getFoldersSortedByLevel());

            //Получаю продукты и сохраняю в оперативки
            ProductDao productDao = new ProductDaoImpl();
            Data.setProductsSortedByLevel(productDao.getProductsSortedByLevel());

            //todo Если сегодня 1 ое число месяца то обновляем баланс сотрудникво
        }

        // Делаем тяжелые долгии запросы
       // Thread.sleep(5000); //что бы видно было заставку
       //for (int i = 0; i < 10; i++) {
         //   Thread.sleep(200);
         //}

        // ExelReport.report();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Отмена перетаскивания окно и скрытие кнопок изменения вида окна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/LoginWindow.fxml"));
        primaryStage.setTitle("Жак-Андрэ Продажи");
        primaryStage.setScene(new Scene(root, 1024, 740));
        primaryStage.show();
    }
}
