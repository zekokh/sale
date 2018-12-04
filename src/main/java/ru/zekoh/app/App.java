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
import ru.zekoh.db.DAO.FolderDao;
import ru.zekoh.db.DAOImpl.FolderDaoImpl;
import ru.zekoh.db.Data;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.DataEntity;
import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.Product;
import ru.zekoh.db.entity.UserEntity;
import ru.zekoh.properties.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private static Logger logger = LogManager.getLogger(App.class);

    @Override
    public void init() throws Exception {

        //todo Получаем данные о продуктах из БД и сортируем по уровню
        //Инициализация данных из проперти файла
        // Properties.initData();

        //todo Если сегодня 1 ое число месяца то обновляем баланс сотрудникво

        logger.info("Инициаизация связи с базой данных.");

        try {
            // Получаем список пользователей
            Session session = HibernateSessionFactory.getSessionFactory().openSession();

            Properties.users = session.createQuery("SELECT a FROM UserEntity a", UserEntity.class).getResultList();

            session.close();

            Data.folders = generateFolders();
            Data.products = generateProducts();

        } catch (Exception e) {
            logger.error("Произошла ошибка при попытки подключения к БД! \n" + e.toString());
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Отмена перетаскивания окно и скрытие кнопок изменения вида окна
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"));
        primaryStage.setTitle("Жак-Андрэ Продажи");
        primaryStage.setScene(new Scene(root, 1024, 740));
        primaryStage.show();
    }

    private Map<Integer, ArrayList<Folder>> generateFolders() {

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<DataEntity> dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.folder = 1 ORDER BY a.serialNumber ASC", DataEntity.class).getResultList();
        session.close();

        //Создаем список папок отсортированных по уровню
        Map<Integer, ArrayList<Folder>> folderListSortByLevel = new HashMap<Integer, ArrayList<Folder>>();

        for(DataEntity data : dataEntity){
            Folder folder = new Folder();
            folder.setId(data.getId());
            folder.setName(data.getShortName());
            folder.setParentId(data.getParentId());

            int folderParentId = folder.getParentId();

            if (folderListSortByLevel.containsKey(folderParentId)) {
                folderListSortByLevel.get(folderParentId).add(folder);
            } else {
                ArrayList<Folder> folderList = new ArrayList<Folder>();
                folderList.add(folder);
                folderListSortByLevel.put(folderParentId, folderList);
            }
        }

        return folderListSortByLevel;
    }

    private Map<Integer, ArrayList<Product>> generateProducts() {

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<DataEntity> dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.folder = 0 ORDER BY a.serialNumber ASC", DataEntity.class).getResultList();
        session.close();

        //Создаем список папок отсортированных по уровню
        Map<Integer, ArrayList<Product>> productListSortByLevel = new HashMap<Integer, ArrayList<Product>>();

        for(DataEntity data : dataEntity){
            Product product = new Product();
            product.setId(data.getId());
            product.setShortName(data.getShortName());
            product.setFullName(data.getFullName());
            product.setParentId(data.getParentId());
            product.setPrice(data.getPrice());

            int productParentId = product.getParentId();

            if (productListSortByLevel.containsKey(productParentId)) {
                productListSortByLevel.get(productParentId).add(product);
            } else {
                ArrayList<Product> productList = new ArrayList<Product>();
                productList.add(product);
                productListSortByLevel.put(productParentId, productList);
            }
        }

        return productListSortByLevel;
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
