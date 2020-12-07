package ru.zekoh.db;

import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.PageFolder;
import ru.zekoh.db.entity.PageProduct;
import ru.zekoh.db.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    // Список папок
    public static Map<Integer, ArrayList<Folder>> folders = null;

    // Список продуктов и товаров
    public static Map<Integer, ArrayList<Product>> products = null;

    public static Map<Integer, ArrayList<PageFolder>> arrayFolderMap = new HashMap<Integer, ArrayList<PageFolder>>();

    public static Map<Integer, List<PageProduct>> arrayProductMap = new HashMap<Integer, List<PageProduct>>();


    //Вернуть товар по его id
    public static Product getProductById(int id, int level){

        List <Product> productList = products.get(level);
        for (int i = 0; i < productList.size(); i++){
           if (productList.get(i).getId() == id) {
               return productList.get(i);
           }
        }

        return null;
    }

    public static Product getProductById(int id){
        /*
        for (List <Product> productList : products.entrySet()) {
            for (int i = 0; i < productList.size(); i++){
                if (productList.get(i).getId() == id) {
                    return productList.get(i);
                }
            }
        }*/
        for (ArrayList<Product> productList : products.values()) {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId() == id) {
                    return productList.get(i);
                }
            }
        }
        return null;
    }
}
