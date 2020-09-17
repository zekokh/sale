package ru.zekoh.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.stjs.javascript.dom.Pre;
import ru.zekoh.core.loyalty.StoreCard;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.entity.Discount;
import ru.zekoh.db.entity.DiscountForEmployees;
import ru.zekoh.db.entity.Present;
import ru.zekoh.db.entity.UserFromBonus;

import java.util.List;

import static java.lang.Math.toIntExact;

public class PresentsPane {
    private StoreCard storeCard;

    public PresentsPane(StoreCard storeCard) {
        this.storeCard = storeCard;
    }

    public GridPane generatePane() {

        // Создать массив кнопок с подарком
        GridPane gridPane = new GridPane();
        Button[] btns = new Button[storeCard.getPresents().size()];
        int x = 0;
        int y = 0;
        for (int i = 0; i < storeCard.getPresents().size(); i++) {
            btns[i] = new Button(storeCard.getPresents().get(i).getName_type_present() + "\n" + storeCard.getPresents().get(i).getDescription());
            btns[i].setPrefSize(280, 160);
            btns[i].setWrapText(true);
            btns[i].setFont(new Font(17));
            btns[i].setId("" + storeCard.getPresents().get(i).getId() + "");
            btns[i].setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#FFF5E1"), CornerRadii.EMPTY, Insets.EMPTY)));
            btns[i].setBorder(new Border(new BorderStroke(Color.valueOf("#faefd9"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        for (Button b : btns) {

            b.setOnMousePressed(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    b.setBackground(new Background(new BackgroundFill(
                            Color.valueOf("#B3E5FC"), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });

            //Действие когда кнопка отпущена
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    b.setBackground(new Background(new BackgroundFill(
                            Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });


            gridPane.add(b, x * (x + (int) b.getWidth()), y);
            x++;
            if (x % 4 == 0) {
                y++;
                x = 0;
            }
        }
        return gridPane;
    }

    public StoreCard getStoreCard() {
        return storeCard;
    }

    public void setStoreCard(StoreCard storeCard) {
        this.storeCard = storeCard;
    }
}
