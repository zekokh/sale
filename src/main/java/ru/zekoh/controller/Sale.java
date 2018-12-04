package ru.zekoh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import ru.zekoh.core.GoodsCellFactory;
import ru.zekoh.db.Check;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.Data;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.db.entity.GoodsForDisplay;
import ru.zekoh.db.entity.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sale {

    private static Logger logger = LogManager.getLogger(Sale.class);

    // Список чеков
    private List<CheckObject> checkList = new ArrayList<CheckObject>();

    // Index текущего чека
    private int currentCheckIndex = 0;


    //Размер шрифта папок и продуктов
    Double fontFolderAndProduct = 16.0;

    //Переменная где содержится level для удобства доступа
    int levalProductForSerch = 0;

    //Считаем количество рядов папок и кнопок для расчета высоты panel
    int numberOfLinesForFolderAndProduct = 0;

    //Кол-во папок и продуктов
    int amountFolderAndProduct = 0;

    //Количество папок и продуктов в строке
    int countFolderAndProductInRow = 4;

    //Ширина кнопок с папками и продуктами
    int btnWigth = 145;

    //Высота кнопок с папками и продуктами
    int btnHight = 90;

    //
    private Goods tempGoods = null;

    //
    private int tempGoodsIndex = 0;

    //Товары для отображения в чеке (в UI ListView)
    private ObservableList<GoodsForDisplay> items;

    //Путь уровней вложенности папок и продуктов
    private ArrayList<Integer> levelPath = new ArrayList<Integer>();

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

    @FXML
    public void initialize() {

        //Инициализация 1 уровня вложенности папко
        levelPath.add(1);

        goodsListView.setCellFactory(new GoodsCellFactory());

        int level = levelPath.get(levelPath.size() - 1);
        panelForButtons.getChildren().add(getGrid(level));
    }

    public void leftSwipe(MouseEvent mouseEvent) {
    }

    public void addNewCheck(ActionEvent actionEvent) {
        createNewCheck();
    }

    private void reloadAll() {

        // Если 0 чеков, то очищать список товаров

        if (checkList.size() > 0) {

            CheckObject check = checkList.get(currentCheckIndex);

            // todo Проверить промоушены и акции

            // Рассчитать цену исходя из проджной
            calculationPrice(check);

            // Устанавливаем продажную цену
            sellingPrice.setText(generateRubleFromDouble(check.getSellingPrice()));

            // Устанавливаем цену без скидки
            whithoutDiscount.setText(generateRubleFromDouble(check.getSellingPrice()));

            // Устанавливаем сумму скидки
            Double tempAmountDiscount = check.getAmountByPrice() - check.getSellingPrice();
            amountDiscount.setText(generateRubleFromDouble(tempAmountDiscount));

            // Устанавливаем сумму бонусов которыми клиент планирует оплатить часть покупки
            amountBonus.setText(generateRubleFromDouble(check.getAmountBonus()));

            //Меняем сумму чека в табе для чеков
            Button btn = (Button) panelForCheckBtns.getChildren().get(currentCheckIndex);
            btn.setText(generateRubleFromDouble(check.getSellingPrice()));


            List<GoodsForDisplay> goodsForDisplayList = convert(check.getGoodsList());
            items = FXCollections.observableArrayList(goodsForDisplayList);
            goodsListView.setItems(items);
            goodsListView.refresh();

            // Оставить выделение пока товар активен для редактирвоания
            if (tempGoods != null) {
                goodsListView.getSelectionModel().select(tempGoodsIndex);
            }else {
                goodsListView.getSelectionModel().clearSelection();
            }

            // Скрол к последнему товару в чеке
            if (check.getGoodsList().size() > 10) {
                goodsListView.scrollTo(goodsForDisplayList.size() - 1);
            }

        } else {
            clearAllUI();
        }
    }

    // Создаем строку с постфиксом р. из числа
    private String generateRubleFromDouble(Double price) {
        return "" + price + " р.";
    }

    // Рассчитываем итоговые суммы чека на основе содержащихся товарных позиций
    private void calculationPrice(CheckObject check) {
        if (check.getGoodsList().size() > 0) {

            Double tempSellingPrice = 0.0;
            Double tempAmountByPrice = 0.0;


            for (Goods good : check.getGoodsList()) {

                // Итоговая цена по которой отпускается чек
                good.setSellingPrice(good.getCount() * good.getPriceAfterDiscount());
                tempSellingPrice += good.getSellingPrice();

                // Итоговая цена без скидок по прайсу
                Double temp = good.getCount() * good.getPriceFromThePriceList();
                tempAmountByPrice += temp;
            }

            tempSellingPrice = new BigDecimal(tempSellingPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
            tempAmountByPrice = new BigDecimal(tempAmountByPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();

            // Устанавливаем в чеке продажную цену
            check.setSellingPrice(tempSellingPrice);

            // Устанавливаем в чеке итоговую сумму без скидок
            check.setAmountByPrice(tempAmountByPrice);

        } else {
            check.setSellingPrice(0.0);
            check.setAmountByPrice(0.0);
            check.setAmountBonus(0.0);
        }
    }

    // Очищаем весь графический интерфей
    private void clearAllUI() {

        // Очищаем список товаров в чеке
        goodsListView.getItems().clear();
        goodsListView.refresh();

        // Очищаем панель с итоговыми показателями за весь чек
        sellingPrice.setText("");
        amountDiscount.setText("");
        amountBonus.setText("");
        whithoutDiscount.setText("");

        // Очищаем level Path
        levelPath.clear();
        levelPath.add(1);

        //удаляем старую Grid
        panelForButtons.getChildren().clear();

        //заполняем элементами
        panelForButtons.getChildren().add(getGrid(1));
    }

    //Конвертирует GoodsList в лист для отображении в ListView
    public List<GoodsForDisplay> convert(List<Goods> goods) {

        // Сортируем товары по id что при удалении они не скакали по ListView а с самого начала лежали рядом друг с другом
       // goods.sort(Comparator.comparingDouble(Goods::getProductId)
        //        .reversed());

        //Список товаров для вывода на дисплей
        List<GoodsForDisplay> goodsForDisplayList = new ArrayList<GoodsForDisplay>();


        for (int i = 0; i < goods.size(); i++) {

            int temp = checkContain(goodsForDisplayList, goods.get(i));

            if (temp == -1) {

                //Создаем новый объект goods для отображения в ListView
                GoodsForDisplay goodsForDisplay = new GoodsForDisplay();

                //Добавляем id продукта
                goodsForDisplay.setProductId(goods.get(i).getProductId());

                //Добавляем имя продука
                goodsForDisplay.setName(goods.get(i).getProductName());

                //Добавляем количетсво товара
                goodsForDisplay.setCount(goods.get(i).getCount());

                //Цена по прайсу
                goodsForDisplay.setPriceFromThePriceList(goods.get(i).getPriceFromThePriceList());

                //Сумма за позици
                Double sellingPriceDouble = new BigDecimal(goods.get(i).getSellingPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsForDisplay.setSellingPrice(sellingPriceDouble);

                //Цена после скидки
                goodsForDisplay.setPriceAfterDiscount(goods.get(i).getPriceAfterDiscount());

                //Добавляем объект goodsForDisplay в список объектов
                goodsForDisplayList.add(goodsForDisplay);
            } else {

                //Временная переменная для удобной работы с позицией в чеке которая уже есть для увелечения ее показателей
                GoodsForDisplay goodsTemp = goodsForDisplayList.get(temp);

                //Количество
                Double count = goodsTemp.getCount() + goods.get(i).getCount();
                goodsTemp.setCount(count);

                //Продажная цена
                Double sellingPrice = goodsTemp.getSellingPrice() + (goods.get(i).getCount() * goods.get(i).getSellingPrice());
                Double sellingPriceDouble = new BigDecimal(sellingPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsTemp.setSellingPrice(sellingPriceDouble);


            }
        }

        return goodsForDisplayList;
    }

    //МЕтод который проверяет наличие в Листе и возвращает
    public int checkContain(List<GoodsForDisplay> goodsList, Goods goods) {

        //Проверяем что бы в нашем объекте что было иначе восприним как создание нового продукта в пустом листе
        if (goodsList.size() > 0) {
            for (int i = 0; i < goodsList.size(); i++) {

                //Если продукт уже есть в списке продуктов
                if (goods.getProductId() == goodsList.get(i).getProductId()) {

                    //Возвращаем индекс в листе
                    return i;
                }
            }
        }


        //Если в листе для отображения пользователю нет такого товара отправляем 0
        return -1;
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

    private Pane getGrid(int level) {
        int x = 0;
        int y = 0;

        //Переменная где содержится level для удобства доступа
        levalProductForSerch = level;

        //Считаем количество строк в панеле с папками и продуктами
        numberOfLinesForFolderAndProduct = 0;

        //Кол-во папок
        int countFolders = 0;

        if (Data.folders.containsKey(level) && Data.folders.get(level).size() > 0) {
            countFolders = Data.folders.get(level).size();
        }

        //Кол-во продуктов
        int countProduct = 0;

        if (Data.products.containsKey(level) && Data.products.get(level).size() > 0) {
            countProduct = Data.products.get(level).size();
        }

        amountFolderAndProduct = countFolders + countProduct;

        numberOfLinesForFolderAndProduct = amountFolderAndProduct / countFolderAndProductInRow;
        int tempDivis = amountFolderAndProduct % countFolderAndProductInRow;

        if (tempDivis > 0 || level > 1) {
            numberOfLinesForFolderAndProduct++;
        }

        int temp = (numberOfLinesForFolderAndProduct * btnHight) + 10;

        panelForButtons.setPrefSize(680, temp);

        GridPane gridPane = new GridPane();


        //Кнопка - папка назад
        if (levelPath.size() > 1) {
            Button back_button = new Button("назад");
            back_button.setPrefSize(btnWigth, btnHight);
            back_button.setWrapText(true);
            back_button.setFont(new Font(fontFolderAndProduct));
            back_button.setId("btn");


            //Вешаем слушатель на кнопку поднимающую на уровень выше
            back_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    //Удалаяем последний уровень из переменной хранящей уровни вложенности
                    levelPath.remove(levelPath.size() - 1);

                    //Определяем превдивущий уровень вложенности
                    int levelBack = levelPath.size() - 1;

                    //Удаляем старую Grid
                    panelForButtons.getChildren().clear();

                    //Заполняем элементами
                    panelForButtons.getChildren().add(getGrid(levelPath.get(levelBack)));
                }
            });

            //Добавляем кнопку на Grid
            gridPane.add(back_button, x * (x + (int) back_button.getWidth()), y);
            x++;
            if (x % 9 == 0) {
                y++;
                x = 0;
            }

        }

        //Если есть папки отрисовываем папки
        if (Data.folders.containsKey(level) && Data.folders.get(level).size() > 0) {
            Button[] btns = new Button[Data.folders.get(level).size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(Data.folders.get(level).get(i).getName());
                btns[i].setPrefSize(btnWigth, btnHight);
                btns[i].setWrapText(true);
                btns[i].setFont(new Font(fontFolderAndProduct));
                btns[i].setId(String.valueOf(Data.folders.get(level).get(i).getId()));
                btns[i].setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#EEEEEE"), CornerRadii.EMPTY, Insets.EMPTY)));
                btns[i].setBorder(new Border(new BorderStroke(Color.valueOf("#E0E0E0"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            //Добавление кнопок в Grid
            for (Button b : btns) {


                // тут надо повесить обработчик кнопок
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {


                        b.setBackground(new Background(new BackgroundFill(
                                Color.valueOf("#E0E0E0"), CornerRadii.EMPTY, Insets.EMPTY)));
                        levelPath.add(Integer.parseInt(b.getId()));

                        //удаляем старую Grid
                        panelForButtons.getChildren().clear();
                        //заполняем элементами
                        panelForButtons.getChildren().add(getGrid(Integer.parseInt(b.getId())));


                    }
                });

                //Скрываем корневую папку
                if (Integer.parseInt(b.getId()) != 1) {
                    gridPane.add(b, x * (x + (int) b.getWidth()), y);
                    x++;
                    if (x % countFolderAndProductInRow == 0) {
                        y++;
                        x = 0;
                    }
                }

            }

        }

        //Если есть продукты отрисовываем продукты
        if (Data.products.containsKey(level) && Data.products.get(level).size() > 0) {
            Button[] btns = new Button[Data.products.get(level).size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(Data.products.get(level).get(i).getShortName());
                btns[i].setId(Data.products.get(level).get(i).getId() + "");
                btns[i].setPrefSize(btnWigth, btnHight);
                btns[i].setFont(new Font(fontFolderAndProduct));
                btns[i].setWrapText(true);
                // btns[i].setId(String.valueOf(Data.getProductsSortedByLevel().get(level).get(i).getId()));
                btns[i].setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));
                btns[i].setBorder(new Border(new BorderStroke(Color.valueOf("#B3E5FC"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            //Добавление кнопок в Grid
            for (Button b : btns) {

                //b.setId("product");
                //Обработчик продукта
                //нажатие
                b.setOnMousePressed(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        //Проверяем есть ли чек
                        if (checkList.size() > 0) {

                            //Проевряем открыт ли чек или уже оплачен
                            if (checkList.get(currentCheckIndex).isLive()) {
                                b.setBackground(new Background(new BackgroundFill(
                                        Color.valueOf("#B3E5FC"), CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        }

                    }
                });

                //Действие когда кнопка отпущена
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        //Если чека нет создаем чек
                        if (checkList.size() > 0) {
                            addItemToGoods(b, level);
                        } else {
                            createNewCheck();
                            addItemToGoods(b, level);
                        }

                    }
                });

                b.getId();
                gridPane.add(b, x * (x + (int) b.getWidth()), y);
                x++;
                if (x % countFolderAndProductInRow == 0) {
                    y++;
                    x = 0;
                }
            }

        }
        return gridPane;
    }

    private void addItemToGoods(Button b, int level) {
        if (checkList.get(currentCheckIndex).isLive()) {


            //Добавляем время при первом добавлении товара в чек
            if (checkList.get(currentCheckIndex).getGoodsList().size() == 0) {

                //Дата создания чека считается с добавления первого товара в чек
                Date date = new Date();
                String currentDate = "" + date.getTime() / 1000 + "";
                checkList.get(currentCheckIndex).setDateOfCreation(currentDate);

            }

            //Добавляем товар в чек
            CheckObject check = checkList.get(currentCheckIndex);

            //Ищем продукт по id
            Product product = Data.getProductById(Integer.parseInt(b.getId()), level);


            // todo отобразить экран для ввода кол-во продукции если товар весовой
            check.getGoodsList().add(new Goods(product.getId(), product.getGeneralId(), product.getShortName(), product.getClassifierId(), 1.0, product.getPrice(), product.getPrice(), product.getPrice(), product.isUnit()));

            // Проверка на скидки и оновление всех данных
            reloadAll();

            b.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));

        }
    }

    private void createNewCheck() {
        if (checkList.size() <= 9) {

            CheckObject checkObject = new CheckObject();
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

    // Клик по списку товаров
    public void itemAction(MouseEvent mouseEvent) {


        if (checkList.size() > 0) {
            if (checkList.get(currentCheckIndex).getGoodsList().size() > 0) {

                CheckObject check = checkList.get(currentCheckIndex);

                //Находим выбранный товар
                List<GoodsForDisplay> currentGoods = goodsListView.getSelectionModel().getSelectedItems();
                tempGoodsIndex = goodsListView.getSelectionModel().getSelectedIndex();

                //Если удалось определить конкретный товар
                if (currentGoods.size() > 0) {

                    //Найти товар в списке товаров внутри чека по id
                    for (Goods goods : check.getGoodsList()) {

                        if (goods.getProductId() == currentGoods.get(0).getProductId()) {

                            tempGoods = goods;

                            break;
                        }
                    }
                }
            }
        }
    }


    // Удалить товар из чека
    public void deleteItem(ActionEvent actionEvent) {

        // Если товар выбран
        if (tempGoods != null) {
            CheckObject check = checkList.get(currentCheckIndex);

            for (int i = 0; i < check.getGoodsList().size(); i++) {
                Goods goods = check.getGoodsList().get(i);

                double a = tempGoods.getPriceAfterDiscount();
                double b = goods.getPriceAfterDiscount();
                boolean isEq = Double.compare(a, b) == 0 ? true : false;
                if (tempGoods.getProductId() == goods.getProductId() && isEq) {
                    check.getGoodsList().remove(i);
                    i = -1;
                }

            }

            tempGoods = null;
            reloadAll();
        }
    }

    public void addItem(ActionEvent actionEvent) {

        // Если товар выбран
        if (tempGoods != null) {
            CheckObject check = checkList.get(currentCheckIndex);


            // Если товар штуный
            if (tempGoods.isUnit()) {

                for (int i = 0; i < check.getGoodsList().size(); i++) {
                    Goods goods = check.getGoodsList().get(i);
                    if (tempGoods.getProductId() == goods.getProductId()) {

                        Goods newGoods = new Goods(goods.getProductId(), goods.getGeneralId(), goods.getProductName(), goods.getClassifier(), goods.getCount(), goods.getPriceFromThePriceList(), goods.getPriceFromThePriceList(), goods.getPriceFromThePriceList(), goods.isUnit());
                        check.getGoodsList().add(newGoods);
                        break;
                    }

                }
            } else {
                for (int i = 0; i < check.getGoodsList().size(); i++) {
                    Goods goods = check.getGoodsList().get(i);

                    if (tempGoods.getProductId() == goods.getProductId() && tempGoods.getPriceAfterDiscount() == goods.getPriceAfterDiscount()) {

                        Goods currentGood = check.getGoodsList().get(i);
                        currentGood.setCount(currentGood.getCount() + 1);
                    }

                }
            }

            reloadAll();
        }
    }

    public void removeItem(ActionEvent actionEvent) {
        // Если товар выбран
        if (tempGoods != null) {
            CheckObject check = checkList.get(currentCheckIndex);


            // Если товар штуный
            if (tempGoods.isUnit()) {

                boolean flag = true;
                int count = 0;

                for (int i = check.getGoodsList().size()-1; i >= 0; i--) {
                    Goods goods = check.getGoodsList().get(i);

                    double a = tempGoods.getPriceAfterDiscount();
                    double b = goods.getPriceAfterDiscount();
                    boolean isEq = Double.compare(a, b) == 0 ? true : false;
                    if (tempGoods.getProductId() == goods.getProductId() && isEq) {


                       if(flag){
                           check.getGoodsList().remove(i);
                           flag = false;
                       }else {
                           count++;

                       }


                    }

                }

                if (count == 0){
                    tempGoods = null;
                }
                reloadAll();
            }
        }
    }
}
