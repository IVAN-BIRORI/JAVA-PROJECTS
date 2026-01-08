/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.ElderlyPerson;
import model.Appointment;
import model.Caregiver;
import model.HealthRecord;

public interface ElderlyService extends Remote {
    // Elderly Person CRUD operations
    boolean addElderly(ElderlyPerson person) throws RemoteException;
    List<ElderlyPerson> getAllElderly() throws RemoteException;
    ElderlyPerson getElderlyById(int id) throws RemoteException;
    boolean updateElderly(ElderlyPerson person) throws RemoteException;
    boolean deleteElderly(int id) throws RemoteException;
    
    // Caregiver operations
    List<Caregiver> getAllCaregivers() throws RemoteException;
    Caregiver getCaregiverById(int id) throws RemoteException;
    
    // Appointment operations
    List<Appointment> getAppointmentsForElderly(int elderlyId) throws RemoteException;
    boolean bookAppointment(Appointment appointment) throws RemoteException;
    boolean updateAppointment(Appointment appointment) throws RemoteException;
    boolean cancelAppointment(int appointmentId) throws RemoteException;
    
    // Health Record operations
    List<HealthRecord> getHealthRecordsForElderly(int elderlyId) throws RemoteException;
    boolean updateHealthRecord(HealthRecord record) throws RemoteException;
}
