package ru.zekoh.db.DAOImpl;

import ru.zekoh.db.Check;
import ru.zekoh.db.DAO.CheckDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Goods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CheckDaoImpl implements CheckDao {

    //Создаем чек
    @Override
    public Check createCheck() {

        //Создаем новый объект типа Чек
        Check check = new Check();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("INSERT INTO `CHECK` (is_a_live) VALUE (1)");
                ResultSet rs = stmt.executeQuery("SELECT * FROM `CHECK` ORDER BY id DESC LIMIT 1");
                while (rs.next()) {
                    check.setId(rs.getInt(1));
                    check.setContainGoods(rs.getBoolean(12));
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();

        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return check;
    }

    //Обновляем данные в чеке
    @Override
    public boolean updateCheck(Check check) {
        boolean flag = false;

        List<Goods> goods = check.getGoodsList();

        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        //Статус оплаты
        int payment_state = check.isPaymentState() ? 1 : 0;

        //
        int discount_on_goods = check.isDiscountOnGoods() ? 1 : 0;

        int discount_on_check = check.isDiscountOnCheck() ? 1 : 0;

        int return_status = check.isReturnStatus() ? 1 : 0;

        int is_a_live = check.isALive() ? 1 : 0;

        int contain_goods = check.isContainGoods() ? 1 : 0;

        try {
            if (connection != null) {

                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                stmt.execute("UPDATE `check` SET `amount_by_price` = '" + check.getAmountByPrice() + "', `total` = '" + check.getTotal() + "', `payment_state`='" + payment_state + "', `discount_on_goods`='" + discount_on_goods + "', `discount_on_check`='" + discount_on_check + "', `type_of_payment`='" + check.getTypeOfPayment() + "', `date_of_creation`='" + check.getDateOfCreation() + "', `date_of_closing`='" + check.getDateOfClosing() + "', `return_status`='" + return_status + "',`is_a_live`='" + is_a_live + "',`contain_goods`='" + contain_goods + "' WHERE `id`='" + check.getId() + "';");
                for (int i = 0; i < goods.size(); i++) {
                    stmt.execute("INSERT INTO `goods` (`check_id`, `general_id`, `product_id`, `product_name`, `price_from_the_price_list`, `price_after_discount`, `selling_price`, `quantity`) VALUE ('" + check.getId() + "', '" + goods.get(i).getGeneralId() + "','" + goods.get(i).getProductId() + "', '" + goods.get(i).getProductName() + "', '" + goods.get(i).getPriceFromThePriceList() + "','" + goods.get(i).getPriceAfterDiscount() + "', '" + goods.get(i).getSellingPrice() + "', '" + goods.get(i).getCount() + "')");
                }
                stmt.close();
            }

            //Комит транзакции
            connection.commit();
            flag = true;
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return flag;
    }

    //Добавить товар в чек
    @Override
    public boolean addGoodsToCheck(Check check, Goods goods) {
        return false;
    }

    //Удалить товар из чека
    @Override
    public boolean deleteGoodsFromCheck(Goods goods) {
        return false;
    }

    //Удалить чек
    @Override
    public boolean deleteCheck(Check check) {
        return false;
    }
}
