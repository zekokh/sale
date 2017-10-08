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
import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DAOImpl.CheckDaoImpl;
import ru.zekoh.db.Data;
import ru.zekoh.db.entity.Goods;
import ru.zekoh.db.entity.GoodsForDisplay;
import ru.zekoh.db.entity.Product;
import ru.zekoh.properties.Properties;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SaleController {

    //Панель для кнопок
    @FXML
    public Pane panelForButtons;

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

    //Размер шрифта папок и продуктов
    Double fontFolderAndProduct = 20.0;

    //Инициализация
    @FXML
    public void initialize() {
        //todo работа с чеком
        //Инициализация 1 уровня вложенности папко
        levelPath.add(1);

        //Добавляем на панель папки и продукты
        addBtnsToPanelForBtns();

        goodsListView.setFixedCellSize(40);
        goodsListView.setCellFactory(new GoodsCellFactory());

        panelWithControlBtn.setVisible(true);
        panelWithNumber.setVisible(false);
        panelForKKMInfo.setVisible(false);
        panelWithNumberForCash.setVisible(false);


    }

    public void addBtnsToPanelForBtns() {
        panelForButtons.getChildren().add(getGrid(levelPath.get(levelPath.size() - 1)));
    }

    private Pane getGrid(int level) {
        int x = 0;
        int y = 0;

        //Переменная где содержится level для удобства доступа
        levalProductForSerch = level;

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
                        if(checkList.size() > 0){

                            //Проевряем открыт ли чек или уже оплачен
                            if(checkList.get(currentCheck).isALive()){
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

    //Проверяем промоушены
    private void checkDiscountProgram(Check check) {
        checkList.get(currentCheck).updateCheck(DiscountProgram.promotion6(check));
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

            //Меняем сумму чека в табе для чеков
            Button btn = (Button) panelForCheckBtns.getChildren().get(currentCheck);
            btn.setText(check.getTotal() + " р.");

            totalLabel.setText("Итого: " + check.getTotal() + " р.");

            //Сумма скидки
            Double discount = check.getAmountByPrice() - check.getTotal();
            discountLabel.setText("Скидка: " + discount + " р.");

            //Сумма без скидки
            totalWhithoutDiscountLabel.setText("Без скидки: " + check.getAmountByPrice() + " р.");

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
                goodsForDisplay.setSellingPrice(goods.get(i).getSellingPrice());

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
                goodsTemp.setSellingPrice(sellingPrice);


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
        checkList.get(currentCheck).setTotal(sellingPriceInCheck);
    }

    //Уменьшаем кол-во товара при сайпе в лево
    public void leftSwipe(MouseEvent swipeEvent) {

        if (checkList.size() > 0) {

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
                            updateDataFromANewCheck(false);
                            break;
                        }
                    }

                    if (check.getGoodsList().size() == 0) {
                        check.setDateOfCreation(null);
                    }
                }

            }

        }
    }

    //Отмена чека
    public void cancelCheck(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            checkList.remove(currentCheck);
            if (checkList.size() > 0) {
                int index = checkList.size() - 1;
                currentCheck = (index);
                panelForCheckBtns.getChildren().remove(index);
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

    //Оплата безналичным платежом
    public void payCard(ActionEvent actionEvent) {

        if (checkList.size() > 0) {
            if (checkList.get(currentCheck).isALive()) {
                Check check = checkList.get(currentCheck);

                //Устанавливавем вид оплаты
                check.setTypeOfPayment(2);
                check.setALive(false);
                check.setPaymentState(true);
                check.setDateOfClosing(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "");

                CheckDao checkDao = new CheckDaoImpl();

                //Если запись в Базу Данных успешна прошла печатем чек
                if (checkDao.updateCheck(check)) {

                    //Если принтер подключен
                    if (Properties.KKM) {

                        //Показываем панель с инфой о печати
                        panelForKKMInfo.setVisible(true);
                        infoLabelOnKkmPanel.setText("Идет печать чека...");
                        skipBtnForKkmPanel.setVisible(false);
                        List<GoodsForDisplay> goodsForDisplays = convert(checkList.get(currentCheck).getGoodsList());
                        try {
                            if (!KKM.doIt(goodsForDisplays, checkList.get(currentCheck))) {
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
                    panelForKKMInfo.setVisible(false);

                } else {
                    //todo Выводим сообщения что-то пошло не так
                }
            }
        }

    }

    //Закрытие чека после печати
    public void closeCheck(ActionEvent actionEvent) {
        //закрытие чека после печати
        cancelCheck(actionEvent);
        switchCloseCheckBtn(false);

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
        clickToBtnKbrdCash("4");
    }

    public void kbrd_5_cash(ActionEvent actionEvent) {
        clickToBtnKbrdCash("5");
    }

    public void kbrd_4_cash(ActionEvent actionEvent) {
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
        Double cashBack = checkList.get(currentCheck).getTotal() - moneyFromCustomer;
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

    public void payCashOnKeyBrd(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            Check check = checkList.get(currentCheck);

            //Устанавливавем вид оплаты
            check.setTypeOfPayment(1);
            check.setALive(false);
            check.setPaymentState(true);
            check.setDateOfClosing(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "");

            CheckDao checkDao = new CheckDaoImpl();

            //Если запись в Базу Данных успешна прошла печатем чек
            if (checkDao.updateCheck(check)) {

                //Если принтер подключен
                if (Properties.KKM) {

                    //Показываем панель с инфой о печати
                    panelForKKMInfo.setVisible(true);
                    infoLabelOnKkmPanel.setText("Идет печать чека...");
                    skipBtnForKkmPanel.setVisible(false);
                    List<GoodsForDisplay> goodsForDisplays = convert(checkList.get(currentCheck).getGoodsList());
                    try {
                        if (!KKM.doIt(goodsForDisplays, checkList.get(currentCheck))) {
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
                //todo Выводим сообщения что-то пошло не так
            }
        }
    }

    //Оплата наличкой
    public void payCash(ActionEvent actionEvent) {
        if (checkList.size() > 0) {
            if(checkList.get(currentCheck).isALive()) {
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
}
