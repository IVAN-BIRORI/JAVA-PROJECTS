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

import model.Appointment;
import org.hibernate.Session;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Query;

public class AppointmentDao extends BaseDao {
    
    public void save(Appointment a) { 
        try {
            inTransaction(s -> s.persist(a)); 
        } catch (Exception e) {
            System.out.println("AppointmentDao save failed, operation ignored: " + e.getMessage());
        }
    }
    
    public void update(Appointment a) { 
        try {
            inTransaction(s -> s.merge(a)); 
        } catch (Exception e) {
            System.out.println("AppointmentDao update failed, operation ignored: " + e.getMessage());
        }
    }
    
    public void delete(Integer id) {
        try {
            inTransaction(s -> {
                Appointment a = (Appointment) s.get(Appointment.class, id);
                if (a != null) s.delete(a);
            });
        } catch (Exception e) {
            System.out.println("AppointmentDao delete failed, operation ignored: " + e.getMessage());
        }
    }
    
    public List<Appointment> findByCaregiverOnDate(Integer caregiverId, LocalDateTime dayStart, LocalDateTime dayEnd) {
        try {
            Session s = util.HibernateUtil.getSessionFactory().openSession();
            try {
                Query q = s.createQuery("from Appointment a where a.caregiver.id = :cid and a.dateTime between :ds and :de");
                q.setParameter("cid", caregiverId);
                q.setParameter("ds", dayStart);
                q.setParameter("de", dayEnd);
                @SuppressWarnings("unchecked")
                List<Appointment> res = q.list();   // ✅ use list() instead of getResultList()
                return res;
            } finally { 
                s.close(); 
            }
        } catch (Exception e) {
            System.out.println("AppointmentDao findByCaregiverOnDate failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    public List<Appointment> findAll() {
        try {
            Session s = util.HibernateUtil.getSessionFactory().openSession();
            try {
                Query q = s.createQuery("from Appointment");
                @SuppressWarnings("unchecked")
                List<Appointment> res = q.list();   // ✅ use list()
                return res;
            } finally { 
                s.close(); 
            }
        } catch (Exception e) {
            System.out.println("AppointmentDao findAll failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    public List<Appointment> getAppointmentsForElderly(int elderlyId) {
        try {
            Session s = util.HibernateUtil.getSessionFactory().openSession();
            try {
                Query q = s.createQuery("from Appointment a where a.elderly.id = :eid");
                q.setParameter("eid", elderlyId);
                @SuppressWarnings("unchecked")
                List<Appointment> res = q.list();
                return res;
            } finally { 
                s.close(); 
            }
        } catch (Exception e) {
            System.out.println("AppointmentDao getAppointmentsForElderly failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
