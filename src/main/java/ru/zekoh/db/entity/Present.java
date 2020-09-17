package ru.zekoh.db.entity;

// Подарок
public class Present {
    // id подарка в системе
    int id;
    // id Клиента
    int customer_id;
    // Тип подарка
    // 1 - Оплата всего чека бонусами
    // 2 - Забрать конкретный продукт
    // 3 - Скидка на какой-то конкретный продукт
    int type_present;
    // Название типа подарка
    String name_type_present;
    // Значение
    String value;
    // Описание
    String description;
    // Ограничения по времени
    int date_limit_unix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getType_present() {
        return type_present;
    }

    public void setType_present(int type_present) {
        this.type_present = type_present;
    }

    public String getName_type_present() {
        return name_type_present;
    }

    public void setName_type_present(String name_type_present) {
        this.name_type_present = name_type_present;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDate_limit_unix() {
        return date_limit_unix;
    }

    public void setDate_limit_unix(int date_limit_unix) {
        this.date_limit_unix = date_limit_unix;
    }
}
