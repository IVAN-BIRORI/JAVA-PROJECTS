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

import model.HealthRecord;
import org.hibernate.Session;
import org.hibernate.Query;
import java.util.List;

public class HealthRecordDao extends BaseDao {

    public HealthRecord getHealthRecordById(int id) {
        try {
            Session session = util.HibernateUtil.getSessionFactory().openSession();
            try {
                return (HealthRecord) session.get(HealthRecord.class, id);
            } finally {
                session.close();
            }
        } catch (Exception e) {
            System.out.println("HealthRecordDao getHealthRecordById failed: " + e.getMessage());
            return null;
        }
    }
    
    public void save(HealthRecord healthRecord) {
        try {
            inTransaction(session -> session.save(healthRecord));
        } catch (Exception e) {
            System.out.println("HealthRecordDao save failed, operation ignored: " + e.getMessage());
        }
    }
    
    public void update(HealthRecord healthRecord) {
        try {
            inTransaction(session -> session.update(healthRecord));
        } catch (Exception e) {
            System.out.println("HealthRecordDao update failed, operation ignored: " + e.getMessage());
        }
    }
    
    public void delete(int id) {
        try {
            inTransaction(session -> {
                HealthRecord healthRecord = (HealthRecord) session.get(HealthRecord.class, id);
                if (healthRecord != null) {
                    session.delete(healthRecord);
                }
            });
        } catch (Exception e) {
            System.out.println("HealthRecordDao delete failed, operation ignored: " + e.getMessage());
        }
    }
    
    public List<HealthRecord> getHealthRecordsForElderly(int elderlyId) {
        try {
            Session session = util.HibernateUtil.getSessionFactory().openSession();
            try {
                Query q = session.createQuery("from HealthRecord hr where hr.elderly.id = :elderlyId");
                q.setParameter("elderlyId", elderlyId);
                @SuppressWarnings("unchecked")
                List<HealthRecord> res = q.list();
                return res;
            } finally {
                session.close();
            }
        } catch (Exception e) {
            System.out.println("HealthRecordDao getHealthRecordsForElderly failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
    
    public List<HealthRecord> getAllHealthRecords() {
        try {
            Session session = util.HibernateUtil.getSessionFactory().openSession();
            try {
                Query q = session.createQuery("from HealthRecord");
                @SuppressWarnings("unchecked")
                List<HealthRecord> res = q.list();
                return res;
            } finally {
                session.close();
            }
        } catch (Exception e) {
            System.out.println("HealthRecordDao getAllHealthRecords failed, returning empty list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}