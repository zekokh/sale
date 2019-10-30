package ru.zekoh.core.DiscountProgram;

import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Goods;

import java.util.List;

public interface DiscountInterface {

    // Собраны все актинвые скидки
    void applyDiscounts(CheckObject check, List<Goods> goodsList);
}
