package ru.zekoh.core;

import org.hibernate.Session;
import ru.zekoh.db.Data;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Сatalog {


    static List<Integer> arrayFolder;

    // Количество на странице
    static int amountElements = 25;

    public static void generate() {
        arrayFolder = new ArrayList<Integer>();
        Data.folders = generateFolders();
        Data.products = generateProducts();

        pagination();

    }

    private static Map<Integer, ArrayList<Folder>> generateFolders() {

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        try {
            List<DataEntity> dataEntity = session.createQuery("SELECT a FROM DataEntity a WHERE a.folder = 1 AND a.live = true ORDER BY a.serialNumber asc ", DataEntity.class).getResultList();


            session.close();

            //Создаем список папок отсортированных по уровню
            Map<Integer, ArrayList<Folder>> folderListSortByLevel = new HashMap<Integer, ArrayList<Folder>>();

            for (DataEntity data : dataEntity) {
                Folder folder = new Folder();
                folder.setId(data.getId());
                folder.setName(data.getShortName());
                folder.setParentId(data.getParentId());
                folder.setAdministrativeAccess(data.isAdministrativeAccess());

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
        } catch (Exception e) {
            System.out.println("Ошибка! " + e.toString());
            return null;
        }
    }

    private static Map<Integer, ArrayList<Product>> generateProducts() {

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
            product.setClassifierId(data.getClassifier());
            product.setParentId(data.getParentId());
            product.setPrice(data.getPrice());
            product.setUnit(data.isUnit());

            int productParentId = product.getParentId();

            if (productListSortByLevel.containsKey(productParentId)) {
                productListSortByLevel.get(productParentId).add(product);
            } else {
                ArrayList<Product> productList = new ArrayList<Product>();
                productList.add(product);
                if (!arrayFolder.contains(productParentId)) {
                    arrayFolder.add(productParentId);
                }

                productListSortByLevel.put(productParentId, productList);
            }
        }

        return productListSortByLevel;
    }

    private static void pagination() {
        Map<Integer, ArrayList<PageFolder>> arrayFolderMap = new HashMap<Integer, ArrayList<PageFolder>>();

        Map<Integer, List<PageProduct>> arrayProductMap = new HashMap<Integer, List<PageProduct>>();


        if (Data.folders.size() > 0) {

            if (Data.products.size() > 0) {

                for (int i : arrayFolder) {

                    int sumFolders = 0;
                    int sumProducts = 0;

                    try {
                        sumFolders = Data.folders.get(i).size();
                    } catch (Exception e) {

                    }

                    try {
                        sumProducts = Data.products.get(i).size();
                    } catch (Exception e) {

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
                            } else {
                                ArrayList<PageFolder> list = new ArrayList<PageFolder>();
                                list.add(new PageFolder(1, folders));
                                arrayFolderMap.put(i, list);
                            }

                        }


                        //-----

                        if (Data.products.get(i) != null) {
                            List<Product> products = new ArrayList<Product>();

                            for (Product product : Data.products.get(i)) {
                                products.add(product);
                            }

                            if (arrayProductMap.containsKey(i)) {
                                arrayProductMap.get(i).add(new PageProduct(1, products));
                            } else {
                                List<PageProduct> list = new ArrayList<PageProduct>();
                                list.add(new PageProduct(1, products));
                                arrayProductMap.put(i, list);
                            }
                        }


                    } else {


                        if (sumFolders > amountElements) {

                        } else {

                            // Если в папке больше папок чем кол-во страниц то работать не будет
                            if (Data.folders.get(i) != null) {
                                ArrayList<Folder> folders = new ArrayList<Folder>();

                                for (Folder folder : Data.folders.get(i)) {
                                    folders.add(folder);
                                }

                                if (arrayFolderMap.containsKey(i)) {
                                    arrayFolderMap.get(i).add(new PageFolder(1, folders));
                                } else {
                                    ArrayList<PageFolder> list = new ArrayList<PageFolder>();
                                    list.add(new PageFolder(1, folders));
                                    arrayFolderMap.put(i, list);
                                }
                            }

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
                                    } else {
                                        List<PageProduct> list = new ArrayList<PageProduct>();
                                        list.add(new PageProduct(page, products));
                                        arrayProductMap.put(i, list);
                                    }

                                    if (page <= count) {
                                        page++;

                                        start = amountElements * (page - 1);

                                        if (countElementLastPage == 0) {
                                            finish = amountElements * page - 1;
                                        } else {
                                            finish = (amountElements * (page - 1)) + countElementLastPage - 1;
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
}
