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



import model.SessionToken;
import org.hibernate.Session;
import org.hibernate.Query;   // ✅ use legacy Query API for Hibernate 5.4
import java.time.LocalDateTime;
import java.util.List;

public class SessionTokenDao extends BaseDao {
    
    public void save(SessionToken t) { 
        inTransaction(s -> s.persist(t)); 
    }
    
    public boolean isValid(String token) {
        try {
            org.hibernate.SessionFactory sf = util.HibernateUtil.getSessionFactory();
            if (sf == null) {
                // No DB configured: simply say token is valid only if we never persisted.
                System.out.println("[SessionTokenDao] No SessionFactory – treating token as in-memory only.");
                return false;
            }

            Session s = sf.openSession();
            try {
                Query q = s.createQuery("from SessionToken t where t.token = :tok");
                q.setParameter("tok", token);
                @SuppressWarnings("unchecked")
                List<SessionToken> list = q.list();   // ✅ use list() instead of getResultList()

                if (list.isEmpty()) return false;

                SessionToken t = list.get(0);
                return t != null
                       && t.getExpiresAt() != null
                       && t.getExpiresAt().isAfter(LocalDateTime.now());
            } finally {
                s.close();
            }
        } catch (Exception e) {
            System.out.println("[SessionTokenDao] isValid failed, treating as invalid: " + e.getMessage());
            return false;
        }
    }
}
