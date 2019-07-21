package ru.zekoh.controller;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.zekoh.core.DiscountProgram;
import ru.zekoh.core.GoodsCellFactory;
import ru.zekoh.db.CheckObject;
import ru.zekoh.db.DAO.DiscountForEmployeesDao;
import ru.zekoh.db.DAOImpl.CardDao;
import ru.zekoh.db.DAOImpl.DiscountForEmployeesDaoImpl;
import ru.zekoh.db.Data;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static java.lang.Math.toIntExact;

public class Sale {

    private static Logger logger = LogManager.getLogger(Sale.class);
    public Button libraBtn;

    // Налево при пагинации
    public Button rightPagination;

    // На право при пагинации
    public Button leftPagination;

    // Вверх по списку товаров
    public Button upInListView;

    // Вниз по списку товаров
    public Button downInListView;

    // Панель для поиска скидочной карты
    public Pane panelFindDiscount;

    // Текстовое поле для ввода номера карты
    public TextField numberDiscountCardTextField;

    // Справочный лейбл для инфы при поиски скидочной карты
    public Label labelForFindDiscount;

    // Кнопка подтверждения выбора пользователя со скидкой
    public Button discountOkBtn;

    // Кнопка отмены выбора пользователя со скидкой
    public Button discountCancelBtn;

    // Панель с клавиатурой для посика пользователя
    public GridPane panelWithBtnForDiscountCard;
    public Button findBtnForDiacountCard;
    public Button cancelBtnForDiacountCard;

    // Кнопка добавления сикдки
    public Button discountBtn;

    // Label с заголовком на панели поиска пользователя со скидкой
    public Label discountTitle;

    // Панель для отображения пользователей из мобильного приложения
    public Pane panelForApp;

    // Кнопка для отображения panelForApp
    public Button appBtn;

    // Панель с информацией о пользователе из мобильного приложения
    public Pane panelForAppWithInfo;

    // Инфо о пользователи из приложения
    public Label appLabelInfo;

    // Панель с пользователями из приложения
    public Pane panelWithUsersFromApp;

    // Количество страниц в уровне
    private int maxCurrenPages = 0;

    // Список чеков
    private List<CheckObject> checkList = new ArrayList<CheckObject>();

    // Index текущего чека
    private int currentCheckIndex = 0;

    // id пекарни
    int bakeryId = 1;


    //Размер шрифта папок и продуктов
    Double fontFolderAndProduct = 16.0;

    //Переменная где содержится level для удобства доступа
    int levalProductForSerch = 0;

    // Переменная пагинации
    int currentPage = 2;

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

    // Индекс товара в чеке при выделении
    private int tempGoodsIndex = 0;

    //
    private String countNotUnitProduct = "";

    // Продукт при вводе количества
    Product currentNotUnitProduct = null;

    // При вводе продукта отображается поверх клавиатуры
    public Label produtWhenInputCountLabel;

    // Флаг дробного значения для клавиатуры
    private boolean flagDoubleNumberForKbr = false;

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

    // Кнопка закрытия скидок
    public Button closeBonusPaneBtn;

    //
    public int ListViewPaginationIndex = 0;

    boolean flagDoubleNumber = false;

    boolean flagDoubleNumberForCash = false;

    @FXML
    public void initialize() {

        //Инициализация 1 уровня вложенности папко
        levelPath.add(1);

        // Добавляем изображение на кнопку
        Image imageDecline = new Image(getClass().getResourceAsStream("/img/libra-icon.png"));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(45);
        imageView.setFitHeight(40);
        imageView.setImage(imageDecline);
        libraBtn.setGraphic(imageView);

        // Добавляем изображение на кнопки вверх
        Image imageDeclineUp = new Image(getClass().getResourceAsStream("/img/arrow.png"));
        ImageView imageViewUp = new ImageView();
        imageViewUp.setFitWidth(40);
        imageViewUp.setFitHeight(35);
        imageViewUp.setRotate(imageViewUp.getRotate() - 180);
        imageViewUp.setImage(imageDeclineUp);
        upInListView.setGraphic(imageViewUp);

        // Добавляем изображение на кнопки вниз
        Image imageDeclineDown = new Image(getClass().getResourceAsStream("/img/arrow.png"));
        ImageView imageViewDown = new ImageView();
        imageViewDown.setFitWidth(40);
        imageViewDown.setFitHeight(35);
        imageViewDown.setRotate(imageViewUp.getRotate() - 180);
        imageViewDown.setImage(imageDeclineDown);
        downInListView.setGraphic(imageViewDown);

        // Скрываем кнопки перемещения по списку товаров
        upInListView.setVisible(false);
        downInListView.setVisible(false);

        goodsListView.setCellFactory(new GoodsCellFactory());

        int level = levelPath.get(levelPath.size() - 1);
        panelForButtons.getChildren().add(getGrid(level, 1));

        // Устанавливаем размер ячейки в списке товаров
        goodsListView.setFixedCellSize(48);
    }

    public void addNewCheck(ActionEvent actionEvent) {
        createNewCheck();
    }

