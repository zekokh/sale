package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.ProductDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Product;
import ru.zekoh.db.entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoImpl implements ProductDao{

    //Получить вcе активные продукты
    @Override
    public List<Product> getProducts() {

        //Список всех продуктов
        List<Product> products = new ArrayList<Product>();

        //Получаем соединение с Базой Данных
        Connection connection = DataBase.getConnection();

        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `product` WHERE folder = '0' AND live = '1';");
                while (rs.next()) {
                    //Создаем объект Product
                    Product product = new Product();

                    product.setId(rs.getInt(1));
                    product.setName(rs.getString(2));
                    product.setPrice(rs.getDouble(3));
                    product.setParentId(rs.getInt(5));
                    product.setGeneralId(rs.getInt(6));
                    product.setALive(rs.getBoolean(7));
                    product.setClassifierId(rs.getInt(8));

                    //Добавляем продукт в список продуктов
                    products.add(product);
                }
                connection.close();
            } catch (SQLException e) {
                return null;
            }
        }
        return products;
    }

    //Получить вcе активные продукты отсортированные по уровню
    @Override
    public Map<Integer, ArrayList<Product>> getProductsSortedByLevel() {

        //Создаем список продуктов отсортированных по уровню
        Map<Integer, ArrayList<Product>> productListSortByLevel = new HashMap<Integer, ArrayList<Product>>();

        //Создаем объект ProductDao для получения всех активных продуктов
        ProductDao productDao = new ProductDaoImpl();

        //Создаем переменную для хранения списка всех активных продуктов для последующей сортировки
        List<Product> products = new ArrayList<Product>();

        //Получаем список всех активных продуктов
        products = productDao.getProducts();

        //Сортируем продукты по уровням
        for (int i = 0; i < products.size(); i++) {

            int productParentId = products.get(i).getParentId();

            if (productListSortByLevel.containsKey(productParentId)) {
                productListSortByLevel.get(productParentId).add(products.get(i));
            } else {
                ArrayList<Product> productList = new ArrayList<Product>();
                productList.add(products.get(i));
                productListSortByLevel.put(productParentId, productList);
            }
        }

        return productListSortByLevel;
    }
}
