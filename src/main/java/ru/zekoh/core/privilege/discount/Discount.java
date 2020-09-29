package ru.zekoh.core.privilege.discount;

public class Discount {
    private boolean applies = false;

    public boolean isApplied() {
        return applies;
    }

    public void setApplies(boolean applies) {
        this.applies = applies;
    }
}
