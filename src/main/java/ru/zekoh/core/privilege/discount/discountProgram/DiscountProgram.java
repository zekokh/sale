package ru.zekoh.core.privilege.discount.discountProgram;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

import java.util.List;

public interface DiscountProgram {

    // Собраны все актинвые скидки
    void applyDiscounts(CheckObject check, List<Goods> goodsList);
}
