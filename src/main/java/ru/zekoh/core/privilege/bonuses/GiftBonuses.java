package ru.zekoh.core.privilege.bonuses;

import ru.zekoh.core.privilege.Privilege;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

// Бонусы которыми можно оплатить полную стоимость
// товаров собственного производство
public class GiftBonuses implements Bonus, Privilege {
    // Количество бонусов
    private Double amountOfBonuses = 0.0;
    // Дата и время до которого можно воспользоваться бонусами
    private Integer dateLimit;
    // Описание за что были начислины бонусы
    private String description;

    public GiftBonuses(Double amountOfBonuses, Integer dateLimit, String description) {
        this.amountOfBonuses = amountOfBonuses;
        this.dateLimit = dateLimit;
        this.description = description;
    }

    @Override
    public Double getAmountOfBonuses() {
        return amountOfBonuses;
    }

    @Override
    public Integer getDateLimit() {
        return dateLimit;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void pay(CheckObject check) {
        //todo логика применения конкретного типа бонусов

        Double balanceOfBonuses = amountOfBonuses;
        // Обнуляем товары пока не закончаться бонусы
        for (Goods good : check.getGoodsOfOwnProduction()) {
            // Если сумма стоимости продукта меньше чем остаток бонусов
            if (good.getSellingPrice() <= balanceOfBonuses) {
                // обнуляем стоимость продукта
                // Продажная цена (sellingPrice) рассчитывается автоматически
                good.setPriceAfterDiscount(0.0);
                balanceOfBonuses -= good.getSellingPrice();
            } else {
                // вычитаем остаток из стоимости
                // Продажная цена (sellingPrice) рассчитывается автоматически
                Double priceAfterDiscount = good.getPriceAfterDiscount();
                good.setPriceAfterDiscount(priceAfterDiscount-balanceOfBonuses);
                balanceOfBonuses -= good.getSellingPrice();
            }

            // Если закончились бонусы прекращаем перебирать ...
            // todo В минус не может уходит надо подумать над корректностью
            if (balanceOfBonuses <= 0.0) {
                break;
            }
        }
    }
}
