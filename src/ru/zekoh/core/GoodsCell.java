package ru.zekoh.core;

import javafx.scene.control.ListCell;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.db.entity.GoodsForDisplay;

public class GoodsCell extends ListCell<GoodsForDisplay>
{
    @Override
    public void updateItem(GoodsForDisplay item, boolean empty)
    {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (item == null || empty)
        {
        }
        else
        {
            name = item.getName() + " - " +
                    item.getCount()+ " * "+
                    item.getPriceFromThePriceList()+" руб. " + " = " + item.getSellingPrice() + " руб.";
        }

        this.setText(name);
        setGraphic(null);
    }
}