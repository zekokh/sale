package ru.zekoh.core.loyalty;

import ru.zekoh.db.entity.Present;

import java.util.List;

public interface StoreCard {
    boolean isExistPresents();
    void setPresents(List<Present> presentList);
    void setExistPresents(boolean existPresents);
    List<Present> getPresents();
}
