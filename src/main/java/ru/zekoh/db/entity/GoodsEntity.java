package ru.zekoh.db.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zekoh.db.Check;

import javax.persistence.*;

@Entity
@Table(name = "goods", schema = "center")
public class GoodsEntity {

    private int id;


    private CheckEntity checkEntity;

    private int generalId;
    private int productId;
    private String productName;
    private Double quantity;
    private Double priceFromThePriceList;
    private Double priceAfterDiscount;
    private Double sellingPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id")
    public CheckEntity getCheckEntity() {
        return checkEntity;
    }

    public void setCheckEntity(CheckEntity checkEntity) {
        this.checkEntity = checkEntity;
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
    @Column(name = "product_id")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "quantity")
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quntity) {
        this.quantity = quntity;
    }

    @Basic
    @Column(name = "price_from_the_price_list")
    public Double getPriceFromThePriceList() {
        return priceFromThePriceList;
    }

    public void setPriceFromThePriceList(Double priceFromThePriceList) {
        this.priceFromThePriceList = priceFromThePriceList;
    }

    @Basic
    @Column(name = "price_after_discount")
    public Double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(Double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    @Basic
    @Column(name = "selling_price")
    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
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
}
