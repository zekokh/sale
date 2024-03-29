package ru.zekoh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;
import ru.zekoh.subtotal.Subtotal;

import static ru.zekoh.core.Сatalog.generate;

public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);


    @Override
    public void init() {
        try {
            logger.info("Запуск системы продаж.");

            // Получаем список пользователей
            Properties.sessionFactory = HibernateSessionFactory.getSessionFactory();
            Session session = Properties.sessionFactory.openSession();
            Properties.users = session.createQuery("SELECT a FROM UserEntity a", UserEntity.class).getResultList();
            session.close();

            // Аутентификация в subtotal
/*            Properties.subtotal = Subtotal.getInstance();
            if (Properties.subtotal == null) {
                logger.error("Ошибка создания объекта для взаимодействия с сервером subtotal");
            }*/
            generate();

            Properties.initDataWithoutFile();
        } catch (Exception e) {
            logger.error("Ошибка! " + e.toString());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Отмена перетаскивания окно и скрытие кнопок изменения вида окна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/view/"+Properties.pathToFXML+"/LoginWindow.fxml"));
        primaryStage.setTitle("Жак-Андрэ Продажи");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
