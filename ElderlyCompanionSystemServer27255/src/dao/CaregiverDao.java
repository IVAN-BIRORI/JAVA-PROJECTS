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

import model.Caregiver;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class CaregiverDao extends BaseDao {
    public void save(Caregiver c) { 
        try {
            inTransaction(s -> s.persist(c)); 
        } catch (Exception e) {
            System.out.println("CaregiverDao save failed, operation ignored: " + e.getMessage());
        }
    }
    public void update(Caregiver c) { 
        try {
            inTransaction(s -> s.merge(c)); 
        } catch (Exception e) {
            System.out.println("CaregiverDao update failed, operation ignored: " + e.getMessage());
        }
    }
    public void delete(Integer id) {
        try {
            inTransaction(s -> {
                Caregiver c = (Caregiver) s.get(Caregiver.class, id);
                if (c != null) s.delete(c);
            });
        } catch (Exception e) {
            System.out.println("CaregiverDao delete failed, operation ignored: " + e.getMessage());
        }
    }
    public Caregiver findById(Integer id) {
        try {
            Session s = util.HibernateUtil.getSessionFactory().openSession();
            try { return (Caregiver) s.get(Caregiver.class, id); } finally { s.close(); }
        } catch (Exception e) {
            System.out.println("CaregiverDao findById failed: " + e.getMessage());
            return null;
        }
    }
    public List<Caregiver> findAll() {
        try {
            Session s = util.HibernateUtil.getSessionFactory().openSession();
            try {
                org.hibernate.query.Query q;
                q = (org.hibernate.query.Query) s.createQuery("from Caregiver");
                @SuppressWarnings("unchecked")
                java.util.List<Caregiver> res = (java.util.List<Caregiver>) q.getResultList();
                return res;
            } finally { s.close(); }
        } catch (Exception e) {
            System.out.println("CaregiverDao findAll failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
