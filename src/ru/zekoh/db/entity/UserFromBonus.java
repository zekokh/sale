package ru.zekoh.db.entity;

public class UserFromBonus {
    String mail;
    long id;
    Double disclount;
    Double bonus;
    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

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
