package ru.zekoh.db.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "check_list", schema = "center")
public class CheckEntity {
    private int id;
    private Double amountByPrice;
    private Double total;
    private Integer typeOfPayment;
    private String dateOfCreation;
    private String dateOfClosing;
    private Boolean returnStatus;
    private Double payWithBonus;
    private String dateOfClosingUnix;

    private List<GoodsEntity> goodsEntities;

    public CheckEntity() {
        goodsEntities = new ArrayList<>();
    }

    public void addGoods(GoodsEntity goodsEntity) {
        goodsEntity.setCheckEntity(this);
        goodsEntities.add(goodsEntity);
    }

    @OneToMany(targetEntity = GoodsEntity.class, mappedBy = "checkEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GoodsEntity> getGoodsEntity() {
        return goodsEntities;
    }

    public void setGoodsEntity(List<GoodsEntity> goodsEntity) {
        this.goodsEntities = goodsEntity;
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
    public String getDateOfClosingUnix() {
        return dateOfClosingUnix;
    }

    public void setDateOfClosingUnix(String dateOfClosingUnix) {
        this.dateOfClosingUnix = dateOfClosingUnix;
    }

}