    private void reloadAll() {

        // Если 0 чеков, то очищать список товаров

        if (checkList.size() > 0) {

            CheckObject check = checkList.get(currentCheckIndex);

            // todo Проверить промоушены и акции
            if (check.getGoodsList().size() > 0) {

                // Вернуть ценны по прайсу
                List<Goods> goods = check.getGoodsList();

                for (int i = 0; i < goods.size(); i++) {

                    goods.get(i).setPriceAfterDiscount(goods.get(i).getPriceFromThePriceList());
                    Double temp = goods.get(i).getPriceAfterDiscount() * goods.get(i).getCount();
                    temp = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    goods.get(i).setSellingPrice(temp);
                }

                if (check.isDiscountAccountExist()) {

                    if (check.getDiscount().getDiscountRole() == 4) {

                        // Вычесть из продажной цены сумму скидки на весь чек
                        //List<Goods> goods = check.getGoodsList();

                        for (int i = 0; i < goods.size(); i++) {

                            Double priceAfterDiscount = goods.get(i).getPriceFromThePriceList();



                            if (goods.get(i).getClassifier() == 19) {
                                priceAfterDiscount -= (priceAfterDiscount * 20.0) / 100;
                            } else {
                                priceAfterDiscount -= (priceAfterDiscount * check.getDiscount().getPercentDiscount()) / 100;
                            }

                            goods.get(i).setPriceAfterDiscount(priceAfterDiscount);
                        }
                    }
                } else {

                    if (check.getDiscount() != null) {

                        if (check.getDiscount().getDiscountRole() == 2) {

                            // Вычесть из продажной цены сумму скидки 20% на весь чек
                            // List<Goods> goods = check.getGoodsList();

                            for (int i = 0; i < goods.size(); i++) {

                                Double priceAfterDiscount = goods.get(i).getPriceFromThePriceList();

                                priceAfterDiscount -= priceAfterDiscount * 0.2;

                                goods.get(i).setPriceAfterDiscount(priceAfterDiscount);

                                Double temp = goods.get(i).getPriceAfterDiscount() * goods.get(i).getCount();
                                temp = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                goods.get(i).setSellingPrice(temp);
                            }

                            // Рассчитать цену исходя из проджной
                            // calculationPrice(check);

                            if (check.getDiscount().isPayWithBonus()) {

                                // Посчитать количество оплатой бонусами
                                payBonus(check);

                            }
                        } else if (check.getDiscount().getDiscountRole() == 1) {

                            // Если пользователь с ролью 1, обычный рядовой пользователь
                            if (check.getDiscount().isPayWithBonus()) {

                                // Посчитать количество оплатой бонусами
                                payBonus(check);

                            }
                        }
                    } else {

                        // Вернуть ценны по прайсу
                        //List<Goods> goods = check.getGoodsList();

                        for (int i = 0; i < goods.size(); i++) {

                            goods.get(i).setPriceAfterDiscount(goods.get(i).getPriceFromThePriceList());
                        }

                        //  amountBonus.setText("");
                    }
                }


                // Промоушены
                if (check.getDiscount() == null) {

                    // Вернуть ценны по прайсу
                    //List<Goods> goods = check.getGoodsList();

                    for (int i = 0; i < goods.size(); i++) {

                        goods.get(i).setPriceAfterDiscount(goods.get(i).getPriceFromThePriceList());
                    }

                    CheckObject tempCheck = DiscountProgram.timeDiscount(check);
                    if (tempCheck == null) {

                        // 5 круаасан по цене 199р.
                        DiscountProgram.discountOnCountProductInCheck(check, 4, 5, 39.8);

                        DiscountProgram.onCroissant(check);

                        //DiscountProgram.cappuccinoAndCroissant(check);


                    }


                    DiscountProgram.onAchmaAndTea(check);

                    DiscountProgram.onBrioshAndTea(check);

                    // Акции которые не с чем не пересикаются
                    // 6 эклеров по цене 5
                    DiscountProgram.discountOnCountProductInCheck(check, 5, 6, 40.833);


                    // Флан натюр по кусочкам
                    //DiscountProgram.discountOnCountProductInCheck(check, 9, 8, 62.375);


                    // Флан кокос и чернослив
                    //DiscountProgram.discountOnCountProductInCheck(check, 6, 8, 81.125);


                    // Флан апельсин лимон ягодны шоколад
                    //DiscountProgram.discountOnCountProductInCheck(check, 16, 8, 93.625);

                    // Акция на панини комбо с 11:00 до 15:00
                   // DiscountProgram.initPaniniWithTimeLimit(check);

                }

            }

            // Рассчитать цену исходя из проджной
            calculationPrice(check);

            // Устанавливаем продажную цену
            sellingPrice.setText(generateRubleFromDouble(check.getSellingPrice()));

            // Устанавливаем цену без скидки
            whithoutDiscount.setText(generateRubleFromDouble(check.getAmountByPrice()));

            // Устанавливаем сумму скидки
            Double tempAmountDiscount = check.getAmountByPrice() - check.getSellingPrice();
            tempAmountDiscount = new BigDecimal(tempAmountDiscount).setScale(1, RoundingMode.HALF_UP).doubleValue();
            amountDiscount.setText(generateRubleFromDouble(tempAmountDiscount));

            // Устанавливаем сумму бонусов которыми клиент планирует оплатить часть покупки
            amountBonus.setText(generateRubleFromDouble(check.getAmountBonus()));

            //Меняем сумму чека в табе для чеков
            Button btn = (Button) panelForCheckBtns.getChildren().get(currentCheckIndex);
            btn.setText("" + check.getSellingPrice());


            List<GoodsForDisplay> goodsForDisplayList = convert(check.getGoodsList());
            items = FXCollections.observableArrayList(goodsForDisplayList);
            goodsListView.setItems(items);
            goodsListView.refresh();

            // Оставить выделение пока товар активен для редактирвоания
            if (tempGoods != null) {
                goodsListView.getSelectionModel().select(tempGoodsIndex);
            } else {
                goodsListView.getSelectionModel().clearSelection();
            }

            // Скрол к последнему товару в чеке
            if (goodsListView.getItems().size() > 8) {
                upInListView.setVisible(true);
                downInListView.setVisible(true);
                goodsListView.scrollTo(goodsForDisplayList.size() - 1);
                ListViewPaginationIndex = goodsForDisplayList.size() - 1;
            } else {
                ListViewPaginationIndex = 0;
                upInListView.setVisible(false);
                downInListView.setVisible(false);
            }


            if (!check.isPanelForFindDiscountCard() && panelFindDiscount.isVisible()) {
                panelForButtons.setVisible(true);
                panelFindDiscount.setVisible(false);
            } else if (check.isPanelForFindDiscountCard() && !panelFindDiscount.isVisible()) {
                panelForButtons.setVisible(false);
                panelFindDiscount.setVisible(true);

                numberDiscountCardTextField.clear();
                numberDiscountCardTextField.requestFocus();
            }

            if (panelFindDiscount.isVisible()) {
                // Если будет привязан к чеку надо отобразить инфу по скидке
                if (check.isDiscountAccountExist()) {
                    discountTitle.setText("К чеку применена скидика пользователя: ");
                    labelForFindDiscount.setText(check.getDiscount().getName());
                    switchDiscountUserPanel(true);
                } else {
                    discountTitle.setText("Введите номер карты для активации скидки");
                    labelForFindDiscount.setText("");
                    switchDiscountUserPanel(false);

                }
            }

            if (check.isDiscountAccountExist()) {
                discountBtn.setId("yellowBtnId");
            } else {
                discountBtn.setId("greyBtn");
            }


            // Правило отображение панели при скидки через приложение
            if (panelForApp.isVisible()) {
                if (check.isDiscountAppExist()) {

                    // Убрать панель с пользователями и оторазить выбранного пользователя
                    switchPanelAppToUserInfo(true);


                } else {
                    switchPanelAppToUserInfo(false);
                }
            }

            if (check.isDiscountAppExist()) {
                appBtn.setId("yellowBtnId");
            } else {
                appBtn.setId("greyBtn");
            }


            if (panelWithNumberForCash.isVisible()) {
                switchToCashKbr(false);
            }

        } else {
            clearAllUI();
        }
    }

    private void payBonus(CheckObject check) {

        List<Goods> goodsForBonus = check.getGoodsList();
        Double amountThatCanBePaidWithBonuses = 0.0;

        for (int i = 0; i < goodsForBonus.size(); i++) {

            Goods currentGoods = goodsForBonus.get(i);

            // Если это не багет, то можно оплатить бонусными баллами
            if (currentGoods.getClassifier() != 11) {
                amountThatCanBePaidWithBonuses = amountThatCanBePaidWithBonuses + (currentGoods.getCount() * currentGoods.getPriceAfterDiscount());
            }
        }

        amountThatCanBePaidWithBonuses = new BigDecimal(amountThatCanBePaidWithBonuses * 0.3).setScale(2, RoundingMode.HALF_UP).doubleValue();

        if (amountThatCanBePaidWithBonuses >= check.getDiscount().getBonus()) {
            check.setAmountBonus(check.getDiscount().getBonus());
        } else {
            check.setAmountBonus(amountThatCanBePaidWithBonuses);
        }


        if (check.getDiscount().isPayWithBonus()) {
            Double amountBonuses = check.getAmountBonus();
            List<Goods> goods = check.getGoodsList();
            for (int i = 0; i < check.getGoodsList().size(); i++) {
                if (amountBonuses == 0.0) {
                    return;
                }

                Double priceAfterDiscount = goods.get(i).getSellingPrice();
                if (amountBonuses >= priceAfterDiscount) {
                    goods.get(i).setPriceAfterDiscount(0.0);

                    goods.get(i).setSellingPrice(goods.get(i).getPriceAfterDiscount() * goods.get(i).getCount());
                    amountBonuses = amountBonuses - priceAfterDiscount;
                } else {

                    if (goods.get(i).isUnit()) {
                        goods.get(i).setPriceAfterDiscount(priceAfterDiscount - amountBonuses);
                        goods.get(i).setSellingPrice(goods.get(i).getPriceAfterDiscount() * goods.get(i).getCount());
                    } else {
                        Double temp = (priceAfterDiscount - amountBonuses) / goods.get(i).getCount();
                        goods.get(i).setPriceAfterDiscount(new BigDecimal(temp).setScale(5, RoundingMode.HALF_UP).doubleValue());
                        goods.get(i).setSellingPrice(priceAfterDiscount - amountBonuses);
                    }

                    amountBonuses = 0.0;
                }

            }
        }

    }

