package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Synchronize;

public interface SynchronizeDao {

    // Записываем чек в таблицу синхронищации
    public boolean create(Synchronize synchronize);

    public Synchronize getLast();
}
