package ru.zekoh.db.entity;

public class Folder {

    //id
    private int id;

    //Название папки
    private String name;

    //id родительского элемента
    private int parentId;

    //id для синхранизации с сервером статистики
    private int generalId;

    //Статус жизни
    private boolean isALive;

    // Доступ к папке только администратора
    private boolean administrativeAccess;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
    }

    public boolean isALive() {
        return isALive;
    }

    public void setALive(boolean ALive) {
        isALive = ALive;
    }

    public boolean isAdministrativeAccess() {
        return administrativeAccess;
    }

    public void setAdministrativeAccess(boolean administrativeAccess) {
        this.administrativeAccess = administrativeAccess;
    }
}
