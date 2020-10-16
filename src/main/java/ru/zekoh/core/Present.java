package ru.zekoh.core;

public class Present {
    long id;
    int type;
    String typeName;
    String description;
    int dateLimit;
    long customerId;
    String value;
    boolean applicable = false;

    public Present(long id, int type, String typeName, String description, int dateLimit, long customerId, String value) {
        this.id = id;
        this.type = type;
        this.typeName = typeName;
        this.description = description;
        this.dateLimit = dateLimit;
        this.customerId = customerId;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    public int getDateLimit() {
        return dateLimit;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getValue() {
        return value;
    }

    public boolean isApplicable() {
        return applicable;
    }

    public void setApplicable(boolean applicable) {
        this.applicable = applicable;
    }
}
