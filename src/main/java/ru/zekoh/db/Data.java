package ru.zekoh.db;

import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Data {

    //Отсортированные папки по уровню
    private static Map<Integer, ArrayList<Folder>> foldersSortedByLevel;

    //Отсортированные продукты по уровню
    private static Map<Integer, ArrayList<Product>> productsSortedByLevel;

    //Вернуть товар по его id
    public static Product getProductById(int id, int level){

        List <Product> products = productsSortedByLevel.get(level);
        for (int i = 0; i < products.size(); i++){
           if (products.get(i).getId() == id) {
               return products.get(i);
           }
        }

        return null;
    }

    public static Map<Integer, ArrayList<Folder>> getFoldersSortedByLevel() {
        return foldersSortedByLevel;
    }

    public static void setFoldersSortedByLevel(Map<Integer, ArrayList<Folder>> foldersSortedByLevel) {
        Data.foldersSortedByLevel = foldersSortedByLevel;
    }

    public static Map<Integer, ArrayList<Product>> getProductsSortedByLevel() {
        return productsSortedByLevel;
    }

    public static void setProductsSortedByLevel(Map<Integer, ArrayList<Product>> productsSortedByLevel) {
        Data.productsSortedByLevel = productsSortedByLevel;
    }
}
