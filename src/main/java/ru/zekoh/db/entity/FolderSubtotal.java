package ru.zekoh.db.entity;

public class FolderSubtotal {
    //id
    private int id;
    // Название продукта
    private String name;
    // Группа id
    private int folder_id;

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

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }
}
