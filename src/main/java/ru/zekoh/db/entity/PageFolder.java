package ru.zekoh.db.entity;

import java.util.ArrayList;
import java.util.List;

public class PageFolder {

    private int page = 1;
    private ArrayList<Folder> listFolders = new ArrayList<Folder>();

    public PageFolder(int page, ArrayList<Folder> listFolders){
        this.page = page;
        this.listFolders = listFolders;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Folder> getListFolders() {
        return listFolders;
    }

    public void setListFolders(ArrayList<Folder> listFolders) {
        this.listFolders = listFolders;
    }
}
