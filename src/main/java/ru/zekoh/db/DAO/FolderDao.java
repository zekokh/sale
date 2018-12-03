package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FolderDao {

    //Получить вcе активные папки
    public List<Folder> getFolders();

    //Получить вcе активные папки отсортированные по уровню
    public Map<Integer, ArrayList<Folder>> getFoldersSortedByLevel();
}
