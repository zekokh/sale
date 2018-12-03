package ru.zekoh.db.entity;

public class Product {

    //id
    private int id;

    //id для синхранизации с сервером статистики
    private int generalId;

    //Название продукта
    private String name;

    //Цена продукта
    private double price;

    //id родительского элемента
    private int parentId;

    //id классификатора
    private int classifierId;

    //Статус жизни
    private boolean isALive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getClassifierId() {
        return classifierId;
    }

    public void setClassifierId(int classifierId) {
        this.classifierId = classifierId;
    }

    public boolean isALive() {
        return isALive;
    }

    public void setALive(boolean ALive) {
        isALive = ALive;
    }
}
