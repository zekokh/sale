package ru.zekoh.core.privilege.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TypeDiscount {
    // Уменьшение суммы на процент
    public static Double decreaseInPercentage(Double total, Double percent) {
        Double result = total - (total * percent / 100);
        // Округилить
        result = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue();
        // В случаи если сумма получилась отрицательная то передаем ноль, так как цена не может быть отрицательной
        if (result < 0.0){
            result = 0.0;
        }
        return result;
    }

    // Уменьшение суммы на сумму
    public static Double decreaseInAmount(Double total, Double amount) {
        Double result = total - amount;
        // Округилить
        result = new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue();

        // В случаи если сумма получилась отрицательная то передаем ноль, так как цена не может быть отрицательной
        if (result < 0.0){
            result = 0.0;
        }
        return result;
    }
}
