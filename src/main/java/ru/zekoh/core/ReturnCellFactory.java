package ru.zekoh.core;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import ru.zekoh.db.entity.CheckEntity;

public class ReturnCellFactory implements Callback<ListView<CheckEntity>, ListCell<CheckEntity>> {
    @Override
    public ListCell<CheckEntity> call(ListView<CheckEntity> listview) {
        return new ReturnCell();
    }
}

