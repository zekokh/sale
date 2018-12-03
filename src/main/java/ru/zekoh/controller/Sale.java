package ru.zekoh.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {

    private static Logger logger = LogManager.getLogger(Sale.class);

    // Список чеков
    private List<CheckObject> checkList = new ArrayList<CheckObject>();

    // Index текущего чека
    private int currentCheckIndex = 0;


    //Размер шрифта папок и продуктов
    Double fontFolderAndProduct = 16.0;

    // Панель для табов чеков
    public HBox panelForCheckBtns;

    // ListView со списком продуктов в чеке
    public ListView goodsListView;

    // Кнопка создания нового чека
    public Button newCheckBtn;

    // Панель с клавиатурой для рассчета сдачи
    public Pane panelWithNumberForCash;

    // К оплате на панели рассчета сдачи
    public Label countLabelForCash;

    // Сумма которую дал клиент
    public Label moneyFromCustomerLabel;

    // Сдача
    public Label cashBackToCustomerLabel;

    // Панель с кнопками для ввода кол-во товара
    public Pane panelWithNumber;

    // Панель с кнопками управления
    public Pane panelWithControlBtn;

    // Кнопка закрытия чека после печати
    public Button closeCheckBtn;

    // Лейбл количества
    public Label countLabel;

    // Лейбл для информирования по проблемам скидки для сотрудника
    public Label discountInfoLabel;

    //Панель для кнопок
    public Pane panelForButtons;

    // Продажная цена
    public Label sellingPrice;

    // Сумма скидки
    public Label amountDiscount;

    // Сумма бонусов
    public Label amountBonus;

    // Сумма без скидки
    public Label whithoutDiscount;

    // Панель скидок
    public Pane bonusPane;

    // Кнопка закрытия скидок
    public Button closeBonusPaneBtn;

    public void leftSwipe(MouseEvent mouseEvent) {
    }

    public void addNewCheck(ActionEvent actionEvent) {
        if(checkList.size() <= 9) {

            CheckObject checkObject = new CheckObject();

            Date date = new Date();
            String currentDate = ""+date.getTime() / 1000+"";
            checkObject.setDateOfCreation(currentDate);
            checkList.add(checkObject);
            currentCheckIndex = (checkList.size() - 1);

            //Создаем кнопку с нужными параметрами и делаем ее активной
            Button button = new Button();
            String temp = (currentCheckIndex) + "";
            button.setId(temp);
            button.setText(checkObject.getSellingPrice() + " р.");
            button.setPrefSize(90, 60);
            button.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#BBDEFB"), CornerRadii.EMPTY, Insets.EMPTY)));
            button.setBorder(new Border(new BorderStroke(Color.valueOf("#BBDEFB"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            //Обработчик событий
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentCheckIndex = Integer.parseInt(button.getId());
                    deselectAllTabsForCheckExcept(currentCheckIndex);
                    reloadAll();
                }
            });

            //Добавляем кнопку на на панель для табов чеков
            panelForCheckBtns.getChildren().add(button);

            //Индекс текщего таба с которого не нужно убирать выделение
            int index = panelForCheckBtns.getChildren().size() - 1;

            // Метод делает неактивные все табы чеков кроме переданного номера
            deselectAllTabsForCheckExcept(index);

            reloadAll();

        }
    }

    private void reloadAll() {

        // Если 0 чеков, то очищать список товаров
    }

    // Метод делает неактивные все табы чеков кроме переданного номера
    private void deselectAllTabsForCheckExcept(int index) {
        for (int i = 0; i < panelForCheckBtns.getChildren().size(); i++) {
            if (index == i) {
                Button btn = (Button) panelForCheckBtns.getChildren().get(i);
                btn.setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#BBDEFB"), CornerRadii.EMPTY, Insets.EMPTY)));
                btn.setFont(new Font(fontFolderAndProduct));
            } else {
                Button btn = (Button) panelForCheckBtns.getChildren().get(i);
                btn.setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#E3F2FD"), CornerRadii.EMPTY, Insets.EMPTY)));
                btn.setFont(new Font(fontFolderAndProduct));
            }
        }
    }

    public void kbrd_1(ActionEvent actionEvent) {
    }

    public void kbrd_2(ActionEvent actionEvent) {
    }

    public void kbrd_3(ActionEvent actionEvent) {
    }
    public void kbrd_4(ActionEvent actionEvent) {
    }

    public void kbrd_5(ActionEvent actionEvent) {
    }

    public void kbrd_6(ActionEvent actionEvent) {
    }

    public void kbrd_7(ActionEvent actionEvent) {
    }

    public void kbrd_8(ActionEvent actionEvent) {
    }
    
    public void kbrd_9(ActionEvent actionEvent) {
    }

    public void kbrd_0(ActionEvent actionEvent) {
    }

    public void kbrd_dote(ActionEvent actionEvent) {
    }
    
    public void removeLastSymbol(ActionEvent actionEvent) {
    }

    public void switchToControlPanel(ActionEvent actionEvent) {
    }

    public void payCash(ActionEvent actionEvent) {
    }

    public void payCard(ActionEvent actionEvent) {
    }

    public void cancelCheck(ActionEvent actionEvent) {
        //Если есть хотя бы один созданный чек
        if (checkList.size() > 0) {

            checkList.remove(currentCheckIndex);

            if (checkList.size() > 0) {


                int index = checkList.size() - 1;

                currentCheckIndex = (index);
                panelForCheckBtns.getChildren().remove(index);
                for (int i = 0; i < panelForCheckBtns.getChildren().size(); i++) {
                    panelForCheckBtns.getChildren().get(i).setId(i + "");
                }
                deselectAllTabsForCheckExcept(currentCheckIndex);

                reloadAll();

            } else {
                panelForCheckBtns.getChildren().clear();
                currentCheckIndex = 0;
                reloadAll();
            }
        }


    }

    public void exit(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            Stage stage = new Stage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/ModalInfoWindow.fxml"));
                stage.setTitle("Оплата");
                stage.setMinHeight(150);
                stage.setMinWidth(300);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.show();
            } catch (Exception e) {
                logger.error(e.toString());
            }

        } else {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/view/MenuWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }

    public void closeCheck(ActionEvent actionEvent) {
    }

    public void cardDiscount(ActionEvent actionEvent) {
    }

    public void appDiscount(ActionEvent actionEvent) {
    }

    public void deleteItem(ActionEvent actionEvent) {
    }

    public void addItem(ActionEvent actionEvent) {
    }

    public void removeItem(ActionEvent actionEvent) {
    }

    public void kbrd_1_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_2_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_3_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_4_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_5_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_6_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_7_cash(ActionEvent actionEvent) {
    }
    
    public void kbrd_8_cash(ActionEvent actionEvent) {
    }

    public void kbrd_9_cash(ActionEvent actionEvent) {
    }

    public void kbrd_0_cash(ActionEvent actionEvent) {
    }


    public void kbrd_dote_cash(ActionEvent actionEvent) {
    }

    public void removeLastSymbol_cash(ActionEvent actionEvent) {
    }

    public void payCashOnKeyBrd(ActionEvent actionEvent) {
    }

    public void cashСancellation(ActionEvent actionEvent) {
    }

    public void closeBonusPaneAction(ActionEvent actionEvent) {
    }
}
