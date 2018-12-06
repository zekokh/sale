package ru.zekoh.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "product", schema = "sale")
public class DataEntity {
    private int id;
    private String shortName;
    private String fullName;
    private Double price;
    private int folder;
    private int parentId;
    private int generalId;
    private boolean live;
    private int classifier;
    private int serialNumber;
    private boolean unit;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "short_name")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "folder")
    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    @Basic
    @Column(name = "parent_id")
    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "general_id")
    public int getGeneralId() {
        return generalId;
    }

    public void setGeneralId(int generalId) {
        this.generalId = generalId;
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
    @Column(name = "classifier")
    public int getClassifier() {
        return classifier;
    }

    public void setClassifier(int classifier) {
        this.classifier = classifier;
    }

    @Basic
    @Column(name = "serial_number")
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Basic
    @Column(name = "unit")
    public boolean isUnit() {
        return unit;
    }

    public void setUnit(boolean unit) {
        this.unit = unit;
    }
}
