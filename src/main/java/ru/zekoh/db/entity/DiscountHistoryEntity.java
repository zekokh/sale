package ru.zekoh.db.entity;

import javax.persistence.*;

@Entity
@Table(name = "discount_history", schema = "center")
public class DiscountHistoryEntity {

    private int id;
    private int checkId;
    private int employerId;
    private String date;

    // 1 - скидка с помощью карты скидочной
    // 2 - скидка с помощью приложения
    private int typeDiscount;

    public DiscountHistoryEntity(int checkId, int employerId, int typeDiscount, String date) {
        this.checkId = checkId;
        this.employerId = employerId;
        this.typeDiscount = typeDiscount;
        this.date = date;
    }

    public DiscountHistoryEntity() {
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "check_id")
    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    @Basic
    @Column(name = "employer_id")
    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    @Basic
    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "type_discount")
    public int getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(int typeDiscount) {
        this.typeDiscount = typeDiscount;
    }
}
