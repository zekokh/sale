package ru.zekoh.db.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "synchronization", schema = "subtotalCenter")
public class Synchronize {
    private int id;
    private String dateCloseOfLastCheck;
    private boolean status;
    private int checkId;

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
    @Column(name = "date_close_last_check")
    public String getDateCloseOfLastCheck() {
        return dateCloseOfLastCheck;
    }

    public void setDateCloseOfLastCheck(String dateCloseOfLastCheck) {
        this.dateCloseOfLastCheck = dateCloseOfLastCheck;
    }


    @Basic
    @Column(name = "status")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Basic
    @Column(name = "check_id")
    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }
}
