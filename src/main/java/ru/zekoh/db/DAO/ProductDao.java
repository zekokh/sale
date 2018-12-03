package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Product;
import ru.zekoh.db.entity.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    //Получить вcе активные продукты
    public List<Product> getProducts();

    //Получить вcе активные продукты отсортированные по уровню
    public Map<Integer, ArrayList<Product>> getProductsSortedByLevel();
}
