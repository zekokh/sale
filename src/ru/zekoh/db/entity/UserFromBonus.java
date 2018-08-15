package ru.zekoh.db.entity;

public class UserFromBonus {
    String mail;
    long id;
    Double disclount;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getDisclount() {
        return disclount;
    }

    public void setDisclount(Double disclount) {
        this.disclount = disclount;
    }
}
