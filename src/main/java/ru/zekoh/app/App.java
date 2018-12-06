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
import ru.zekoh.db.Data;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private static Logger logger = LogManager.getLogger(App.class);

    List<Integer> arrayFolder = new ArrayList<Integer>();

    // Количество на странице
    int amountElements = 21;

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

            pagination();

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

        for (DataEntity data : dataEntity) {
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
                arrayFolder.add(folderParentId);
                folderListSortByLevel.put(folderParentId, folderList);
            }
        }

        return folderListSortByLevel;
    }

    private Map<Integer, ArrayList<Product>> generateProducts() {

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<DataEntity> dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.folder = 0 AND a.live = true ORDER BY a.serialNumber ASC", DataEntity.class).getResultList();
        session.close();

        //Создаем список папок отсортированных по уровню
        Map<Integer, ArrayList<Product>> productListSortByLevel = new HashMap<Integer, ArrayList<Product>>();

        for (DataEntity data : dataEntity) {
            Product product = new Product();
            product.setId(data.getId());
            product.setShortName(data.getShortName());
            product.setFullName(data.getFullName());
            product.setParentId(data.getParentId());
            product.setPrice(data.getPrice());
            product.setUnit(data.isUnit());

            int productParentId = product.getParentId();

            if (productListSortByLevel.containsKey(productParentId)) {
                productListSortByLevel.get(productParentId).add(product);
            } else {
                ArrayList<Product> productList = new ArrayList<Product>();
                productList.add(product);
                if(!arrayFolder.contains(productParentId)){
                    arrayFolder.add(productParentId);
                }

                productListSortByLevel.put(productParentId, productList);
            }
        }

        return productListSortByLevel;
    }

    private void generatePageWithFoldersAndProducts() {

        // Если есть хотябы папки, то
        try {
            for (Integer index : arrayFolder) {
                int foldersSize = Data.folders.get(index).size();

                if (Data.products.get(index) != null) {
                    int productsSize = Data.products.get(index).size();
                    int amount = foldersSize + productsSize;
                }
                int amount = foldersSize;

                // Если на странице больше 25 элементов
                if (amount > 25) {

                    if (foldersSize > 25) {
                        int countTime = 25 % foldersSize;

                    }
                } else if (foldersSize > 25) {

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void pagination() {
        Map<Integer,ArrayList<PageFolder>> arrayFolderMap = new HashMap<Integer, ArrayList<PageFolder>>();

        Map<Integer, List<PageProduct>> arrayProductMap = new HashMap<Integer,  List<PageProduct>>();


        if (Data.folders.size() > 0) {

            if (Data.products.size() > 0) {

                for (int i : arrayFolder) {

                    int sumFolders = 0;
                    int sumProducts = 0;

                    try {
                        sumFolders = Data.folders.get(i).size();
                    }catch (Exception e){

                    }

                    try {
                        sumProducts = Data.products.get(i).size();
                    }catch (Exception e){

                    }



                    int sum = sumFolders + sumProducts;

                    if (sum <= amountElements) {

                        if (Data.folders.get(i) != null) {
                            ArrayList<Folder> folders = new ArrayList<Folder>();

                            for (Folder folder : Data.folders.get(i)) {
                                folders.add(folder);
                            }

                            if (arrayFolderMap.containsKey(i)) {
                                arrayFolderMap.get(i).add(new PageFolder(1, folders));
                            }else {
                                ArrayList<PageFolder> list = new ArrayList<PageFolder>();
                                list.add(new PageFolder(1, folders));
                                arrayFolderMap.put(i, list);
                            }

                        }



                        //-----

                        if(Data.products.get(i) != null) {
                            List<Product> products = new ArrayList<Product>();

                            for (Product product : Data.products.get(i)) {
                                products.add(product);
                            }

                            if (arrayProductMap.containsKey(i)) {
                                arrayProductMap.get(i).add(new PageProduct(1, products));
                            }else {
                                List<PageProduct> list = new ArrayList<PageProduct>();
                                list.add(new PageProduct(1, products));
                                arrayProductMap.put(i, list);
                            }
                        }




                    } else {


                        if (sumFolders > amountElements) {

                        } else {
                            if (sumProducts > amountElements) {

                                int page = 1;

                                int count = sumProducts / amountElements;

                                int mod = sumProducts % amountElements;

                                int countElementLastPage = 0;

                                if (mod > 0) {
                                    countElementLastPage = mod;
                                }

                                boolean flag = true;
                                int start = 0;
                                int finish = amountElements - 1;

                                while (flag) {

                                    List<Product> products = new ArrayList<Product>();

                                    for (int m = start; m <= finish; m++) {
                                        products.add(Data.products.get(i).get(m));
                                    }

                                    if (arrayProductMap.containsKey(i)) {
                                        arrayProductMap.get(i).add(new PageProduct(page, products));
                                    }else {
                                        List<PageProduct> list = new ArrayList<PageProduct>();
                                        list.add(new PageProduct(page, products));
                                        arrayProductMap.put(i, list);
                                    }

                                    if (page <= count) {
                                        page++;

                                        start = amountElements * (page-1);

                                        if(countElementLastPage == 0){
                                            finish = amountElements * page-1;
                                        }else {
                                            finish = (amountElements * (page-1))+countElementLastPage -1;
                                        }


                                    } else {
                      /*                  if (countElementLastPage > 0) {
                                            start = amountElements * page-1;
                                            finish = countElementLastPage;
                                            countElementLastPage = 0;
                                        }else {
                                            flag = false;
                                        }*/

                                        flag = false;
                                    }
                                }


                            }
                        }
                    }
                }



            }

        } else if (Data.products.size() > 0) {
            for (int i : arrayFolder) {
                int sumFolders = Data.folders.get(i).size();
                int sumProducts = Data.products.get(i).size();

                int sum = sumFolders + sumProducts;
            }
        }

        Data.arrayFolderMap = arrayFolderMap;
        Data.arrayProductMap = arrayProductMap;

        System.out.println(Data.arrayFolderMap);
        System.out.println(Data.arrayProductMap);

        System.out.println("готово");
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
