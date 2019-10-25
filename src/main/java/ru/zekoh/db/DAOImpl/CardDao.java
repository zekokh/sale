package ru.zekoh.db.DAOImpl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.DiscountCardEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Statement;

public class CardDao {

    public DiscountCardEntity findById(Long id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(DiscountCardEntity.class, id);
    }

    public void update(DiscountCardEntity card) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(card);
        tx1.commit();
        session.close();
    }

    public void create(DiscountCardEntity card) {
        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();

        try {
            if (connection != null) {
                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                Double numeral = new BigDecimal(card.getBalance()).setScale(1, RoundingMode.HALF_UP).doubleValue();
                stmt.execute("INSERT INTO `discount_for_employees` (id, name, balance, budget_for_the_month, amount_of_discount, role) VALUE ('"+card.getId()+"', '"+card.getName()+"', '"+numeral+"', '"+card.getBudgetForTheMonth()+"', '"+card.getAmountOfDiscount()+"', '"+card.getRole()+"')");
                stmt.close();
            }

            //Комит транзакции
            connection.commit();
        } catch (Exception e) {
            System.out.println("Ошибка записи в бд "+e.getMessage());

        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
