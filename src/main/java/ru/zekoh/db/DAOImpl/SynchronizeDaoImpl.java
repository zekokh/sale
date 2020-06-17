package ru.zekoh.db.DAOImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.zekoh.db.DAO.SynchronizeDao;
import ru.zekoh.db.entity.Synchronize;
import ru.zekoh.properties.Properties;
import ru.zekoh.subtotal.Subtotal;

import java.util.concurrent.ExecutionException;

public class SynchronizeDaoImpl implements SynchronizeDao {
    private static final Logger logger = LogManager.getLogger(Subtotal.class);

    @Override
    public Synchronize create(Synchronize synchronize) {
        try{
            Session session = Properties.sessionFactory.openSession();
            Transaction transaction = session.getTransaction();
            session.save(synchronize);
            transaction.commit();
            session.close();
        }catch (Exception e){
            logger.error("Ошибка добавлеия записи о синхронизации: "+ e.toString());
        }
        return null;
    }

    @Override
    public Synchronize getLast() {
        Synchronize synchronize = null;
        try{
            Session session = Properties.sessionFactory.openSession();
            Transaction transaction = session.getTransaction();
            synchronize = (Synchronize) session.createQuery("from synchronize order by id DESC LIMIT 1").list();
            transaction.commit();
            session.close();
        }catch (Exception e){
            logger.error("Ошибка получения последней записи о синхронизации: "+ e.toString());
        }
        return synchronize;
    }
}
