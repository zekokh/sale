package ru.zekoh.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "presents", schema = "center")
public class PresentEntity {
    private int id;
    private long presentId;
    private long customerId;
    private int typeId;
    private String typeName;
    private String description;
    private int dateLimit;
    private int dateUse;
    private String value;
    private boolean live = true;
    private boolean synchronise;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Basic
    @Column(name = "present_id")
    public long getPresentId() {
        return presentId;
    }

    public void setPresentId(long presentId) {
        this.presentId = presentId;
    }

    @Basic
    @Column(name = "customer_id")
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Basic
    @Column(name = "type_id")
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "date_limit")
    public int getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(int dateLimit) {
        this.dateLimit = dateLimit;
    }

    @Basic
    @Column(name = "date_use")
    public int getDateUse() {
        return dateUse;
    }

    public void setDateUse(int dateUse) {
        this.dateUse = dateUse;
    }

    @Basic
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @Basic
    @Column(name = "live")
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    @Basic
    @Column(name = "synchronise")
    public boolean isSynchronise() {
        return synchronise;
    }

    public void setSynchronise(boolean synchronise) {
        this.synchronise = synchronise;
    }
}
