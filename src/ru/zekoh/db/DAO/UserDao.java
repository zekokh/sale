package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.User;

import java.util.List;

public interface UserDao {

    //Получить всех пользователей
    public List<User> getAllUsers();

    //Получить пользователя по его логину
    public User getUserByLogin(String login);

    //Получить пользователя по его id
    public User getUserById(Long id);

    //Получить пользователей по их статусу
    public List<User> getUsersByLiveStatus(boolean status);

}
