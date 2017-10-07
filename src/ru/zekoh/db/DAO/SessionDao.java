package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Session;
import ru.zekoh.db.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface SessionDao {

    //Создать сессию
    public boolean createSession(User user);

    //Закрыть сесссию
    public boolean closeSession(User user);

    //Получить вcе открытые сессии
    public List<Session> getAllOpenSessions();

    //Завершить все открытые сессии
    public boolean closeAllOpenSessions();

    //Получить последнии открытый сеанс
    public Session getLastOpenSeesion();
}