    // Переключение между панелью с информацией и панелью поиска пользователя из приложения
    private void switchPanelAppToUserInfo(boolean flag) {
        if (flag) {
            panelForApp.setVisible(false);
            panelForAppWithInfo.setVisible(true);

            appLabelInfo.setText(checkList.get(currentCheckIndex).getDiscount().getName());
        } else {
            panelForApp.setVisible(true);
            panelForAppWithInfo.setVisible(false);
            startSearchNewUsers();

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

        discountBtn.setId("greyBtn");
        appBtn.setId("greyBtn");

        // Очищаем level Path
        levelPath.clear();
        levelPath.add(1);

        //удаляем старую Grid
        panelForButtons.getChildren().clear();

        // Убираем оплату наличкой клавиатуру
        if (panelWithNumberForCash.isVisible()) {
            switchToCashKbr(false);
        }


        //заполняем элементами
        panelForButtons.getChildren().add(getGrid(1, 1));
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
                Double sellingPrice = goodsTemp.getCount() * goods.get(i).getPriceAfterDiscount();
                Double sellingPriceDouble = new BigDecimal(sellingPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
                goodsTemp.setSellingPrice(sellingPriceDouble);


            }
        }

        return goodsForDisplayList;
    }

    //Конвертирует GoodsList в лист для отображении в ListView
    public List<GoodsForDisplay> convertToCheck(List<Goods> goods) {

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

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }

