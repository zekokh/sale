package ru.zekoh.db.DAO;

import ru.zekoh.db.Check;
import ru.zekoh.db.entity.DiscountForEmployees;

public interface DiscountForEmployeesDao {

    //Получить скидочную карту по id
    public DiscountForEmployees getDiscountCard(int numberCard);

    //Запись в бд данных о скидки
    public boolean passed(Check check, DiscountForEmployees discountForEmployees);
}
