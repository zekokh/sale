package ru.zekoh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.zekoh.core.printing.KKMOFD;
import ru.zekoh.db.Check;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.*;
import ru.zekoh.properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReturnWindowController {

    @FXML
    public ListView listView;

    @FXML
    public ListView goodView;

    @FXML
    public TableView tableView;

    @FXML
    public TableColumn date;

    @FXML
    public TableColumn total;

    @FXML
    public TableColumn priceOfprice;

    @FXML
    public TableColumn typePayment;

    @FXML
    public TableColumn amountOfbonus;

    @FXML
    public TextField productLabel;

    @FXML
    public TableColumn productName;

    @FXML
    public TableColumn price;

    @FXML
    public TableColumn totalProduct;

    @FXML
    public TableColumn count;

    @FXML
    public TableView goodTableView;

    @FXML
    public TableColumn sellingPrice;

    @FXML
    public TableColumn returnStatus;

    @FXML
    public Button returnBtn;

    @FXML
    public Label labelInfo;

    List<CheckEntity> checkEntityList = null;

    //Товары для отображения в чеке (в UI ListView)
    private ObservableList<CheckEntity> items;

    private ObservableList<TableCheck> tableChecks = FXCollections.observableArrayList();
    private ObservableList<TableGoods> goodsEntities = FXCollections.observableArrayList();

    //Инициализация
    @FXML
    public void initialize() {

        initData();

        date.setCellValueFactory(new PropertyValueFactory<TableCheck, Double>("date"));
        total.setCellValueFactory(new PropertyValueFactory<TableCheck, Double>("total"));
        priceOfprice.setCellValueFactory(new PropertyValueFactory<TableCheck, Double>("priceOfprice"));
        typePayment.setCellValueFactory(new PropertyValueFactory<TableCheck, String>("typePayment"));
        amountOfbonus.setCellValueFactory(new PropertyValueFactory<TableCheck, Double>("amountOfbonus"));
        returnStatus.setCellValueFactory(new PropertyValueFactory<TableCheck, String>("returnStatus"));

        tableView.setItems(tableChecks);

        productName.setCellValueFactory(new PropertyValueFactory<TableGoods, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<TableGoods, Double>("price"));
        count.setCellValueFactory(new PropertyValueFactory<TableGoods, Double>("count"));
        totalProduct.setCellValueFactory(new PropertyValueFactory<TableGoods, Double>("priceAfterDiscount"));
        sellingPrice.setCellValueFactory(new PropertyValueFactory<TableGoods, Double>("sellingPrice"));

        goodTableView.setItems(goodsEntities);

        returnBtn.setVisible(false);

    }


    public void back(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        try {
            Parent pageDate = FXMLLoader.load(getClass().getResource("/view/MenuWindow.fxml"));
            stage.getScene().setRoot(pageDate);
            stage.requestFocus();
        } catch (Exception e) {

        }
    }

    private void initData() {
        Properties.sessionFactory = HibernateSessionFactory.getSessionFactory();
        Session session = Properties.sessionFactory.openSession();
        //Текущий день
        String today = "" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) + "";
        checkEntityList = session.createQuery("SELECT a FROM CheckEntity a WHERE FROM_UNIXTIME(a.dateOfClosing) LIKE '%" + today + "%'", CheckEntity.class).getResultList();
        session.close();
        tableChecks.clear();

        for (int i = 0; i < checkEntityList.size(); i++) {

            long unixSeconds = Long.valueOf(checkEntityList.get(i).getDateOfClosing()).longValue();
            // convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds * 1000L);
            // the format of your date
            //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            // give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
            String formattedDate = sdf.format(date);

            String pay = "карта";
            if (checkEntityList.get(i).getTypeOfPayment() == 1) {
                pay = "наличными";
            }

            String returnStatus = "";
            if (checkEntityList.get(i).getReturnStatus() == true) {
                returnStatus = "Оформлен возврат";
            }

            tableChecks.add(new TableCheck(checkEntityList.get(i).getId(), formattedDate, checkEntityList.get(i).getTotal(), checkEntityList.get(i).getAmountByPrice(), checkEntityList.get(i).getPayWithBonus(), pay, returnStatus));
        }
    }

    public void clicked(MouseEvent mouseEvent) {
        findGoods();
    }

    public void kbrClicked(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                findGoods();
            }
        }
    }

    private void findGoods() {

        if (labelInfo.getText().length() > 0) {
            labelInfo.setText("");
        }

        if (tableView.getSelectionModel().getSelectedItem() != null) {
            TableCheck tableCheck = (TableCheck) tableView.getSelectionModel().getSelectedItem();
            if (tableCheck.getReturnStatus() == "Оформлен возврат") {
                returnBtn.setVisible(true);
            } else {
                returnBtn.setVisible(true);
            }

            // Поиск
            for (int i = 0; i < checkEntityList.size(); i++) {
                if (tableCheck.getId() == checkEntityList.get(i).getId()) {
                    goodsEntities.clear();
                    List<GoodsEntity> goods = checkEntityList.get(i).getGoodsEntity();

                    for (int x = 0; x < goods.size(); x++) {

                        GoodsEntity good = goods.get(x);
                        // Если есть такой товар с одинаковой ценой то обновляем кол-во
                        // если нет добаляем цену
                        TableGoods tableGoods = check(good);
                        if (tableGoods != null) {
                            tableGoods.setCount(tableGoods.getCount() + good.getQuantity());

                            Double temp = tableGoods.getCount() * tableGoods.getPriceAfterDiscount();
                            tableGoods.setSellingPrice(new BigDecimal(temp).setScale(1, RoundingMode.HALF_UP).doubleValue());
                        } else {
                            goodsEntities.add(new TableGoods(good.getProductId(), good.getProductName(), good.getPriceFromThePriceList(), good.getQuantity(), good.getPriceAfterDiscount(), good.getSellingPrice()));
                        }

                    }

                    //goodsEntities.addAll(checkEntityList.get(i).getGoodsEntity());
                    goodTableView.refresh();
                }
            }
        }
    }

    private TableGoods check(GoodsEntity goodsEntity) {
        for (int i = 0; i < goodsEntities.size(); i++) {
            if (goodsEntities.get(i).getId() == goodsEntity.getProductId() && areEqualDouble(goodsEntities.get(i).getPriceAfterDiscount(), goodsEntity.getPriceAfterDiscount(), 2)) {
                return goodsEntities.get(i);
            }
        }
        return null;
    }

    public static boolean areEqualDouble(double a, double b, int precision) {
        return Math.abs(a - b) <= Math.pow(10, -precision);
    }

    public void returnAction(ActionEvent event) throws IOException {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Жак-Андрэ Продажи");

        Parent root = FXMLLoader.load(getClass().getResource("/view/ReturnModalInfo.fxml"));

        dialog.setScene(new Scene(root, 1200, 410));

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

        if (Properties.returnStatusPassword) {
            Properties.returnStatusPassword = false;

            // Берем текущий чек и вовзращаем

            if (tableView.getSelectionModel().getSelectedItem() != null) {
                TableCheck tableCheck = (TableCheck) tableView.getSelectionModel().getSelectedItem();

                CheckEntity checkEntity = findCheck(tableCheck);
                if (checkEntity != null) {

                    // отображаем принтер всплывающее окно с печатью и если все хорошо то ок
                    if(KKMOFD.returnToKKM(goodsEntities, checkEntity)){
                    //if(true){
                        checkEntity.setReturnStatus(true);
                        Session session = Properties.sessionFactory.openSession();
                        Transaction t = session.beginTransaction();
                        session.update(checkEntity);
                        t.commit();
                        session.close();

                        ReturnHistory returnHistory = new ReturnHistory();
                        returnHistory.setCheckId(checkEntity.getId());
                        returnHistory.setWho(Properties.currentUser.getName());
                        returnHistory.setUserId(Properties.currentUser.getId());
                        Date date = new Date();
                        String currentDate = "" + date.getTime() / 1000 + "";
                        returnHistory.setDate(currentDate);
                        returnHistory.setStatus(false);
                        Session session1 = Properties.sessionFactory.openSession();
                        Transaction t1 = session1.beginTransaction();
                        session1.save(returnHistory);
                        t1.commit();
                        session1.close();

                        initData();

                        tableView.refresh();
                    }else {
                        labelInfo.setText("Не удалось напечатать чек!");
                    }
                }
            }

        } else {
            // Отображаем что пароль неверный
            labelInfo.setText("Пароль не верный!");
        }
    }

    private CheckEntity findCheck(TableCheck tableCheck) {
        for (int i = 0; i < checkEntityList.size(); i++) {
            if (checkEntityList.get(i).getId() == tableCheck.getId()) {
                return checkEntityList.get(i);
            }
        }
        return null;
    }

}