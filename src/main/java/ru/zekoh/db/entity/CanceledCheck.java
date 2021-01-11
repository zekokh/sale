package ru.zekoh.db.entity;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
@Table(name = "canceled_check_list", schema = "center")
public class CanceledCheck {
    // Идентификатор записи
    private int id;
    // Идентификатор чека
    private int checkId;
    // Сумма по прайсу без скидки
    private Double amountByPrice;
    // Итоговая сумма к оплате со скидкой
    private Double total;
    // Дата создания чека
    private String dateOfCreation;
    // Дата отмены чека
    private String dateOfClosing;
    // Имя администратора
    private String administratorName;
    // id Администратора
    private int administratorId;
    // Статус синхронизации
    private boolean synchronised;
    // Статус существования записи
    private boolean live;

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
    @Column(name = "check_id")
    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
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
    @Column(name = "administrator_name")
    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    @Basic
    @Column(name = "administrator_id")
    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    @Basic
    @Column(name = "synchronised")
    public boolean isSynchronised() {
        return synchronised;
    }

    public void setSynchronised(boolean synchronised) {
        this.synchronised = synchronised;
    }

    @Basic
    @Column(name = "live")
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
