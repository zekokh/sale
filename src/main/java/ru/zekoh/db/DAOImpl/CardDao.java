package ru.zekoh.db.DAOImpl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.DiscountCardEntity;

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
                stmt.execute("INSERT INTO `discount_for_employees` (id, balance) VALUE ("+card.getId()+", "+card.getBalance()+")");
                stmt.close();
            }

            //Комит транзакции
            connection.commit();
        } catch (Exception e) {

        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
