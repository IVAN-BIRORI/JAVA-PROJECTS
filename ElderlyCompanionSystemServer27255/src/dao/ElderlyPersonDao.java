package dao;

/**
 *
 * @author USER
 */

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import util.HibernateUtil;
import java.util.List;
import model.ElderlyPerson;

public class ElderlyPersonDao {

    public void saveElderlyPerson(ElderlyPerson person) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        if (sf == null) {
            throw new RuntimeException("SessionFactory not available");
        }
        try {
            inTransaction(s -> s.persist(person));
        } catch (Exception e) {
            System.out.println("Hibernate save failed, operation ignored: " + e.getMessage());
            throw e; // Re-throw to indicate failure
        }
    }

    public List<ElderlyPerson> getAllElderly() {
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            if (sf == null) {
                System.out.println("ElderlyPersonDao.getAllElderly: no SessionFactory, returning empty list.");
                return new java.util.ArrayList<>();
            }
            Session s = sf.openSession();
            try {
                return (List<ElderlyPerson>) s.createQuery("from ElderlyPerson").list();
            } finally {
                s.close();
            }
        } catch (Exception e) {
            System.out.println("Hibernate query failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public ElderlyPerson findElderlyById(int id) {
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            if (sf == null) {
                System.out.println("ElderlyPersonDao.findElderlyById: no SessionFactory, returning null.");
                return null;
            }
            Session s = sf.openSession();
            try {
                return (ElderlyPerson) s.get(ElderlyPerson.class, id);
            } finally {
                s.close();
            }
        } catch (Exception e) {
            System.out.println("ElderlyPersonDao findElderlyById failed: " + e.getMessage());
            return null;
        }
    }

    public void updateElderlyPerson(ElderlyPerson person) {
        try {
            inTransaction(s -> s.merge(person));
        } catch (Exception e) {
            System.out.println("Hibernate update failed, operation ignored: " + e.getMessage());
        }
    }

    public void deleteElderly(int id) {
        try {
            inTransaction(s -> {
                ElderlyPerson person = (ElderlyPerson) s.get(ElderlyPerson.class, id);
                if (person != null) s.delete(person);
            });
        } catch (Exception e) {
            System.out.println("Hibernate delete failed, operation ignored: " + e.getMessage());
        }
    }

    private void inTransaction(java.util.function.Consumer<Session> work) {
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            if (sf == null) {
                System.out.println("ElderlyPersonDao.inTransaction: no SessionFactory, skipping work.");
                return;
            }
            Session s = sf.openSession();
            Transaction tx = s.beginTransaction();
            try {
                work.accept(s);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                System.out.println("Hibernate transaction failed: " + e.getMessage());
            } finally {
                s.close();
            }
        } catch (Exception e) {
            System.out.println("Hibernate transaction failed: " + e.getMessage());
            // Do not rethrow; allow service layer to continue using in-memory fallback.
        }
    }
}
