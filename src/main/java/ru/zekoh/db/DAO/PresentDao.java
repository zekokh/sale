package ru.zekoh.db.DAO;

import ru.zekoh.db.entity.PresentEntity;

public interface PresentDao {
    //Создаем подарок
    public boolean save(Long presentId, long customerId, int typeId, String typeName, String description, int dateLimit, int dateUse, String value, boolean synchronise);
    // Получить подарок по id
    public PresentEntity getPresentEntity(Long presentId);
}
