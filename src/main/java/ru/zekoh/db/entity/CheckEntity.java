package ru.zekoh.db.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "check", schema = "sale", catalog = "")
public class CheckEntity {
    private int id;
    private BigDecimal amountByPrice;
    private BigDecimal total;
    private byte paymentState;
    private Byte discountOnGoods;
    private Byte discountOnCheck;
    private Integer typeOfPayment;
    private Timestamp dateOfCreation;
    private Timestamp dateOfClosing;
    private Byte returnStatus;
    private Byte isALive;
    private Byte containGoods;
    private BigDecimal payWithBonus;
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
    public BigDecimal getAmountByPrice() {
        return amountByPrice;
    }

    public void setAmountByPrice(BigDecimal amountByPrice) {
        this.amountByPrice = amountByPrice;
    }

    @Basic
    @Column(name = "total")
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Basic
    @Column(name = "payment_state")
    public byte getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(byte paymentState) {
        this.paymentState = paymentState;
    }

    @Basic
    @Column(name = "discount_on_goods")
    public Byte getDiscountOnGoods() {
        return discountOnGoods;
    }

    public void setDiscountOnGoods(Byte discountOnGoods) {
        this.discountOnGoods = discountOnGoods;
    }

    @Basic
    @Column(name = "discount_on_check")
    public Byte getDiscountOnCheck() {
        return discountOnCheck;
    }

    public void setDiscountOnCheck(Byte discountOnCheck) {
        this.discountOnCheck = discountOnCheck;
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
    public Timestamp getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Timestamp dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    @Basic
    @Column(name = "date_of_closing")
    public Timestamp getDateOfClosing() {
        return dateOfClosing;
    }

    public void setDateOfClosing(Timestamp dateOfClosing) {
        this.dateOfClosing = dateOfClosing;
    }

    @Basic
    @Column(name = "return_status")
    public Byte getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(Byte returnStatus) {
        this.returnStatus = returnStatus;
    }

    @Basic
    @Column(name = "is_a_live")
    public Byte getIsALive() {
        return isALive;
    }

    public void setIsALive(Byte isALive) {
        this.isALive = isALive;
    }

    @Basic
    @Column(name = "contain_goods")
    public Byte getContainGoods() {
        return containGoods;
    }

    public void setContainGoods(Byte containGoods) {
        this.containGoods = containGoods;
    }

    @Basic
    @Column(name = "pay_with_bonus")
    public BigDecimal getPayWithBonus() {
        return payWithBonus;
    }

    public void setPayWithBonus(BigDecimal payWithBonus) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckEntity that = (CheckEntity) o;

        if (id != that.id) return false;
        if (paymentState != that.paymentState) return false;
        if (amountByPrice != null ? !amountByPrice.equals(that.amountByPrice) : that.amountByPrice != null)
            return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (discountOnGoods != null ? !discountOnGoods.equals(that.discountOnGoods) : that.discountOnGoods != null)
            return false;
        if (discountOnCheck != null ? !discountOnCheck.equals(that.discountOnCheck) : that.discountOnCheck != null)
            return false;
        if (typeOfPayment != null ? !typeOfPayment.equals(that.typeOfPayment) : that.typeOfPayment != null)
            return false;
        if (dateOfCreation != null ? !dateOfCreation.equals(that.dateOfCreation) : that.dateOfCreation != null)
            return false;
        if (dateOfClosing != null ? !dateOfClosing.equals(that.dateOfClosing) : that.dateOfClosing != null)
            return false;
        if (returnStatus != null ? !returnStatus.equals(that.returnStatus) : that.returnStatus != null) return false;
        if (isALive != null ? !isALive.equals(that.isALive) : that.isALive != null) return false;
        if (containGoods != null ? !containGoods.equals(that.containGoods) : that.containGoods != null) return false;
        if (payWithBonus != null ? !payWithBonus.equals(that.payWithBonus) : that.payWithBonus != null) return false;
        if (dateOfClosingUnix != null ? !dateOfClosingUnix.equals(that.dateOfClosingUnix) : that.dateOfClosingUnix != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (amountByPrice != null ? amountByPrice.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (int) paymentState;
        result = 31 * result + (discountOnGoods != null ? discountOnGoods.hashCode() : 0);
        result = 31 * result + (discountOnCheck != null ? discountOnCheck.hashCode() : 0);
        result = 31 * result + (typeOfPayment != null ? typeOfPayment.hashCode() : 0);
        result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
        result = 31 * result + (dateOfClosing != null ? dateOfClosing.hashCode() : 0);
        result = 31 * result + (returnStatus != null ? returnStatus.hashCode() : 0);
        result = 31 * result + (isALive != null ? isALive.hashCode() : 0);
        result = 31 * result + (containGoods != null ? containGoods.hashCode() : 0);
        result = 31 * result + (payWithBonus != null ? payWithBonus.hashCode() : 0);
        result = 31 * result + (dateOfClosingUnix != null ? dateOfClosingUnix.hashCode() : 0);
        return result;
    }
}
