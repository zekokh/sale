package ru.zekoh.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "check", schema = "center")
public class CheckEntity {
    private int id;
    private Double amountByPrice;
    private Double total;
    private Integer typeOfPayment;
    private String dateOfCreation;
    private String dateOfClosing;
    private Boolean returnStatus;
    private Double payWithBonus;
    private Long dateOfClosingUnix;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "amount_by_price")
    public Double getAmountByPrice() {
        return amountByPrice;
    }

    public void setAmountByPrice(Double amountByPrice) {
        this.amountByPrice = amountByPrice;
    }

    @Basic
    @Column(name = "total")
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Basic
    @Column(name = "type_of_payment")
    public Integer getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(Integer typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    @Basic
    @Column(name = "date_of_creation")
    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Basic
    @Column(name = "date_of_closing")
    public String getDateOfClosing() {
        return dateOfClosing;
    }

    public void setDateOfClosing(String dateOfClosing) {
        this.dateOfClosing = dateOfClosing;
    }

    @Basic
    @Column(name = "return_status")
    public Boolean getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(Boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

    @Basic
    @Column(name = "pay_with_bonus")
    public Double getPayWithBonus() {
        return payWithBonus;
    }

    public void setPayWithBonus(Double payWithBonus) {
        this.payWithBonus = payWithBonus;
    }

    @Basic
    @Column(name = "date_of_closing_unix")
    public Long getDateOfClosingUnix() {
        return dateOfClosingUnix;
    }

    public void setDateOfClosingUnix(Long dateOfClosingUnix) {
        this.dateOfClosingUnix = dateOfClosingUnix;
    }
}
