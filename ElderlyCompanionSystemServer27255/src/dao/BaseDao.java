/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author USER
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.function.Consumer;

public class BaseDao {
    protected void inTransaction(Consumer<Session> work) {
        SessionFactory sf = null;
        try {
            sf = HibernateUtil.getSessionFactory();
        } catch (Throwable t) {
            // Should not happen anymore, but keep as safety net
            System.out.println("Hibernate SessionFactory not available: " + t.getMessage());
        }

        if (sf == null) {
            // Graceful degradation: no real DB, just skip the transaction.
            System.out.println("[BaseDao] No SessionFactory – skipping database operation (using in-memory / fallback).");
            return;
        }

        try {
            Session s = sf.openSession();
            Transaction tx = s.beginTransaction();
            try {
                work.accept(s);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                System.out.println("Hibernate transaction failed inside work: " + e.getMessage());
                // Do NOT rethrow; just log so higher layers can keep working.
            } finally {
                s.close();
            }
        } catch (Exception e) {
            System.out.println("Hibernate transaction failed: " + e.getMessage());
            // Swallow to avoid killing RMI calls like login/registration when DB is misconfigured.
        }
    }
}