    //МЕтод который проверяет наличие в Листе и возвращает
    public int checkContain(List<GoodsForDisplay> goodsList, Goods goods) {

        //Проверяем что бы в нашем объекте что было иначе восприним как создание нового продукта в пустом листе
        if (goodsList.size() > 0) {
            for (int i = 0; i < goodsList.size(); i++) {

                //Если продукт уже есть в списке продуктов
                if (goods.getProductId() == goodsList.get(i).getProductId() && areEqualDouble(goods.getPriceAfterDiscount(), goodsList.get(i).getPriceAfterDiscount(), 2)) {

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

    private void typeToKbrdCount(int number) {

        if (countNotUnitProduct.length() < 10000000)

            if (number != 0) {
                countNotUnitProduct += number;
            } else {
                if (countNotUnitProduct.length() > 0) {
                    countNotUnitProduct += number;
                }
            }
        countLabel.setText("" + countNotUnitProduct);
    }


    // Кнопка отображения количество введенного веса
    public void libraAction(ActionEvent actionEvent) {
        //currentNotUnitProduct = Data.getProductById(tempGoods.getProductId(), t);

        if (checkList.size() > 0) {
            if (checkList.get(currentCheckIndex).getGoodsList().size() > 0) {
                /*if (tempGoods != null) {
                    countNotUnitProduct = ""+tempGoods.getCount();
                    currentNotUnitProduct = Data.getProductById(tempGoods.getProductId(), tempGoods.getParentId());
                    displayScreenForEnteringProductQuantities(true);
                }*/
            }
        }
    }

    public void switchToControlPanel(ActionEvent actionEvent) {
        displayScreenForEnteringProductQuantities(false);
    }

    private void switchToCashKbr(boolean flag) {
        if (flag) {
            panelWithNumberForCash.setVisible(true);
            panelForButtons.setVisible(false);

            // Все что может быть под ним
            panelForAppWithInfo.setVisible(false);
            panelForApp.setVisible(false);
            panelFindDiscount.setVisible(false);

            // Очищаю переменные

            flagDoubleNumber = false;
            flagDoubleNumberForCash = false;
            cashBackToCustomerLabel.setText("Сдача: 0 р.");


        } else {
            moneyFromCustomerLabel.setText("");
            panelWithNumberForCash.setVisible(false);
            panelForButtons.setVisible(true);
        }
    }

    public void payCash(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            if (checkList.get(currentCheckIndex).getGoodsList().size() > 0) {
                if (!panelWithNumberForCash.isVisible()) {
                    switchToCashKbr(true);
                }
            }
        }
    }

    public void payCard(ActionEvent actionEvent) throws IOException {
        if (checkList.size() > 0) {
            if (checkList.get(currentCheckIndex).getGoodsList().size() > 0) {
                if (!panelWithNumberForCash.isVisible()) {
                    Stage dialog = new Stage();
                    dialog.initStyle(StageStyle.UNDECORATED);
                    dialog.setTitle("Жак-Андрэ Продажи");

                    Parent root = FXMLLoader.load(getClass().getResource("/view/PayCardWindow.fxml"));

                    dialog.setScene(new Scene(root, 700, 300));

                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();

                    dialog.initOwner(stage);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();


                    if (Properties.isPayCard) {
                        Properties.isPayCard = false;

                        pay(false, actionEvent);
                    }
                }
            }
        }
    }

    public void cancelCheck(ActionEvent actionEvent) throws IOException {
        if (checkList.size() > 0) {

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.setTitle("Жак-Андрэ Продажи");

            Parent root = FXMLLoader.load(getClass().getResource("/view/CancelModal.fxml"));

            dialog.setScene(new Scene(root, 700, 300));

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            dialog.initOwner(stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

            if (Properties.cancelModalView) {
                Properties.cancelModalView = false;
                cancelCheckMethod();
            }
        }

    }

    private void cancelCheckMethod() {
        //Если есть хотя бы один созданный чек
        if (checkList.size() > 0) {

            checkList.remove(currentCheckIndex);

            if (checkList.size() > 0) {


                int index = checkList.size() - 1;

                currentCheckIndex = (index);
                panelForCheckBtns.getChildren().remove(index);
                for (int i = 0; i < panelForCheckBtns.getChildren().size(); i++) {
                    panelForCheckBtns.getChildren().get(i).setId(i + "");

                    CheckObject checkObject = checkList.get(i);
                    Button b = (Button) panelForCheckBtns.getChildren().get(i);
                    b.setText("" + checkObject.getSellingPrice());
                }
                deselectAllTabsForCheckExcept(currentCheckIndex);

                reloadAll();

            } else {

                panelForCheckBtns.getChildren().clear();
                currentCheckIndex = 0;
                reloadAll();
            }

            if (panelFindDiscount.isVisible()) {
                panelFindDiscount.setVisible(false);
                panelForButtons.setVisible(true);
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
        if (checkList.size() > 0) {

            CheckObject checkObject = checkList.get(currentCheckIndex);
            if (!panelForApp.isVisible() && !panelWithNumberForCash.isVisible()) {

                panelForButtons.setVisible(false);
                panelFindDiscount.setVisible(true);

                if (checkObject.isDiscountAccountExist()) {
                    discountTitle.setText("К чеку применена скидика пользователя: ");
                    labelForFindDiscount.setText(checkList.get(currentCheckIndex).getDiscount().getName());
                    switchDiscountUserPanel(true);
                } else {
                    discountTitle.setText("Введите номер карты для активации скидки");
                    labelForFindDiscount.setText("");
                    switchDiscountUserPanel(false);


                    numberDiscountCardTextField.clear();
                    numberDiscountCardTextField.requestFocus();
                }
                checkList.get(currentCheckIndex).setPanelForFindDiscountCard(true);

            }

        }
    }

    public void appDiscount(ActionEvent actionEvent) {

        if (checkList.size() > 0) {

            if (!panelFindDiscount.isVisible() && !panelWithNumberForCash.isVisible()) {


                if (checkList.get(currentCheckIndex).isDiscountAppExist()) {
                    switchPanelAppToUserInfo(true);
                } else {


                    switchPanelForApp(true);

                    // Если к чеку прикреплен пользователей из приложения его надо отобразить

                    // Поиск пользователей в пекарне
                    startSearchNewUsers();


                    // Отображение пользователей в пекарне
                }

            }

        }
    }

    private void startSearchNewUsers() {
        panelWithUsersFromApp.getChildren().clear();
        // Поиск пользователей в пекарне
        try {
            getUsersFromApp();

            // Создаем новый поток который получает данные о пользователе и отрисовывает их
            Thread clientThread = new Thread(() -> {

                try {
                    while (panelForApp.isVisible()) {
                        Thread.sleep(1000);
                        Platform.runLater(() -> {
                            getUsersFromApp();
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
            clientThread.setDaemon(true);

            clientThread.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void getUsersFromApp() {
        try {
            String url = "http://5.188.41.134:8080/api/v1/bakery/" + bakeryId + "";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //System.out.println("ищем...");
            JSONArray myresponse = new JSONArray(response.toString());
            List<UserFromBonus> userFromBonusList = new ArrayList<UserFromBonus>();
            for (int i = 0; i < myresponse.length(); i++) {
                JSONObject json = new JSONObject(myresponse.get(i).toString());
                int payWithBonuses = json.getInt("bonus");
                UserFromBonus userFromBonus = new UserFromBonus();
                JSONObject customerInformation = (JSONObject) json.get("customer");

                userFromBonus.setId(customerInformation.getLong("id"));

                // Если приходит запрос с просьбой оплатить покупку бонусами, то сохраняем кол-во бонусов клиента
                if (payWithBonuses == 1) {
                    userFromBonus.setBonus(customerInformation.getDouble("bonus"));
                } else {

                    // Если оплата без бонусов, просто устанавливаем 0, тогда бонусы будут только начисляться с покупки
                    userFromBonus.setBonus(0.0);
                }

                String[] name = customerInformation.getString("mail").split("@");
                userFromBonus.setMail(name[0]);
                userFromBonus.setLevel(customerInformation.getInt("level"));
                userFromBonus.setRole(customerInformation.getInt("role"));

                //System.out.println("С бонусом или нет: " + json.getBoolean("bonus"));

                userFromBonus.setDisclount(customerInformation.getDouble("discount"));
                userFromBonusList.add(userFromBonus);
                //System.out.println("mail тут " + json.getString("mail"));
            }


            GridPane gridPane = new GridPane();
            Button[] btns = new Button[userFromBonusList.size()];
            int x = 0;
            int y = 0;
            for (int i = 0; i < userFromBonusList.size(); i++) {
                btns[i] = new Button(userFromBonusList.get(i).getMail());
                btns[i].setPrefSize(280, btnHight);
                btns[i].setWrapText(true);
                btns[i].setFont(new Font(fontFolderAndProduct));
                btns[i].setId("" + userFromBonusList.get(i).getId() + "");
                btns[i].setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#EEEEEE"), CornerRadii.EMPTY, Insets.EMPTY)));
                btns[i].setBorder(new Border(new BorderStroke(Color.valueOf("#E0E0E0"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            for (Button b : btns) {

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


                        if (checkList.get(currentCheckIndex).isLive()) {

                            if (checkList.size() > 0) {
                                if (checkList.get(currentCheckIndex).isLive()) {
                                    CheckObject check = checkList.get(currentCheckIndex);

                                    UserFromBonus currentUserFromBonus = new UserFromBonus();
                                    for (int i = 0; i < userFromBonusList.size(); i++) {
                                        long longId = Integer.parseInt(b.getId());
                                        if (userFromBonusList.get(i).getId() == toIntExact(longId)) {
                                            currentUserFromBonus = userFromBonusList.get(i);
                                        }
                                    }

                                    if (currentUserFromBonus != null) {
                                        DiscountForEmployees discountForEmployees = new DiscountForEmployees();
                                        //todo узкое место если long будет больше int жопка будет надо привести в порядок

                                        Discount discount = new Discount();

                                        // Устанавливаем что есть скидка


                                        // Если существует скидка по карте то отменяем ее
                                        if (check.isDiscountAccountExist()) {
                                            check.setDiscountAccountExist(false);
                                            check.setDiscount(null);
                                        }

                                        check.setDiscountAppExist(true);

                                        // Устанавливаем роль аккаунта
                                        discount.setDiscountRole(currentUserFromBonus.getRole());

                                        // Баланс
                                        discount.setBalance(0.0);

                                        // Бюджет
                                        discount.setBudget(9720445.73);

                                        // Прцент скидки
                                        discount.setPercentDiscount(currentUserFromBonus.getDisclount());

                                        // Имя пользователя со скидкой
                                        discount.setName(currentUserFromBonus.getMail());

                                        discount.setId(toIntExact(currentUserFromBonus.getId()));

                                        check.setDiscount(discount);


                                        discountForEmployees.setBonus(currentUserFromBonus.getBonus());
                                        if (currentUserFromBonus.getBonus() > 0) {
                                            discount.setPayWithBonus(true);
                                            discount.setBonus(currentUserFromBonus.getBonus());
                                        }
                                        discountForEmployees.setLevel(currentUserFromBonus.getLevel());

                                        reloadAll();

                                        //switchPanelForApp(false);

                                    }

                                }
                            }


                            b.setBackground(new Background(new BackgroundFill(
                                    Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));
                        }

                    }
                });


                gridPane.add(b, x * (x + (int) b.getWidth()), y);
                x++;
                if (x % countFolderAndProductInRow == 0) {
                    y++;
                    x = 0;
                }


            }

            panelWithUsersFromApp.getChildren().add(gridPane);

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    private void switchPanelForApp(boolean flag) {
        if (flag) {
            panelForApp.setVisible(true);
            panelForButtons.setVisible(false);

            // Если вдруг открыта панель выбора пользователя по карте
            panelFindDiscount.setVisible(false);
        } else {
            panelForApp.setVisible(false);
            panelForButtons.setVisible(true);
        }
    }

    public void payCashOnKeyBrd(ActionEvent actionEvent) {
        try {
            pay(true, actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cashСancellation(ActionEvent actionEvent) {
        switchToCashKbr(false);
    }

    // Метод возвращает папку по уровню и страницы
    private ArrayList<Folder> getFolders(int level, int page) {
        ArrayList<Folder> list = new ArrayList<Folder>();

        for (int i = 0; i < Data.arrayFolderMap.get(level).size(); i++) {
            if (Data.arrayFolderMap.get(level).get(i).getPage() == page) {
                list = Data.arrayFolderMap.get(level).get(i).getListFolders();
            }
        }
        return list;
    }

    // Метод возвращает продукты по уровню и страницы
    private ArrayList<Product> getProducts(int level, int page) {
        ArrayList<Product> list = new ArrayList<Product>();

        for (int i = 0; i < Data.arrayProductMap.get(level).size(); i++) {
            if (Data.arrayProductMap.get(level).get(i).getPage() == page) {
                list = (ArrayList<Product>) Data.arrayProductMap.get(level).get(i).getListProducts();
            }
        }
        return list;
    }

    private Pane getGrid(int level, int page) {
        int x = 0;
        int y = 0;

        int foldersMaxPage = 0;

        int productsMaxPage = 0;

        //Переменная где содержится level для удобства доступа
        levalProductForSerch = level;

        currentPage = page;

        //Считаем количество строк в панеле с папками и продуктами
        numberOfLinesForFolderAndProduct = 0;

        //Кол-во папок
        int countFolders = 0;

        if (Data.arrayFolderMap.containsKey(level)) {

            ArrayList<Folder> list = new ArrayList<Folder>();

            for (int i = 0; i < Data.arrayFolderMap.get(level).size(); i++) {
                if (Data.arrayFolderMap.get(level).get(i).getPage() == page) {
                    list = (ArrayList<Folder>) Data.arrayFolderMap.get(level).get(i).getListFolders();
                    countFolders = list.size();
                }
            }

        }

        //Кол-во продуктов
        int countProduct = 0;

        if (Data.arrayProductMap.containsKey(level)) {

            ArrayList<Product> list = new ArrayList<Product>();

            for (int i = 0; i < Data.arrayProductMap.get(level).size(); i++) {
                if (Data.arrayProductMap.get(level).get(i).getPage() == page) {
                    list = (ArrayList<Product>) Data.arrayProductMap.get(level).get(i).getListProducts();
                    countProduct = list.size();
                }
            }
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
                    panelForButtons.getChildren().add(getGrid(levelPath.get(levelBack), 1));
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
        boolean folderIsExist = false;

        if (Data.arrayFolderMap.containsKey(level)) {


            for (int i = 0; i < Data.arrayFolderMap.get(level).size(); i++) {
                if (Data.arrayFolderMap.get(level).get(i).getPage() == page) {
                    if (Data.arrayFolderMap.get(level).get(i).getListFolders().size() > 0) {
                        folderIsExist = true;
                    }
                }
            }
        }

        if (folderIsExist) {

            foldersMaxPage = Data.arrayFolderMap.get(level).size();
            ArrayList<Folder> folders = getFolders(level, page);
            Button[] btns = new Button[folders.size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(folders.get(i).getName());
                btns[i].setPrefSize(btnWigth, btnHight);
                btns[i].setWrapText(true);
                btns[i].setFont(new Font(fontFolderAndProduct));
                btns[i].setId(String.valueOf(folders.get(i).getId()));
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







                        // Реализиуем запароленную папку
                        // отображаем модальное окно с вводом пароля
                        // если все правильно то открываем папку, если нет ошибкаи остается все как есть
                        if(Integer.parseInt(b.getId()) == 370) {

                            // Отобразить модального окно
                            Stage dialog = new Stage();
                            dialog.initStyle(StageStyle.UNDECORATED);
                            dialog.setTitle("Жак-Андрэ Продажи");

                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("/view/PasswordEntryToAccessTheFolderWindow.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            dialog.setScene(new Scene(root, 600, 700));

                            Node source = (Node) event.getSource();
                            Stage stage = (Stage) source.getScene().getWindow();

                            dialog.initOwner(stage);
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.showAndWait();

                            if (Properties.correctPasswordEntryToAccessTheFolder) {

                                Properties.correctPasswordEntryToAccessTheFolder = false;

                                levelPath.add(Integer.parseInt(b.getId()));

                                //удаляем старую Grid
                                panelForButtons.getChildren().clear();
                                //заполняем элементами
                                panelForButtons.getChildren().add(getGrid(Integer.parseInt(b.getId()), 1));
                            }



                        }else {

                            b.setBackground(new Background(new BackgroundFill(
                                    Color.valueOf("#E0E0E0"), CornerRadii.EMPTY, Insets.EMPTY)));

                            levelPath.add(Integer.parseInt(b.getId()));

                            //удаляем старую Grid
                            panelForButtons.getChildren().clear();
                            //заполняем элементами
                            panelForButtons.getChildren().add(getGrid(Integer.parseInt(b.getId()), 1));
                        }



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

        //Если есть папки отрисовываем папки
        boolean productIsExist = false;

        if (Data.arrayProductMap.containsKey(level)) {


            for (int i = 0; i < Data.arrayProductMap.get(level).size(); i++) {
                if (Data.arrayProductMap.get(level).get(i).getPage() == page) {
                    if (Data.arrayProductMap.get(level).get(i).getListProducts().size() > 0) {
                        productIsExist = true;
                    }
                }
            }
        }


        if (productIsExist) {

            productsMaxPage = Data.arrayProductMap.get(level).size();
            ArrayList<Product> products = getProducts(level, page);
            Button[] btns = new Button[products.size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(products.get(i).getShortName());
                btns[i].setId(products.get(i).getId() + "");
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

                        //reloadAll();
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
        maxCurrenPages = productsMaxPage;
        if (maxCurrenPages < foldersMaxPage) {
            maxCurrenPages = foldersMaxPage;
        }

        // Пагинация
        if (maxCurrenPages > 1) {

            if (currentPage < maxCurrenPages && currentPage > 1) {
                rightPagination.setVisible(true);
                leftPagination.setVisible(true);

                rightPagination.setDisable(false);
                leftPagination.setDisable(false);
            } else if (currentPage < maxCurrenPages) {
                rightPagination.setVisible(true);
                rightPagination.setDisable(false);
                leftPagination.setVisible(true);
                leftPagination.setDisable(true);
            } else {
                leftPagination.setVisible(true);
                leftPagination.setDisable(false);

                rightPagination.setVisible(true);
                rightPagination.setDisable(true);
            }


        } else {
            rightPagination.setVisible(false);
            leftPagination.setVisible(false);
        }

        return gridPane;
    }

    private void addItemToGoods(Button b, int level) {
        if (checkList.get(currentCheckIndex).isLive()) {

            // Обнуляем выбанный товар до этого
            tempGoods = null;

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

            if (product.isUnit()) {

                check.getGoodsList().add(new Goods(product.getId(), product.getGeneralId(), product.getShortName(), product.getClassifierId(), 1.0, product.getPrice(), product.getPrice(), product.getPrice(), product.isUnit(), product.getParentId()));

                // Проверка на скидки и оновление всех данных
                reloadAll();

                b.setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));

            } else {

                currentNotUnitProduct = product;
                // Отобразить экран для ввода кол-во продукции если товар весовой
                displayScreenForEnteringProductQuantities(true);
            }

            reloadAll();
        }
    }

    private void displayScreenForEnteringProductQuantities(boolean value) {
        panelForButtons.setVisible(!value);
        panelWithNumber.setVisible(value);

        if (!value) {
            if (countNotUnitProduct.length() > 0) {
                addItemToGoods(currentNotUnitProduct, Double.parseDouble(countNotUnitProduct));
            }
            countNotUnitProduct = "";
        } else {
            produtWhenInputCountLabel.setText(currentNotUnitProduct.getShortName());
            countLabel.setText("" + countNotUnitProduct);
        }
    }

    public void cancelCountKbr(ActionEvent actionEvent) {
        countNotUnitProduct = "";
        displayScreenForEnteringProductQuantities(false);
    }

    private void addItemToGoods(Product product, Double count) {
        if (checkList.get(currentCheckIndex).isLive()) {

            // Обнуляем выбанный товар до этого
            tempGoods = null;

            //Добавляем время при первом добавлении товара в чек
            if (checkList.get(currentCheckIndex).getGoodsList().size() == 0) {

                //Дата создания чека считается с добавления первого товара в чек
                Date date = new Date();
                String currentDate = "" + date.getTime() / 1000 + "";
                checkList.get(currentCheckIndex).setDateOfCreation(currentDate);

            }

            //Добавляем товар в чек
            CheckObject check = checkList.get(currentCheckIndex);

            check.getGoodsList().add(new Goods(product.getId(), product.getGeneralId(), product.getShortName(), product.getClassifierId(), count, product.getPrice(), product.getPrice(), product.getPrice(), product.isUnit(), product.getParentId()));

            // Проверка на скидки и оновление всех данных
            reloadAll();

        }
    }

    private void createNewCheck() {
        if (checkList.size() <= 10) {

            // Обнуляем выбранный товар
            tempGoods = null;

            CheckObject checkObject = new CheckObject();
            checkList.add(checkObject);
            currentCheckIndex = (checkList.size() - 1);

            //Создаем кнопку с нужными параметрами и делаем ее активной
            Button button = new Button();
            String temp = (currentCheckIndex) + "";
            button.setId(temp);
            button.setStyle("-fx-font-size: 15");
            button.setText("" + checkObject.getSellingPrice());

            button.setPrefSize(99, 60);
            button.setBackground(new Background(new BackgroundFill(
                    Color.valueOf("#BBDEFB"), CornerRadii.EMPTY, Insets.EMPTY)));
            button.setBorder(new Border(new BorderStroke(Color.valueOf("#BBDEFB"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            //Обработчик событий
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Обнуляем выбранный товар
                    tempGoods = null;
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

        if (checkList.size() > 0) {
            CheckObject check = checkList.get(currentCheckIndex);

            // Если чек открыт
            if (check.isLive()) {

                // Если товар выбран
                if (tempGoods != null) {

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
        }
    }

    public void addItem(ActionEvent actionEvent) {

        if (checkList.size() > 0) {
            CheckObject check = checkList.get(currentCheckIndex);

            // Если чек открыт
            if (check.isLive()) {

                // Если товар выбран
                if (tempGoods != null) {


                    // Если товар штуный
                    if (tempGoods.isUnit()) {

                        for (int i = 0; i < check.getGoodsList().size(); i++) {
                            Goods goods = check.getGoodsList().get(i);
                            if (tempGoods.getProductId() == goods.getProductId()) {

                                Goods newGoods = new Goods(goods.getProductId(), goods.getGeneralId(), goods.getProductName(), goods.getClassifier(), goods.getCount(), goods.getPriceFromThePriceList(), goods.getPriceFromThePriceList(), goods.getPriceFromThePriceList(), goods.isUnit(), goods.getParentId());
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
        }
    }

    public void removeItem(ActionEvent actionEvent) {

        if (checkList.size() > 0) {
            CheckObject check = checkList.get(currentCheckIndex);

            // Если чек открыт
            if (check.isLive()) {

                // Если товар выбран
                if (tempGoods != null) {

                    // Если товар штуный
                    if (tempGoods.isUnit()) {

                        boolean flag = true;
                        int count = 0;
                        int tempCount = 0;

                        for (int i = check.getGoodsList().size() - 1; i >= 0; i--) {
                            Goods goods = check.getGoodsList().get(i);

                            double a = tempGoods.getPriceAfterDiscount();
                            double b = goods.getPriceAfterDiscount();
                            boolean isEq = Double.compare(a, b) == 0 ? true : false;

                            // В случаи если товары отличаются по цене но с одним id во время промоакции то выделение снимается,
                            // что не правильно. Поэтому добавляем такой вот костыль

                            if (tempGoods.getProductId() == goods.getProductId()) {
                                tempCount++;
                            }

                            if (tempGoods.getProductId() == goods.getProductId() && isEq) {

                                if (flag) {
                                    check.getGoodsList().remove(i);
                                    tempCount--;
                                    flag = false;
                                } else {
                                    count++;

                                }
                            }

                        }

                        if (count == 0) {
                            if (tempCount > 0) {
                                // todo зарефакторить надо бы
                            } else {
                                tempGoods = null;
                            }
                        }
                        reloadAll();
                    } else {

                        for (int i = check.getGoodsList().size() - 1; i >= 0; i--) {
                            Goods goods = check.getGoodsList().get(i);

                            double a = tempGoods.getPriceAfterDiscount();
                            double b = goods.getPriceAfterDiscount();
                            boolean isEq = Double.compare(a, b) == 0 ? true : false;
                            if (tempGoods.getProductId() == goods.getProductId() && isEq) {

                                Goods currentGood = check.getGoodsList().get(i);
                                if (currentGood.getCount() - 1 > 0) {
                                    currentGood.setCount(currentGood.getCount() - 1);
                                } else {
                                    tempGoods = null;
                                    check.getGoodsList().remove(i);
                                    break;
                                }

                            }
                        }

                        reloadAll();
                    }
                }
            }
        }
    }

    public void kbrd_1_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("1");
    }

    public void kbrd_2_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("2");
    }

    public void kbrd_3_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("3");
    }

    public void kbrd_4_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("4");
    }

    public void kbrd_5_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("5");
    }

    public void kbrd_6_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("6");
    }

    public void kbrd_7_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("7");
    }

    public void kbrd_8_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("8");
    }

    public void kbrd_9_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("9");
    }

    public void kbrd_0_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("0");
    }


    public void kbrd_dote_cash(ActionEvent actionEvent) {
        if (!flagDoubleNumber) {
            String countLabelString = moneyFromCustomerLabel.getText();
            moneyFromCustomerLabel.setText(countLabelString + ".");
            flagDoubleNumberForCash = true;
            reloadCashBack();
        }
    }

    public void removeLastSymbol_cash(ActionEvent actionEvent) {
        String data = moneyFromCustomerLabel.getText();
        if (data.length() <= 12) {
            if (data.length() > 0) {
                if (data.length() > 1) {
                    String mayBeDote = data.substring(data.length() - 1, data.length());
                    if (mayBeDote.equals(".")) {
                        flagDoubleNumberForCash = false;
                    }
                    data = data.substring(0, data.length() - 1);
                    moneyFromCustomerLabel.setText(data);
                } else {
                    moneyFromCustomerLabel.setText("0");
                }


            } else {
                clickOnKbrd("0");
            }
        }
        reloadCashBack();
    }

    public void clickOnKbrd(String value) {
        String countLabelString = countLabel.getText();
        if (countLabelString.equals("0")) {
            countLabel.setText(value);
        } else {
            countLabel.setText(countLabelString + value);
        }
    }

    public void reloadCashBack() {
        Double moneyFromCustomer = Double.parseDouble(moneyFromCustomerLabel.getText());
        Double cashBack = moneyFromCustomer - checkList.get(currentCheckIndex).getSellingPrice();
        cashBack = new BigDecimal(cashBack).setScale(2, RoundingMode.HALF_UP).doubleValue();
        cashBackToCustomerLabel.setText("Сдача: " + cashBack + " р.");
    }

    public void clickToBtnKbrdCash(String symbol) {
        String countLabelString = moneyFromCustomerLabel.getText();
        if (countLabelString.equals("0")) {
            moneyFromCustomerLabel.setText(symbol);
        } else {
            moneyFromCustomerLabel.setText(countLabelString + symbol);
        }
        reloadCashBack();
    }


    public void kbrd_1(ActionEvent actionEvent) {

        typeToKbrdCount(1);
    }

    public void kbrd_2(ActionEvent actionEvent) {
        typeToKbrdCount(2);
    }

    public void kbrd_3(ActionEvent actionEvent) {
        typeToKbrdCount(3);
    }

    public void kbrd_4(ActionEvent actionEvent) {

        typeToKbrdCount(4);
    }

    public void kbrd_5(ActionEvent actionEvent) {

        typeToKbrdCount(5);
    }

    public void kbrd_6(ActionEvent actionEvent) {

        typeToKbrdCount(6);
    }

    public void kbrd_7(ActionEvent actionEvent) {

        typeToKbrdCount(7);
    }

    public void kbrd_8(ActionEvent actionEvent) {

        typeToKbrdCount(8);
    }

    public void kbrd_9(ActionEvent actionEvent) {

        typeToKbrdCount(9);
    }

    public void kbrd_0(ActionEvent actionEvent) {
        typeToKbrdCount(0);
    }

    public void kbrd_dote(ActionEvent actionEvent) {
        if (!flagDoubleNumberForKbr) {

            countNotUnitProduct += ".";
            flagDoubleNumberForKbr = true;
            countLabel.setText(countNotUnitProduct);
        }
    }

    public void removeLastSymbol(ActionEvent actionEvent) {
        if (countNotUnitProduct.length() > 0) {
            if (countNotUnitProduct.length() > 0) {
                if (countNotUnitProduct.length() > 1) {
                    String mayBeDote = countNotUnitProduct.substring(countNotUnitProduct.length() - 1, countNotUnitProduct.length());
                    if (mayBeDote.equals(".")) {
                        flagDoubleNumberForKbr = false;
                    }
                    countNotUnitProduct = countNotUnitProduct.substring(0, countNotUnitProduct.length() - 1);
                    countLabel.setText(countNotUnitProduct);
                } else {
                    countNotUnitProduct = "";
                    countLabel.setText(countNotUnitProduct);
                }


            }

            countLabel.setText("" + countNotUnitProduct);
        }
    }

    public void rightPaginationAction(ActionEvent actionEvent) {
        if (currentPage < maxCurrenPages) {
            currentPage++;

            //удаляем старую Grid
            panelForButtons.getChildren().clear();
            //заполняем элементами
            panelForButtons.getChildren().add(getGrid(levelPath.get(levelPath.size() - 1), currentPage));
        }
    }

    public void leftPaginationAction(ActionEvent actionEvent) {
        if (currentPage > 1) {
            currentPage--;

            //заполняем элементами
            panelForButtons.getChildren().add(getGrid(levelPath.get(levelPath.size() - 1), currentPage));
        }
    }

    public void upListGoods(ActionEvent actionEvent) {
        if (goodsListView.getItems().size() > 8) {
            int temp = ListViewPaginationIndex - 8;
            if (temp >= 0) {
                goodsListView.scrollTo(temp);
                ListViewPaginationIndex = temp;
            } else {
                goodsListView.scrollTo(0);
                ListViewPaginationIndex = 0;
            }

        }
    }

    public void downListGoods(ActionEvent actionEvent) {
        if (goodsListView.getItems().size() > 8) {

            int temp = ListViewPaginationIndex + 8;
            if (temp < goodsListView.getItems().size()) {
                goodsListView.scrollTo(temp);
                ListViewPaginationIndex = temp;
            } else {
                goodsListView.scrollTo(goodsListView.getItems().size());
                ListViewPaginationIndex = goodsListView.getItems().size() - 1;
            }
        }
    }

    // Скрыть панель ввода скидочной карты
    public void discountHidePanel(ActionEvent actionEvent) {
        panelFindDiscount.setVisible(false);
        panelForButtons.setVisible(true);
        checkList.get(currentCheckIndex).setPanelForFindDiscountCard(false);

        discountTitle.setText("Введите номер карты для активации скидки");
        labelForFindDiscount.setText("");
    }

    public void kbrdDiscount_1(ActionEvent actionEvent) {
        typeToDiscountTextField("1");
    }

    public void kbrdDiscount_2(ActionEvent actionEvent) {
        typeToDiscountTextField("2");
    }

    public void kbrdDiscount_3(ActionEvent actionEvent) {
        typeToDiscountTextField("3");
    }

    public void kbrdDiscount_4(ActionEvent actionEvent) {
        typeToDiscountTextField("4");
    }

    public void kbrdDiscount_5(ActionEvent actionEvent) {
        typeToDiscountTextField("5");
    }

    public void kbrdDiscount_6(ActionEvent actionEvent) {
        typeToDiscountTextField("6");
    }

    public void kbrdDiscount_7(ActionEvent actionEvent) {
        typeToDiscountTextField("7");
    }

    public void kbrdDiscount_8(ActionEvent actionEvent) {
        typeToDiscountTextField("8");
    }

    public void kbrdDiscount_9(ActionEvent actionEvent) {
        typeToDiscountTextField("9");
    }

    public void kbrdDiscount_0(ActionEvent actionEvent) {
        typeToDiscountTextField("0");
    }

    public void kbrdDiscount_remove(ActionEvent actionEvent) {
        if (numberDiscountCardTextField.getLength() > 1) {
            numberDiscountCardTextField.setText(numberDiscountCardTextField.getText().substring(0, numberDiscountCardTextField.getLength() - 1));
        } else if (numberDiscountCardTextField.getLength() == 1) {
            numberDiscountCardTextField.setText(numberDiscountCardTextField.getText().substring(0, numberDiscountCardTextField.getLength() - 1));
            numberDiscountCardTextField.requestFocus();
        } else {
            numberDiscountCardTextField.requestFocus();
        }

        discountTitle.setText("Введите номер карты для активации скидки");
        labelForFindDiscount.setText("");
    }

    public void kbrdDiscount_clean(ActionEvent actionEvent) {
        if (numberDiscountCardTextField.getLength() > 0) {
            numberDiscountCardTextField.clear();
            numberDiscountCardTextField.requestFocus();

            discountTitle.setText("Введите номер карты для активации скидки");
            labelForFindDiscount.setText("");
        }
    }

    private void typeToDiscountTextField(String text) {
        numberDiscountCardTextField.setText(numberDiscountCardTextField.getText() + text);

        discountTitle.setText("Введите номер карты для активации скидки");
        labelForFindDiscount.setText("");
    }

    public void findMemberByCodeNumber(ActionEvent actionEvent) {

        // Отправить запрос на поиск
        if (numberDiscountCardTextField.getLength() > 0) {

            long numberMember = new Long(numberDiscountCardTextField.getText());
            DiscountForEmployeesDao discountForEmployeesDao = new DiscountForEmployeesDaoImpl();
            try {

                // Получаем список пользователей
                Session session = HibernateSessionFactory.getSessionFactory().openSession();

                List<DiscountCardEntity> card = session.createQuery("SELECT a FROM DiscountCardEntity a WHERE a.live = true and a.number = :number", DiscountCardEntity.class)
                        .setParameter("number", numberMember)
                        .getResultList();

                if (card.size() > 0) {

                    CheckObject check = checkList.get(currentCheckIndex);


                    // Если существует скидка по карте то отменяем ее
                    if (check.isDiscountAppExist()) {
                        check.setDiscountAppExist(false);
                        check.setDiscount(null);
                    }

                    // Устанавливаем что есть скидка
                    check.setDiscountAccountExist(true);

                    Discount discount = new Discount();

                    // Устанавливаем id
                    discount.setId(card.get(0).getId());

                    // Устанавливаем роль аккаунта
                    discount.setDiscountRole(card.get(0).getRole());

                    // Баланс
                    discount.setBalance(card.get(0).getBalance());

                    // Бюджет
                    discount.setBudget(card.get(0).getBudgetForTheMonth());

                    // Прцент скидки
                    discount.setPercentDiscount(card.get(0).getAmountOfDiscount());

                    // Имя пользователя со скидкой
                    discount.setName(card.get(0).getName());

                    check.setDiscount(discount);

                    discountTitle.setText("К чеку применена скидика пользователя: ");
                    labelForFindDiscount.setText(card.get(0).getName());

                    reloadAll();
                } else {

                    // Отображение ошибки

                    labelForFindDiscount.setText("Карта не найдена!");
                }
            } catch (Exception e) {
                logger.error(e.toString());
                labelForFindDiscount.setText("Карта не найдена!");
            }
        }

        // Обработать ответ

        // Отобразить ответ
    }

    // Переключатель при выборе пользователя со скидкой

    private void switchDiscountUserPanel(boolean flag) {
        if (flag) {
            // Отобразить кнопки ок и отмена и скрыть все остальное
            discountOkBtn.setVisible(true);
            discountCancelBtn.setVisible(true);

            numberDiscountCardTextField.setVisible(false);
            panelWithBtnForDiscountCard.setVisible(false);
            findBtnForDiacountCard.setVisible(false);
            cancelBtnForDiacountCard.setVisible(false);

            // Вслучаи если открыто другая
            panelForApp.setVisible(false);

        } else {
            discountOkBtn.setVisible(false);
            discountCancelBtn.setVisible(false);

            numberDiscountCardTextField.setVisible(true);
            panelWithBtnForDiscountCard.setVisible(true);
            findBtnForDiacountCard.setVisible(true);
            cancelBtnForDiacountCard.setVisible(true);

            numberDiscountCardTextField.clear();
            numberDiscountCardTextField.requestFocus();
        }
    }

    // Действие на нажатие кнопки ОК когда пользоватеь для скидки найден
    public void discountOkAction(ActionEvent actionEvent) {
        panelFindDiscount.setVisible(false);
        panelForButtons.setVisible(true);
        checkList.get(currentCheckIndex).setPanelForFindDiscountCard(false);

        discountTitle.setText("Введите номер карты для активации скидки");
        labelForFindDiscount.setText("");
    }

    // Удаления пользователя со скидкой из чека
    public void discountCancelAction(ActionEvent actionEvent) {
        discountTitle.setText("Введите номер карты для активации скидки");
        labelForFindDiscount.setText("");
        CheckObject checkObject = checkList.get(currentCheckIndex);
        checkObject.setDiscountAccountExist(false);
        checkObject.setDiscount(null);
        reloadAll();
    }

    public void closePanelForApp(ActionEvent actionEvent) {
        switchPanelForApp(false);
    }


    public void appCancelAction(ActionEvent actionEvent) {
        checkList.get(currentCheckIndex).setDiscountAppExist(false);
        checkList.get(currentCheckIndex).setDiscount(null);
        checkList.get(currentCheckIndex).setAmountBonus(0.0);
        switchPanelAppToUserInfo(false);
        reloadAll();
    }

    public void appOkAction(ActionEvent actionEvent) {
        switchPanelAppToUserInfo(false);
        switchPanelForApp(false);
    }

    // Метод проведения оплаты
    private void pay(boolean isCash, ActionEvent event) throws IOException {

        // Проверить все ли условия соблюдены
        if (checkList.size() > 0) {

            //Текущий чек
            CheckObject checkObject = checkList.get(currentCheckIndex);


            //Если выбранный чек еще существует
            if (checkObject.isLive() && !checkObject.isBlockForSale()) {

                //Если есть товары в чеке
                if (checkObject.getGoodsList().size() > 0) {


                    // todo костыль переделать

                    if (checkObject.getDiscount() != null && checkObject.getDiscount().getDiscountRole() == 4) {
                        // Проверяем лимит по балансу при оплате скидкой
                        if (checkObject.getDiscount().getBalance() + checkObject.getSellingPrice() > checkObject.getDiscount().getBudget()) {
                            // Отобразить модальное окно с инфой
                            Stage dialog = new Stage();
                            dialog.initStyle(StageStyle.UNDECORATED);
                            dialog.setTitle("Жак-Андрэ Продажи");

                            Parent root = FXMLLoader.load(getClass().getResource("/view/info.fxml"));

                            dialog.setScene(new Scene(root, 700, 220));

                            Node source = (Node) event.getSource();
                            Stage stage = (Stage) source.getScene().getWindow();

                            dialog.initOwner(stage);
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.showAndWait();


                            return;
                        }
                    }


                    // Сформировать чек

                    CheckEntity checkEntity = new CheckEntity();
                    checkEntity.setAmountByPrice(checkObject.getAmountByPrice());
                    checkEntity.setTotal(checkObject.getSellingPrice());
                    //checkEntity.setPaymentState();
                    //checkEntity.setDiscountOnGoods();
                    //checkEntity.setDiscountOnCheck();
                    if (isCash) {
                        checkEntity.setTypeOfPayment(1);
                        checkObject.setTypeOfPayment(1);
                    } else {
                        checkEntity.setTypeOfPayment(2);
                        checkObject.setTypeOfPayment(2);
                    }

                    checkEntity.setDateOfCreation(checkObject.getDateOfCreation());

                    //Дата создания чека считается с добавления первого товара в чек
                    Date date = new Date();
                    String currentDate = "" + date.getTime() / 1000 + "";
                    checkEntity.setDateOfClosing(currentDate);
                    checkObject.setDateOfClosing(currentDate);
                    //checkEntity.setDateOfClosingUnix();
                    checkEntity.setReturnStatus(false);
                    //checkEntity.setIsALive();
                    //checkEntity.setContainGoods();
                    checkEntity.setPayWithBonus(checkObject.getAmountBonus());

                    // Отправить на принтер
                    if (Properties.KKM) {

                        // Открыть модальное окно для печати чека
                        Properties.checkObject = checkObject;



                        Stage dialog = new Stage();
                        dialog.initStyle(StageStyle.UNDECORATED);
                        dialog.setTitle("Жак-Андрэ Продажи");

                        Parent root = FXMLLoader.load(getClass().getResource("/view/ModalWhilePrintCheck.fxml"));

                        dialog.setScene(new Scene(root, 700, 220));

                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();

                        dialog.initOwner(stage);
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.showAndWait();

                        if (Properties.statusPrinted) {
                       // if (true) {

                            System.out.println("Статуст успешной печати: " + Properties.statusPrinted);
                            System.out.println("Объект checkObkect сумма чека: " + Properties.checkObject.getAmountByPrice());
                            // if (true) {
                            // todo перед тем как класть в бд проверять товары и групировать по кол-ву с одинаковой продажной ценой

                            System.out.println("Cоздаем транзакцию!");

                            try {


                                // Печать прошла успешно добавляем запись в бд
                                Session session = Properties.sessionFactory.openSession();

                                Transaction t = session.beginTransaction();

                                // Добавляем товары
                                List<Goods> goods = checkObject.getGoodsList();

                                for (int i = 0; i < goods.size(); i++) {
                                    GoodsEntity goodsEntity = new GoodsEntity();
                                    goodsEntity.setPriceAfterDiscount(goods.get(i).getPriceAfterDiscount());
                                    goodsEntity.setPriceFromThePriceList(goods.get(i).getPriceFromThePriceList());
                                    goodsEntity.setSellingPrice(goods.get(i).getSellingPrice());
                                    goodsEntity.setProductId(goods.get(i).getProductId());
                                    goodsEntity.setQuantity(goods.get(i).getCount());
                                    goodsEntity.setProductName(goods.get(i).getProductName());
                                    checkEntity.addGoods(goodsEntity);
                                }

                                int id = (int) session.save(checkEntity);
                                t.commit();
                                session.close();
                                System.out.println("Транзакция прошла успешно!");

                                // todo Запись покупки по скидки

                                if (checkObject.getDiscount() != null) {
                                    DiscountHistoryEntity discountHistoryEntity = new DiscountHistoryEntity(id, checkObject.getDiscount().getId(), checkObject.getDiscount().getDiscountRole(), checkObject.getDateOfCreation());
                                    Session session1 = Properties.sessionFactory.openSession();
                                    Transaction t1 = session1.beginTransaction();
                                    session1.save(discountHistoryEntity);
                                    t1.commit();
                                    session1.close();

                                    if (checkObject.getDiscount().getDiscountRole() == 2 || checkObject.getDiscount().getDiscountRole() == 1) {

                                        try {
                                            pushDataOnTheServer(checkObject, id);
                                        } catch (Exception e) {
                                            logger.error("Не удалось отправить данные при покупке с приложения. Чек: " + id + " Пользователь: " + checkObject.getDiscount().getName() + " " + checkObject.getDiscount().getId());
                                        }

                                    } else {
                                        CardDao cardDao = new CardDao();
                                        DiscountCardEntity card = cardDao.findById(checkObject.getDiscount().getId());
                                        card.setBalance(card.getBalance() + checkObject.getSellingPrice());
                                        cardDao.update(card);
                                    }
                                }


                                // Закрываем чек
                                cancelCheckMethod();
                            } catch (Exception e) {
                                System.out.println("Ошибка при добавлении в бд!" + e.getMessage());
                            }

                        } else {

                            // Не удалось напечатать
                        }

                        // Очищаем переменные для корректной работы
                        Properties.statusPrinted = false;
                        Properties.checkObject = null;


                    }

                    // Подождать ответа от принетера
                    // Если ошибка остонавоиться и показать ошибку
                    // Если ошибки нет, то положить в бд и закрыть.

                }
            }
        }
    }

    // Отправляем данные на сервер и клиенту
    private void pushDataOnTheServer(CheckObject check, int checkIdFromHibernate) {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {
            String name = check.getDiscount().getName();
            int id = check.getDiscount().getId();
            int checkId = checkIdFromHibernate;
            Double total = check.getSellingPrice();
            Double amountPaidBonuses = check.getAmountBonus();

/*            String dateString = check.getDateOfClosing();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = dateFormat.parse(dateString);
            long unixTime = (long) date.getTime() / 1000;
            System.out.println(unixTime);*/

            HttpPost request = new HttpPost("http://5.188.41.134:8080/api/v1/sales");
            StringEntity params = new StringEntity("{\"customerId\":\"" + id + "\",\"bakeryId\":\"" + bakeryId + "\",\"amountPaidBonuses\":\"" + amountPaidBonuses + "\",\"checkId\":\"" + checkId + "\",\"date\":\"" + check.getDateOfClosing() + "\",\"total\":\"" + total + "\"} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            org.apache.http.HttpResponse response = httpClient.execute(request);
            logger.info("Статус отправки данных о чеке на сервер системы лояльности: " + response.getStatusLine().getStatusCode());
            //handle response here...

        } catch (Exception ex) {
            logger.error("Отправка данных на сервер системы лояльности не произведена! \n" + ex.getMessage());
            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }
    }
}
