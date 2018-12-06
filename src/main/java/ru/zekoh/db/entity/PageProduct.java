package ru.zekoh.db.entity;

import java.util.ArrayList;
import java.util.List;

public class PageProduct {
    private int page = 1;
    private List<Product> listProducts = new ArrayList<Product>();

    public PageProduct(int page, List<Product> listProducts){
        this.page = page;
        this.listProducts = listProducts;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Product> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<Product> listProducts) {
        this.listProducts = listProducts;
    }
}
