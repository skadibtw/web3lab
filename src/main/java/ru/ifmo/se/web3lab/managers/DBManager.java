package ru.ifmo.se.web3lab.managers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import ru.ifmo.se.web3lab.models.Point;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;


public class DBManager {
    private static volatile DBManager instance;

    public static DBManager getInstance() {
        DBManager localInstance = instance;
        if (localInstance == null) {
            synchronized (DBManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBManager();
                }
            }
        }
        return localInstance;
    }


    @PersistenceContext
    private EntityManager manager;

    public DBManager() {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("default");
        manager = managerFactory.createEntityManager();
    }

    public void send(Point point) {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.persist(point);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
        }
    }

    public ArrayList<Point> getAll() {
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            ArrayList<Point> res = new ArrayList<>(manager.createQuery("select p from Point p", Point.class).getResultList());
            transaction.commit();
            return res;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            return new ArrayList<>();
        }
    }

    public void clearAll() {
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.createQuery("delete from Point p").executeUpdate();
        transaction.commit();
    }
}