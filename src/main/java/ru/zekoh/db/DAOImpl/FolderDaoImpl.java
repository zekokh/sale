package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.DAO.FolderDao;
import ru.zekoh.db.DAO.ProductDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Folder;
import ru.zekoh.db.entity.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderDaoImpl implements FolderDao {

    //Получить вcе активные папки
    @Override
    public List<Folder> getFolders() {

        //Список всех папок
        List<Folder> folders = new ArrayList<Folder>();



        //Получаем соединение с Базой Данных
        Connection connection = DataBase.getConnection();

        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM `product` WHERE folder = '1' AND live = '1';");
                while (rs.next()) {
                    //Создаем объект Product
                    Folder folder = new Folder();

                    folder.setId(rs.getInt(1));
                    folder.setName(rs.getString(2));
                    folder.setParentId(rs.getInt(5));
                    folder.setGeneralId(rs.getInt(6));
                    folder.setALive(rs.getBoolean(7));

                    //Добавляем папку в список папок
                    folders.add(folder);
                }
                connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return folders;
    }

    //Получить вcе активные папки отсортированные по уровню
    @Override
    public Map<Integer, ArrayList<Folder>> getFoldersSortedByLevel() {

        //Создаем список папок отсортированных по уровню
        Map<Integer, ArrayList<Folder>> folderListSortByLevel = new HashMap<Integer, ArrayList<Folder>>();

        //Создаем объект FolderDao для получения всех активных папок
        FolderDao folderDao = new FolderDaoImpl();

        //Создаем переменную для хранения списка всех активных папок для последующей сортировки
        List<Folder> folders = new ArrayList<Folder>();

        //Получаем список всех активных папок
        folders = folderDao.getFolders();

        //Сортируем папки по уровням
        for (int i = 0; i < folders.size(); i++) {

            int folderParentId = folders.get(i).getParentId();

            if (folderListSortByLevel.containsKey(folderParentId)) {
                folderListSortByLevel.get(folderParentId).add(folders.get(i));
            } else {
                ArrayList<Folder> folderList = new ArrayList<Folder>();
                folderList.add(folders.get(i));
                folderListSortByLevel.put(folderParentId, folderList);
            }
        }

        return folderListSortByLevel;
    }
}
