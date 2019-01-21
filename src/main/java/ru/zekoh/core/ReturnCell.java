package ru.zekoh.core;

import javafx.scene.control.ListCell;
import javafx.scene.text.Font;
import ru.zekoh.db.entity.CheckEntity;
import ru.zekoh.db.entity.GoodsForDisplay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnCell extends ListCell<CheckEntity> {

    @Override
    public void updateItem(CheckEntity item, boolean empty) {
        super.updateItem(item, empty);

        int index = this.getIndex();
        String name = null;

        // Format name
        if (item == null || empty) {
        } else {

            long unixSeconds = Long.valueOf(item.getDateOfClosing()).longValue();
            // convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds * 1000L);
            // the format of your date
            //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            // give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
            String formattedDate = sdf.format(date);

            String pay = "карта";
            if(item.getTypeOfPayment() == 1){
                pay = "наличными";
            }

            name = formattedDate+"  "+
                    item.getTotal() + "р.  " +
                    item.getAmountByPrice() + "р. "+pay;
        }

        this.setText(name);
        this.setFont(new Font(12.0));
        setGraphic(null);
    }
}
