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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.zekoh.core.DiscountProgram;
import ru.zekoh.core.GoodsCellFactory;
import ru.zekoh.core.printing.KKM;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAO.DiscountForEmployeesDao;
import ru.zekoh.db.DAO.PromocodDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.DAOImpl.DiscountForEmployeesDaoImpl;
import ru.zekoh.db.DAOImpl.PromocodDaoImpl;
import ru.zekoh.db.Data;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SaleController {

    //Панель для кнопок
/*    @FXML
    public Pane panelForButtons;*/
    @FXML
    public AnchorPane panelForButtons;


    //Кнопка для создания нового чека
    @FXML
    public Button addNewCheck;

    //ListView со списком продуктов
    @FXML
    public ListView<GoodsForDisplay> goodsListView;

    //Панель для табов чеков
    @FXML
    public HBox panelForCheckBtns;

    //Кнопка создания нового чека
    @FXML
    public Button newCheckBtn;

    //Цена конечная со скидкой
    @FXML
    public Label totalLabel;

    //Скидкой
    @FXML
    public Label discountLabel;

    //Цена за весь чек без скидки
    @FXML
    public Label totalWhithoutDiscountLabel;

    //Панель с кнопками для ввода кол-во товара
    @FXML
    public Pane panelWithNumber;

    //Панель с кнопками управления
    @FXML
    public Pane panelWithControlBtn;

    //Лейбл количества
    @FXML
    public Label countLabel;

    //Панель для инвормации во время печати
    @FXML
    public Pane panelForKKMInfo;

    //Лейбл для информации во время печати
    @FXML
    public Label infoLabelOnKkmPanel;

    //Кнопка если возникнут проблемы с печатью выйти из режима информирования о печати
    @FXML
    public Button skipBtnForKkmPanel;

    //Кнопка закрытия чека после печати
    @FXML
    public Button closeCheckBtn;

    //Панель с клавиатурой для рассчета сдачи
    @FXML
    public Pane panelWithNumberForCash;

    //К оплате на панели рассчета сдачи
    @FXML
    public Label countLabelForCash;

    //Сумма которую дал клиент
    @FXML
    public Label moneyFromCustomerLabel;

    //Сдача
    @FXML
    public Label cashBackToCustomerLabel;

    //Скрол бар для папок и продуктов
    @FXML
    public ScrollPane scrollPane;

    //Кнопка для совершения скидок
    @FXML
    public Button discountBtn;

    //Пполе для ввода id сотрудника
    @FXML
    public TextField idCustomerInput;

    //Лейбл для информирования по проблемам скидки для сотрудника
    @FXML
    public Label discountInfoLabel;

    //Кнопка отмены скидки
    @FXML
    public Button cancelDiscountBtn;

    //Путь уровней вложенности папок и продуктов
    private ArrayList<Integer> levelPath = new ArrayList<Integer>();

    //Список чеков
    private List<Check> checkList = new ArrayList<Check>();

    //Текущий чек
    private int currentCheck = 0;

    //Товары для отображения в чеке (в UI ListView)
    private ObservableList<GoodsForDisplay> items;

    //Флаг для дробного значения количества в клавиатуре
    boolean flagDoubleNumber = false;

    //Флаг для дробного значения количества в клавиатуре для клавиатуры рассчитывающей сдачу
    boolean flagDoubleNumberForCash = false;

    //Количество папок и продуктов в строке
    int countFolderAndProductInRow = 5;

    //Переменная где содержится level для удобства доступа
    int levalProductForSerch = 0;

    //Ширина кнопок с папками и продуктами
    int btnWigth = 180;

    //Высота кнопок с папками и продуктами
    int btnHight = 100;

    //Считаем количество рядов папок и кнопок для расчета высоты panel
    int numberOfLinesForFolderAndProduct = 0;

    //Кол-во папок и продуктов
    int amountFolderAndProduct = 0;

    //Размер шрифта папок и продуктов
    Double fontFolderAndProduct = 20.0;

    //Скидка сотрудника
    //DiscountForEmployees discountForEmployees = null;

    //Инициализация
    @FXML
    public void initialize() {
        //todo работа с чеком
        //Инициализация 1 уровня вложенности папко
        levelPath.add(1);

        //Добавляем на панель папки и продукты
        addBtnsToPanelForBtns();

        goodsListView.setFixedCellSize(70);
        goodsListView.setCellFactory(new GoodsCellFactory());

        panelWithControlBtn.setVisible(true);
        panelWithNumber.setVisible(false);
        panelForKKMInfo.setVisible(false);
        panelWithNumberForCash.setVisible(false);
        cancelDiscountBtn.setVisible(false);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

    public void addBtnsToPanelForBtns() {
        panelForButtons.getChildren().add(getGrid(levelPath.get(levelPath.size() - 1)));
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

        if (Data.getFoldersSortedByLevel().containsKey(level) && Data.getFoldersSortedByLevel().get(level).size() > 0) {
            countFolders = Data.getFoldersSortedByLevel().get(level).size();
        }

        //Кол-во продуктов
        int countProduct = 0;

        if (Data.getProductsSortedByLevel().containsKey(level) && Data.getProductsSortedByLevel().get(level).size() > 0) {
            countProduct = Data.getProductsSortedByLevel().get(level).size();
        }

        amountFolderAndProduct = countFolders + countProduct;

        numberOfLinesForFolderAndProduct = amountFolderAndProduct / countFolderAndProductInRow;
        int tempDivis = amountFolderAndProduct % countFolderAndProductInRow;

        if (tempDivis > 0 || level > 1) {
            numberOfLinesForFolderAndProduct++;
        }

        int temp = (numberOfLinesForFolderAndProduct * btnHight) + 10;

        panelForButtons.setPrefSize(880, temp);

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
        if (Data.getFoldersSortedByLevel().containsKey(level) && Data.getFoldersSortedByLevel().get(level).size() > 0) {
            Button[] btns = new Button[Data.getFoldersSortedByLevel().get(level).size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(Data.getFoldersSortedByLevel().get(level).get(i).getName());
                btns[i].setPrefSize(btnWigth, btnHight);
                btns[i].setWrapText(true);
                btns[i].setFont(new Font(fontFolderAndProduct));
                btns[i].setId(String.valueOf(Data.getFoldersSortedByLevel().get(level).get(i).getId()));
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
        if (Data.getProductsSortedByLevel().containsKey(level) && Data.getProductsSortedByLevel().get(level).size() > 0) {
            Button[] btns = new Button[Data.getProductsSortedByLevel().get(level).size()];
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(Data.getProductsSortedByLevel().get(level).get(i).getName());
                btns[i].setId(Data.getProductsSortedByLevel().get(level).get(i).getId() + "");
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
                            if (checkList.get(currentCheck).isALive()) {
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
                        if (checkList.size() < 1)
                            newCheck(true);

                        if (checkList.get(currentCheck).isALive()) {


                            //Добавляем время при первом добавлении товара в чек
                            if (checkList.get(currentCheck).getGoodsList().size() == 0) {

                                //Дата создания чека считается с добавления первого товара в чек
                                checkList.get(currentCheck).setDateOfCreation(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "");

                                //Меняем флаг наличия товаров в чеке
                                checkList.get(currentCheck).setContainGoods(true);
                            }

                            //Добавляем товар в чек
                            Check check = checkList.get(currentCheck);

                            //Ищем продукт по id
                            Product product = Data.getProductById(Integer.parseInt(b.getId()), level);

                            check.getGoodsList().add(new Goods(product.getId(), product.getGeneralId(), product.getName(), product.getClassifierId(), 1.0, product.getPrice(), product.getPrice(), product.getPrice()));

                            //Проверяем скидки
                            checkDiscountProgram(check);

                            updateDataFromANewCheck(false);

                            b.setBackground(new Background(new BackgroundFill(
                                    Color.valueOf("#E1F5FE"), CornerRadii.EMPTY, Insets.EMPTY)));
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

    /* Вызываем при:
    - уменьшении количества товара;
    - при добавлении товара;
    - после ввода количества товара при закрытие клавиатуры;
     */
    private void checkDiscountProgram(Check check) {

        //Отменяем скидки на товары внутри чека
        for (int i = 0; i < check.getGoodsList().size(); i++) {

            //Текущий товар
            Goods goods = check.getGoodsList().get(i);

            //Цена на текущий товар по прайсу
            Double priceFromThePriceList = goods.getPriceFromThePriceList();

            //Устанавливаем цену после скидки такую как было по прайсу
            goods.setPriceAfterDiscount(priceFromThePriceList);

            //Цена после скидки
            Double priceAfterDiscount = goods.getPriceAfterDiscount();

            //Количество товара
            Double count = goods.getCount();

            //Считаем продажную цену умножая цену после скидки на кол-во товара
            Double sellingPrice = count * priceAfterDiscount;

            //Устанавливаем продажную цену товара
            goods.setSellingPrice(sellingPrice);

            //Указываем что в чеке нет товаров со скидкой
            check.setDiscountOnGoods(false);

        }

        //Если есть скидка на чек, то считаем укладывается ли сотрудник по лимиту на приобретаемые товары в месяц
        if (check.isDiscountOnCheck()) {

            discountConverter(check);

            // Цена за весь чек с учетом скидки
            Double total = check.getTotal();

            //Текущий баланс сотрудника
            Double balance = check.getDiscountForEmployees().getBalance();

            //Лимит по скидки в месяц
            Double limit = check.getDiscountForEmployees().getBudgetForTheMonth();

            //Если текущий баланс сотрудника вместе с запланированной покупкой больше лимита установленного в месяц то оповестить об этом
            if ((balance + total) >= limit) {
                discountInfoLabel.setText("Лимит превышен! Ваш баланс: " + (limit - balance) + " р.");
            } else {
                discountInfoLabel.setText("");
            }
        } else {
            //Если на чек скидки нет
            //Проверяем есть в текущем чеке товары на которые работает промоушен

            //6 больших эклеров по цене 5
            check.updateCheck(DiscountProgram.promotion6(check));

            //Скидка на 5 и 10 круасанов
            check.updateCheck(DiscountProgram.promotion2(check));

            //8 кусков флана стоимость как за целый флан
            check.updateCheck(DiscountProgram.promotion1(check));

            //6 мини эклеров по цене 5
            //check.updateCheck(DiscountProgram.promotionMini6(check));

            //Скидка 30% на выпечку после 8 вечера
            //check.updateCheck(DiscountProgram.discountOnBakes(check));

            //Скидка на 5 мафинов
            //check.updateCheck(DiscountProgram.maffins(check));


            //Промокод
            int promoCod = check.getPromocod();

            //Если промокод активирован
            if (promoCod != 0) {

                //Делаем скидку 10% на все товары на которых еще нет скидок
                //Отменяем скидки на товары внутри чека
                for (int i = 0; i < check.getGoodsList().size(); i++) {

                    //Текущий товар
                    Goods goods = check.getGoodsList().get(i);

                    //Цена на текущий товар по прайсу
                    Double priceFromThePriceList = goods.getPriceFromThePriceList();

                    //Цена после скидки
                    Double priceAfterDiscount = goods.getPriceAfterDiscount();

                    //Если цены не отличаются и этот товар не хлеб (классификатор - 11) и комбо из папки (классификатор - 14) т.е. товар не участвует в промоушене то делаем на товар скидку
                    if (priceFromThePriceList.equals(priceAfterDiscount) && goods.getClassifier() != 11 && goods.getClassifier() != 14) {

                        //Сумма скидки
                        Double discountAmount = priceFromThePriceList * 0.10;

                        //Делаем скидку 10%
                        goods.setPriceAfterDiscount(priceFromThePriceList - discountAmount);

                        //Обновляем значение в цене после скидки
                        priceAfterDiscount = goods.getPriceAfterDiscount();
                    }

                    //Количество товара
                    Double count = goods.getCount();

                    //Считаем продажную цену умножая цену после скидки на кол-во товара
                    Double sellingPrice = count * priceAfterDiscount;

                    //Устанавливаем продажную цену товара
                    goods.setSellingPrice(sellingPrice);

                    //Указываем что в чеке есть товаров со скидкой
                    check.setDiscountOnGoods(true);
                }
            }
        }
    }


    //Создаем новый чек и добавляем  в список чеков
    public void addNewCheck(ActionEvent actionEvent) {
        newCheck(false);
    }

    //Метод делает неактивные все табы чеков кроме переданного номера
    private void deselectAllTabsForCheckExcept(int index) {
        for (int i = 0; i < panelForCheckBtns.getChildren().size(); i++) {
            if (index == i) {
                Button btn = (Button) panelForCheckBtns.getChildren().get(i);
                btn.setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#BBDEFB"), CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                Button btn = (Button) panelForCheckBtns.getChildren().get(i);
                btn.setBackground(new Background(new BackgroundFill(
                        Color.valueOf("#E3F2FD"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    //Обновление данных из чека
    private void updateDataFromANewCheck(boolean createCheckByClickingCnProduct) {
        Check check = checkList.get(currentCheck);

        if (check.getGoodsList().size() > 0) {
            List<GoodsForDisplay> goodsForDisplayList = convert(check.getGoodsList());
            items = FXCollections.observableArrayList(goodsForDisplayList);
            goodsListView.setItems(items);
            goodsListView.refresh();

            //Рассчитываем суммы за чек
            amountInCheck(check.getGoodsList());


            Double totalLabelDouble = new BigDecimal(check.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            check.setTotal(totalLabelDouble);

            //Меняем сумму чека в табе для чеков
            Button btn = (Button) panelForCheckBtns.getChildren().get(currentCheck);
            btn.setText(check.getTotal() + " р.");

            totalLabel.setText("Итого: " + check.getTotal() + " р.");

            //Сумма скидки
            Double discount = check.getAmountByPrice() - check.getTotal();
            discount = new BigDecimal(discount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            discountLabel.setText("Скидка: " + discount + " р.");

            //Сумма без скидки
            Double amountByPrice = check.getAmountByPrice();
            amountByPrice = new BigDecimal(amountByPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
            totalWhithoutDiscountLabel.setText("Без скидки: " + amountByPrice + " р.");


            if (check.getDiscountForEmployees() != null) {

                Double temp1 = checkList.get(currentCheck).getAmountByPrice() - (checkList.get(currentCheck).getAmountByPrice() * check.getDiscountForEmployees().getAmountOfDiscount() / 100);

                if ((check.getDiscountForEmployees().getBalance() + temp1) >= check.getDiscountForEmployees().getBudgetForTheMonth()) {
                    discountInfoLabel.setText("Лимит превышен! Ваш баланс: " + (check.getDiscountForEmployees().getBudgetForTheMonth() - check.getDiscountForEmployees().getBalance()) + " р.");
                } else {
                    discountInfoLabel.setText("");
                }
            }

        } else {

            //Если мы создаем чек не через нажатия на товар
            if (!createCheckByClickingCnProduct) {
                if (checkList.size() > 1) {
                    items.clear();
                    goodsListView.refresh();
                }

                //Меняем сумму чека в табе для чеков
                Button btn = (Button) panelForCheckBtns.getChildren().get(currentCheck);
                btn.setText("0 р.");

                totalLabel.setText("Итого: 0 р.");

                //Сумма скидки
                discountLabel.setText("Скидка: 0 р.");

                //Сумма без скидки
                totalWhithoutDiscountLabel.setText("Без скидки: 0 р.");
            }
        }

        if (checkList.size() > 0) {
            if (check.isPaymentState()) {
                closeCheckBtn.setVisible(true);
            } else {
                closeCheckBtn.setVisible(false);
            }
        }
    }

    public void switchCloseCheckBtn(boolean flag) {
        if (flag) {
            closeCheckBtn.setVisible(true);
        } else {
            closeCheckBtn.setVisible(false);
        }
    }

    public void clearDataOnDisplay() {
        goodsListView.getItems().clear();
        goodsListView.refresh();

        //Сумма итого
        totalLabel.setText("Итого: 0 р.");

        //Сумма скидки
        discountLabel.setText("Скидка: 0 р.");

        //Сумма без скидки
        totalWhithoutDiscountLabel.setText("Без скидки: 0 р.");

        if (checkList.size() > 0) {
            checkList.get(currentCheck).setDiscountForEmployees(null);
        }

        idCustomerInput.clear();
        discountInfoLabel.setText("");

        //Переключить кнопки сделать скидку и отмена скидки
        cancelDiscountBtn.setVisible(false);
        discountBtn.setVisible(true);

        switchCloseCheckBtn(false);


    }

    public void controllDiscount() {
        if (checkList.get(currentCheck).isDiscountOnCheck()) {
            idCustomerInput.setText(checkList.get(currentCheck).getDiscountForEmployees().getName() + "");
            discountBtn.setVisible(false);
            cancelDiscountBtn.setVisible(true);
        } else if (checkList.get(currentCheck).getPromocod() != 0) {
            idCustomerInput.setText("Скидка по промокоду 10%");
            discountBtn.setVisible(false);
            cancelDiscountBtn.setVisible(true);
        } else {
            idCustomerInput.clear();
            discountBtn.setVisible(true);
            cancelDiscountBtn.setVisible(false);

        }


        discountInfoLabel.setText("");
    }

    public void controllDiscount(boolean delete) {
        if (checkList.get(currentCheck).isDiscountOnCheck()) {
            if (delete) {
                idCustomerInput.clear();
                discountBtn.setVisible(true);
                cancelDiscountBtn.setVisible(false);
            } else {
                idCustomerInput.setText(checkList.get(currentCheck).getDiscountForEmployees().getName() + "");
                discountBtn.setVisible(false);
                cancelDiscountBtn.setVisible(true);
            }
        }
    }

    //Переход в меню и выход из окно продаж

    public void exit(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            Stage stage = new Stage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ModalInfoWindow.fxml"));
                stage.setTitle("Оплата");
                stage.setMinHeight(150);
                stage.setMinWidth(300);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                stage.show();
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                Parent pageDate = FXMLLoader.load(getClass().getResource("/MenuWindow.fxml"));
                stage.getScene().setRoot(pageDate);
                stage.requestFocus();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /*Метод добавления нового чека в зависимости от куда его вызовут
    (например из кнопки для создания чеков или когда чек пустой и
    пользователь нажмет на добавление товара в пустой чек) */

    public void newCheck(boolean createCheckByClickingCnProduct) {

        //Ограничим количество одновременно создоваемых чеков до 9 потом надо сделать прокрутку
        if (checkList.size() < 9) {

            //Создаю объект checkDao для создания нового чека
            CheckDao checkDao = new CheckDaoImpl();

            //Создаю новый чек
            Check check = checkDao.createCheck();

            //Если чек создан успешно
            if (check != null) {

                //Добавляю чек в список чеков
                checkList.add(check);

            /* Если в списке чеков есть чек то устанавливаем в переменную с текущим чеком последний созданный чек
            (не знаю когда могло бы сломаться, но навсякий случай сделал) */
                if (checkList.size() > 0) {
                    currentCheck = (checkList.size() - 1);

                    //Создаем кнопку с нужными параметрами и делаем ее активной
                    Button button = new Button();
                    String temp = (checkList.size() - 1) + "";
                    button.setId(temp);
                    button.setText(check.getTotal() + " р.");
                    button.setPrefSize(90, 85);
                    button.setBackground(new Background(new BackgroundFill(
                            Color.valueOf("#BBDEFB"), CornerRadii.EMPTY, Insets.EMPTY)));
                    button.setBorder(new Border(new BorderStroke(Color.valueOf("#BBDEFB"),
                            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                    //Обработчик событий
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            currentCheck = Integer.parseInt(button.getId());
                            deselectAllTabsForCheckExcept(currentCheck);
                            //todo переключать кнопки и очищать поле
                            controllDiscount();

                            updateDataFromANewCheck(false);
                        }
                    });

                    //Добавляем кнопку на на панель для табов чеков
                    panelForCheckBtns.getChildren().add(button);

                    //Индекс текщего таба с которого не нужно убирать выделение
                    int index = panelForCheckBtns.getChildren().size() - 1;

                    //Метод делает неактивные все табы чеков кроме переданного номера
                    deselectAllTabsForCheckExcept(index);

                    //Обновление данных из чека
                    updateDataFromANewCheck(createCheckByClickingCnProduct);


                    //Панель скидки
                    controllDiscount();
                }
            }
        }

    }

    //Конвертирует GoodsList в лист для отображении в ListView
    public List<GoodsForDisplay> convert(List<Goods> goods) {

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

    //Считаем суммы данных по товарам в чеке
    public void amountInCheck(List<Goods> goods) {

        //Сумма в чеке по прайсу
        Double priceFromThePrice = 0.0;

        //Сумма в чеке с учетом скидок
        Double sellingPriceInCheck = 0.0;

        for (int i = 0; i < goods.size(); i++) {

            //Сумма по прайсу
            priceFromThePrice = priceFromThePrice + (goods.get(i).getCount() * goods.get(i).getPriceFromThePriceList());

            //Сумма в чеке по с учетом скидок
            sellingPriceInCheck = sellingPriceInCheck + (goods.get(i).getCount() * goods.get(i).getSellingPrice());
        }

        //Расчет данных для чека
        checkList.get(currentCheck).setAmountByPrice(priceFromThePrice);

        if (checkList.get(currentCheck).isDiscountOnCheck()) {
            if (checkList.get(currentCheck).getDiscountForEmployees() != null) {
                Double temp = new BigDecimal(checkList.get(currentCheck).getAmountByPrice() - (checkList.get(currentCheck).getAmountByPrice() * checkList.get(currentCheck).getDiscountForEmployees().getAmountOfDiscount() / 100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
                checkList.get(currentCheck).setTotal(temp);
                //System.out.println(checkList.get(currentCheck).getAmountByPrice() - (checkList.get(currentCheck).getAmountByPrice() * checkList.get(currentCheck).getDiscountForEmployees().getAmountOfDiscount() / 100));
            }
        } else {
            sellingPriceInCheck = new BigDecimal(sellingPriceInCheck).setScale(2, RoundingMode.HALF_UP).doubleValue();
            checkList.get(currentCheck).setTotal(sellingPriceInCheck);
        }

    }

    //Уменьшаем кол-во товара при сайпе в лево
    public void leftSwipe(MouseEvent swipeEvent) {

        if (checkList.size() > 0) {
            if (!checkList.get(currentCheck).isPaymentState()) {

                //Текущий чек
                Check check = checkList.get(currentCheck);

                //Если в чеке есть товары
                if (check.getGoodsList().size() > 0) {

                    //Находим выбранный товар
                    List<GoodsForDisplay> currentGoods = goodsListView.getSelectionModel().getSelectedItems();

                    //Если удалось определить конкретный товар
                    if (currentGoods.size() > 0) {
                        //Найти товар в списке товаров внутри чека по id и удалить
                        for (int i = 0; i < check.getGoodsList().size(); i++) {
                            if (check.getGoodsList().get(i).getProductId() == currentGoods.get(0).getProductId()) {
                                check.getGoodsList().remove(i);
                                checkDiscountProgram(check);
                                updateDataFromANewCheck(false);
                                break;
                            }
                        }

                        if (check.getGoodsList().size() == 0) {
                            check.setDateOfCreation(null);

                            //Получается update не срабатывает так как в чеке нет товара
                            //Поэтому в ручную очищаем
                            items.clear();
                            goodsListView.refresh();
                        }

                    }

                }
            }
        }
    }

    //Отмена чека
    public void cancelCheck(ActionEvent actionEvent) {

        //Если есть хотя бы один созданный чек
        if (checkList.size() > 0) {

            //Удаляем скидку на чек сотрудника если есть
            if (checkList.get(currentCheck).isDiscountOnCheck()) {
                controllDiscount(true);
                checkList.get(currentCheck).setDiscountForEmployees(null);
            }
            checkList.remove(currentCheck);
            if (checkList.size() > 0) {


                int index = checkList.size() - 1;

                currentCheck = (index);
                panelForCheckBtns.getChildren().remove(index);
                for (int i = 0; i < panelForCheckBtns.getChildren().size(); i++) {
                    panelForCheckBtns.getChildren().get(i).setId(i + "");
                }
                deselectAllTabsForCheckExcept(currentCheck);
                updateDataFromANewCheck(false);
            } else {
                panelForCheckBtns.getChildren().clear();
                currentCheck = 0;
                clearDataOnDisplay();
            }
        }
    }

    //Переключение на панель с клавиатурой
    public void switchToKeyBord(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            if (checkList.get(currentCheck).isALive()) {
                if (checkList.get(currentCheck).getGoodsList().size() > 0) {
                    panelWithControlBtn.setVisible(false);
                    panelWithNumber.setVisible(true);
                }
            }
        }
    }

    //Переключение на панель управления
    public void switchToControlPanel(ActionEvent actionEvent) {

        if (checkList.size() > 0) {
            Check check = checkList.get(currentCheck);

            if (check.getGoodsList().size() > 0) {

                //Если значение кол-во не равно 0
                if (!countLabel.getText().equals("0")) {
                    Double count = Double.parseDouble(countLabel.getText());

                    int index = (check.getGoodsList().size() - 1);

                    //Если товар может быть дробным то просто прописываем его значение иначе дублируем его для корректной работы акций
                    //Класификатор макаронс = 10
                    //Класификатор хлеба и тд
                    //todo добавить работы с дробными значениями через классификаторы

                    if (check.getGoodsList().get(index).getClassifier() == 10) {
                        check.getGoodsList().get(index).setCount(count);
                    } else {
                        int i = 1;
                        Product product = Data.getProductById(check.getGoodsList().get(index).getProductId(), levalProductForSerch);
                        while (i < count) {
                            check.getGoodsList().add(new Goods(product.getId(), product.getGeneralId(), product.getName(), product.getClassifierId(), 1.0, product.getPrice(), product.getPrice(), product.getPrice()));
                            i++;
                        }
                    }

                    //Проверка промоушенов
                    checkDiscountProgram(check);

                    //Обновление данных на экране пользователя
                    updateDataFromANewCheck(false);
                }
            }
        }

        countLabel.setText("0");
        flagDoubleNumber = false;
        panelWithNumber.setVisible(false);
        panelWithControlBtn.setVisible(true);
    }

    public void kbrd_1(ActionEvent actionEvent) {
        clickOnKbrd("1");
    }

    public void kbrd_2(ActionEvent actionEvent) {
        clickOnKbrd("2");
    }

    public void kbrd_3(ActionEvent actionEvent) {
        clickOnKbrd("3");
    }

    public void kbrd_4(ActionEvent actionEvent) {
        clickOnKbrd("4");
    }

    public void kbrd_5(ActionEvent actionEvent) {
        clickOnKbrd("5");
    }

    public void kbrd_6(ActionEvent actionEvent) {
        clickOnKbrd("6");
    }

    public void kbrd_7(ActionEvent actionEvent) {
        clickOnKbrd("7");
    }

    public void kbrd_8(ActionEvent actionEvent) {
        clickOnKbrd("8");
    }

    public void kbrd_9(ActionEvent actionEvent) {
        clickOnKbrd("9");
    }

    public void kbrd_0(ActionEvent actionEvent) {
        clickOnKbrd("0");
    }

    public void kbrd_dote(ActionEvent actionEvent) {
        if (!flagDoubleNumber) {
            String countLabelString = countLabel.getText();
            countLabel.setText(countLabelString + ".");
            flagDoubleNumber = true;
        }
    }

    public void delLastSymbol(ActionEvent actionEvent) {
        String data = countLabel.getText();
        if (data.length() <= 12) {
            if (data.length() > 0) {
                if (data.length() > 1) {
                    String mayBeDote = data.substring(data.length() - 1, data.length());
                    if (mayBeDote.equals(".")) {
                        flagDoubleNumber = false;
                    }
                    data = data.substring(0, data.length() - 1);
                    countLabel.setText(data);
                } else {
                    countLabel.setText("0");
                }


            } else {
                clickOnKbrd("0");
            }
        }
    }

    public void clickOnKbrd(String value) {
        String countLabelString = countLabel.getText();
        if (countLabelString.equals("0")) {
            countLabel.setText(value);
        } else {
            countLabel.setText(countLabelString + value);
        }
    }

    //Закрытие чека после печати
    public void closeCheck(ActionEvent actionEvent) {
        //закрытие чека после печати
        cancelCheck(actionEvent);

    }

    //Закрыть инфо панель для печати
    public void skipBtnForKkm(ActionEvent actionEvent) {
        panelForKKMInfo.setVisible(false);
    }

    //Клавиатура для расчета сдачи
    public void kbrd_1_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("1");
    }

    public void kbrd_2_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("2");
    }

    public void kbrd_3_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("3");
    }

    public void kbrd_6_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("6");
    }

    public void kbrd_5_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("5");
    }

    public void kbrd_4_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("4");
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

    public void delLastSymbol_cash(ActionEvent actionEvent) {
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

    public void reloadCashBack() {
        Double moneyFromCustomer = Double.parseDouble(moneyFromCustomerLabel.getText());
        Double cashBack = moneyFromCustomer - checkList.get(currentCheck).getTotal();
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
        reloadCashBack();
    }

    //Оплата безналичным платежом
    public void payCard(ActionEvent actionEvent) {

        pay(false);

    }

    //Оплата наличкой
    public void payCashOnKeyBrd(ActionEvent actionEvent) {
        pay(true);
    }

    public void pay(boolean cashType) {

        //Если есть хотя бы один созданный чек
        if (checkList.size() > 0) {

            //Текущий чек
            Check check = checkList.get(currentCheck);

            //Если выбранный чек еще существует
            if (check.isALive()) {

                //Если есть товары в чеке
                if (check.getGoodsList().size() > 0) {

                    //Устанавливает флаг что чека больше не существует
                    check.setALive(false);

                    //Флаг наличия скидки для сотрудника или гостя
                    boolean flagDiscount = false;

                    //Если товар продается со скидкой для сотрудника или клиента, то проверим совбадает ли баланс после покупки с установленным лимитом
                    if (checkList.get(currentCheck).getDiscountForEmployees() != null) {

                        //Цена за весь чек
                        Double price = check.getAmountByPrice();

                        //Размер скидки (проценты)
                        Double discount = checkList.get(currentCheck).getDiscountForEmployees().getAmountOfDiscount() / 100;

                        //Сумма скидки
                        Double discountAmount = price * discount;

                        //Текущий баланс сотрудника
                        Double balance = checkList.get(currentCheck).getDiscountForEmployees().getBalance();

                        //Лимит по скидки в месяц
                        Double limit = checkList.get(currentCheck).getDiscountForEmployees().getBudgetForTheMonth();

                        //Цена на чек со скидкой сотрудника
                        Double discountPrice = price - discountAmount;

                        Double temp = checkList.get(currentCheck).getAmountByPrice() - (checkList.get(currentCheck).getAmountByPrice() * checkList.get(currentCheck).getDiscountForEmployees().getAmountOfDiscount() / 100);

                        if ((balance + discountPrice) >= limit) {
                            discountInfoLabel.setText("Не возможно совершить покупку! Ваш баланс: " + (checkList.get(currentCheck).getDiscountForEmployees().getBudgetForTheMonth() - checkList.get(currentCheck).getDiscountForEmployees().getBalance()) + " р.");
                            return;
                        } else {

                            //Если условия соблюдены для скидки
                            flagDiscount = true;
                        }
                    }

                    //Если есть скидка 10% по промоушену из чека
                    if (check.getPromocod() != 0) {

                        //Промокод
                        int promoCod = check.getPromocod();

                        //Создаем объест promoCod для манипуляцией сущностью
                        PromocodDao promocodDao = new PromocodDaoImpl();

                        //Получаем обхект промокода
                        Promocod promo = promocodDao.getPromocodByNumber(promoCod);

                        //Отмечаем промокод как использованный
                        promocodDao.use(promo);
                    }

                    //Устанавливавем вид оплаты
                    if (cashType) {

                        //Наличка
                        check.setTypeOfPayment(1);
                    } else {

                        //Картой
                        check.setTypeOfPayment(2);
                    }

                    //Устанавливаем флаг оплаты, чек оплачен
                    check.setPaymentState(true);

                    //Указываем дату закрытия чека
                    check.setDateOfClosing(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "");

                    //Создаем объект Check DAO для манипуляции с сущностью
                    CheckDao checkDao = new CheckDaoImpl();

                    //Если запись в Базу Данных успешна прошла печатем чек
                    if (checkDao.updateCheck(check)) {

                        //Если есть скидка на чек для сотрудника или гостя
                        if (flagDiscount) {

                            //Создаем объект для манипуляции скидкой для сотрудника или гостя
                            DiscountForEmployeesDao discountForEmp = new DiscountForEmployeesDaoImpl();

                            //Если данные о сотруднике или госте со скидкой существуют то добавляем их БД
                            if (checkList.get(currentCheck).getDiscountForEmployees() != null) {

                                //Обновляем в БД баланс на счету сотрудника, клиента и записываем в историю транзаций о проведенной скидке
                                discountForEmp.passed(check, checkList.get(currentCheck).getDiscountForEmployees());
                            }
                        }

                        //Если принтер подключен
                        if (Properties.KKM) {

                            //Показываем панель с инфой о печати
                            panelForKKMInfo.setVisible(true);
                            infoLabelOnKkmPanel.setText("Идет печать чека...");
                            skipBtnForKkmPanel.setVisible(false);
                            List<GoodsForDisplay> goodsForDisplays = convert(checkList.get(currentCheck).getGoodsList());
                            try {
                                if (!KKMOFD.printCheck(Properties.FPTR, checkList.get(currentCheck), goodsForDisplays)) {
                                    infoLabelOnKkmPanel.setText("Возникли проблемы с ККМ отключите ККМ в настройках и обратитесь к администратору!");
                                    skipBtnForKkmPanel.setVisible(true);
                                }
                            } catch (Exception e) {
                                infoLabelOnKkmPanel.setText("Возникли проблемы с ККМ отключите ККМ в настройках и обратитесь к администратору!");
                                skipBtnForKkmPanel.setVisible(true);
                                System.out.println("что то пошло не так с ккм");
                                System.out.println(e);
                            }
                        }
                        switchCloseCheckBtn(true);
                        switchToKeyBrdCash(false);
                        panelForKKMInfo.setVisible(false);
                    } else {

                        //Устанавливает флаг что чека больше не существует
                        check.setALive(true);

                        //Если не удалось добавить чек в БД то сообщаем об этом пользователю
                        discountInfoLabel.setText("Не удалось добавить чек в БД!!! Обратитесь к администратору!");
                    }
                }
            } else {
                System.out.printf("Чек уже не существует!");
            }

        }
    }

    //Оплата наличкой
    public void payCash(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            if (checkList.get(currentCheck).isALive()) {
                switchToKeyBrdCash(true);
            }
        }
    }

    public void switchToKeyBrdCash(boolean flag) {
        if (flag) {
            panelWithNumberForCash.setVisible(true);
            panelWithControlBtn.setVisible(false);
            countLabelForCash.setText("К оплате: " + checkList.get(currentCheck).getTotal() + " р.");
        } else {
            panelWithNumberForCash.setVisible(false);
            panelWithControlBtn.setVisible(true);
            countLabelForCash.setText("К оплате: 0 р.");
            moneyFromCustomerLabel.setText("0");
            cashBackToCustomerLabel.setText("Сдача: 0 р.");
        }
    }

    //Возвращает обратно на панель с управления чеком и очищает поля на панели сдачи
    public void returnToControlBtnPanel(ActionEvent actionEvent) {
        switchToKeyBrdCash(false);

    }

    //Сделать скидку
    public void doDiscount(ActionEvent actionEvent) {

        //Очищаем информационное поле от предвидущих записей
        discountInfoLabel.setText("");

        //Номер сикдки (сотрудника или гостя) или промокода
        String data = idCustomerInput.getText();

        if (checkList.size() > 0) {

            //Текущий чек
            Check check = checkList.get(currentCheck);

            // Если в чеке есть товары
            if (check.getGoodsList().size() > 0) {

                //Проверяем открыт ли чек
                if (check.isALive()) {
                    DiscountForEmployeesDao discountForEmployeesDao = new DiscountForEmployeesDaoImpl();

                    try {
                        int numberCustomerCurd = Integer.parseInt(idCustomerInput.getText());

                        check.setDiscountForEmployees(discountForEmployeesDao.getDiscountCard(numberCustomerCurd));

                        if (check.getDiscountForEmployees() == null) {
                            discountInfoLabel.setText("Пользователь не найден");
                            idCustomerInput.setText("");
                        } else {
                            //todo изменение цены
                            Double temp = check.getAmountByPrice() - (check.getAmountByPrice() * check.getDiscountForEmployees().getAmountOfDiscount() / 100);

                            if ((check.getDiscountForEmployees().getBalance() + temp) >= check.getDiscountForEmployees().getBudgetForTheMonth()) {
                                discountInfoLabel.setText(check.getDiscountForEmployees().getName() + " Лимит превышен! Ваш баланс: " + (check.getDiscountForEmployees().getBudgetForTheMonth() - check.getDiscountForEmployees().getBalance()) + " р.");
                            } else {
                                check.setDiscountOnCheck(true);


                                check.setTotal(check.getAmountByPrice() - (check.getAmountByPrice() * check.getDiscountForEmployees().getAmountOfDiscount() / 100));

                                discountConverter(check);
                                idCustomerInput.setText(checkList.get(currentCheck).getDiscountForEmployees().getName());

                                updateDataFromANewCheck(false);

                                cancelDiscountBtn.setVisible(true);
                                discountBtn.setVisible(false);
                            }
                        }
                    } catch (Exception e) {
                        discountInfoLabel.setText("Что то пошло не так!");
                    }
                }
            }
        }
    }


    // Скидка на чек
    private Check discountConverter(Check check) {
        Double discount = check.getDiscountForEmployees().getAmountOfDiscount() / 100;

        List<Goods> goods = check.getGoodsList();

        Double total = 0.0;
        for (int i = 0; i < goods.size(); i++) {
            Double priceAfterDiscount = goods.get(i).getPriceFromThePriceList() - (goods.get(i).getPriceFromThePriceList() * discount);
            goods.get(i).setPriceAfterDiscount(priceAfterDiscount);

            Double sellingPrice = priceAfterDiscount * goods.get(i).getCount();
            goods.get(i).setSellingPrice(sellingPrice);
            total += sellingPrice;
        }

        // Устанавливаем общую сумму чека
        check.setTotal(total);

        return check;
    }

    //Отмена скидки
    public void cancelDiscount(ActionEvent actionEvent) {

        //Текущий чек
        Check check = checkList.get(currentCheck);

        //Обнуляем промокод
        check.setPromocod(0);

        check.setDiscountOnCheck(false);
        check.setDiscountForEmployees(null);

        //Обновляем цены на товары после отмены чека
        checkDiscountProgram(check);

        checkList.get(currentCheck).setDiscountForEmployees(null);

        idCustomerInput.clear();
        discountInfoLabel.setText("");
        cancelDiscountBtn.setVisible(false);
        discountBtn.setVisible(true);
        updateDataFromANewCheck(false);
    }

    //Запись скидки сотрудника
    public void recordDiscountForEmployer() {

    }
}
