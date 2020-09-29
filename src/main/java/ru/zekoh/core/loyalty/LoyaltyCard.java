package ru.zekoh.core.loyalty;

import ru.zekoh.db.CheckObject;

public interface LoyaltyCard {
    long getId();
    String getName();
    void usePrivileges(CheckObject checkObject);
    boolean saveInDB();
    boolean pushOnServer();
    boolean isExistPresents();
    String getInfoText();
    Double getBalance();
}
