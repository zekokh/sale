package ru.zekoh.db.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.tools.corba.se.idl.constExpr.BooleanNot;

public class Session {

    //id
    private Long id;

    //User_id
    private Long userId;

    //Имя
    private String name;

    //id Роли
    private Long roleId;

    //Наименование роли
    private String role;

    //Дата и время создания сессии
    private String dateAndTimeOfSessionCreation;

    //Дата и время закрытия сессии
    private String dateAndTimeOfSessionClosing;

    //Статус жизни объекта
    private boolean isALive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateAndTimeOfSessionCreation() {
        return dateAndTimeOfSessionCreation;
    }

    public void setDateAndTimeOfSessionCreation(String dateAndTimeOfSessionCreation) {
        this.dateAndTimeOfSessionCreation = dateAndTimeOfSessionCreation;
    }

    public String getDateAndTimeOfSessionClosing() {
        return dateAndTimeOfSessionClosing;
    }

    public void setDateAndTimeOfSessionClosing(String dateAndTimeOfSessionClosing) {
        this.dateAndTimeOfSessionClosing = dateAndTimeOfSessionClosing;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isALive() {
        return isALive;
    }

    public void setALive(boolean ALive) {
        isALive = ALive;
    }
}
