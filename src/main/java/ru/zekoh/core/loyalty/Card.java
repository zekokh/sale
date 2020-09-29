package ru.zekoh.core.loyalty;

import ru.zekoh.db.entity.Present;

import java.util.List;

public class Card {
    // Флаг наличия у клиента подарков
    private boolean existPresents = false;

    // Подарки
    private List<Present> presents;

    public boolean isExistPresents() {
        return existPresents;
    }

    public List<Present> getPresents() {
        return presents;
    }

    public void setPresents(List<Present> presents) {
        if (presents != null) {
            existPresents = true;
        }else {
            existPresents = false;
        }
        this.presents = presents;
    }
}
