package ru.zekoh.core;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import ru.zekoh.db.entity.GoodsForDisplay;

public class GoodsCellFactory implements Callback<ListView<GoodsForDisplay>, ListCell<GoodsForDisplay>> {
    @Override
    public ListCell<GoodsForDisplay> call(ListView<GoodsForDisplay> listview) {
        return new GoodsCell();
    }
}

