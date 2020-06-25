package ru.zekoh.db.DAOImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.zekoh.db.DAO.SynchronizeDao;
import ru.zekoh.db.DataBase;
import ru.zekoh.db.entity.Synchronize;
import ru.zekoh.properties.Properties;
import ru.zekoh.subtotal.Subtotal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SynchronizeDaoImpl implements SynchronizeDao {
    private static final Logger logger = LogManager.getLogger(Subtotal.class);

    @Override
    public boolean create(Synchronize synchronize) {
        Session session = Properties.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.save(synchronize);
            transaction.commit();
            return true;
        }catch (Exception e){
            logger.error("Ошибка добавлеия записи о синхронизации: "+ e.toString());
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public Synchronize getLast() {
        Synchronize synchronize = new Synchronize();
        //Получаем соединение с БД
        Connection connection = DataBase.getConnection();
        try{
            if (connection != null) {
                Statement stmt = null;
                connection.setAutoCommit(false);
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM synchronization WHERE status = 1 ORDER BY id DESC LIMIT 1");
                while (rs.next()) {
                    synchronize.setId(rs.getInt(1));
                    synchronize.setDateCloseOfLastCheck(rs.getString(2));

                    synchronize.setStatus(rs.getBoolean(4));
                    synchronize.setCheckId(rs.getInt(5));
                }
            }
        }catch (Exception e){
            logger.error("Ошибка получения последней записи о синхронизации: "+ e.toString());
        } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        return synchronize;
    }
}
