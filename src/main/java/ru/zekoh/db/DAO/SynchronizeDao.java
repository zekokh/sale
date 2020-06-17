package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.Synchronize;

public interface SynchronizeDao {

    // Записываем чек в таблицу синхронищации
    public Synchronize create(Synchronize synchronize);

    public Synchronize getLast();
}
