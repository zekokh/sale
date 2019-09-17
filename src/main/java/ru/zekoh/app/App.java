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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import static ru.zekoh.core.Сatalog.generate;

public class App extends Application {

    //private static Logger logger = LogManager.getLogger(App.class);

    @Override
    public void init() throws Exception {

        //todo Если сегодня 1 ое число месяца то обновляем баланс сотрудникво

        // logger.info("Инициаизация связи с базой данных.");

        try {
            // Получаем список пользователей
            Properties.sessionFactory  = HibernateSessionFactory.getSessionFactory();
            Session session = Properties.sessionFactory.openSession();
            Properties.users = session.createQuery("SELECT a FROM UserEntity a", UserEntity.class).getResultList();
            session.close();

            generate();

            Properties.initDataWithoutFile();
        } catch (Exception e) {
            System.out.println("Ошибка! "+e.toString());
            //logger.error("Произошла ошибка при попытки подключения к БД! \n" + e.toString());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Отмена перетаскивания окно и скрытие кнопок изменения вида окна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"));
        primaryStage.setTitle("Жак-Андрэ Продажи");
        primaryStage.setScene(new Scene(root, 1280, 760));
        primaryStage.show();
    }

    /*            session.beginTransaction();

            UserEntity userEntity = new UserEntity();

            userEntity.setLogin("testFrom Hybernate");
            userEntity.setMail("misterTest@mail.ru");
            userEntity.setName("Test testovich");
            userEntity.setPassword("1235");
            userEntity.setRoleId(1);

            session.save(userEntity);
            session.getTransaction().commit();
            session.close();

            */
}
