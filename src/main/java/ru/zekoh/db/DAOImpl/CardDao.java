package ru.zekoh.db.DAOImpl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.zekoh.db.HibernateSessionFactory;
import ru.zekoh.db.entity.DiscountCardEntity;

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
}
