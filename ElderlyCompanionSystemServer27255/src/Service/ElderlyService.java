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
    // Create a new elderly person record
    boolean addElderly(ElderlyPerson person) throws RemoteException;

    // Retrieve all elderly persons
    List<ElderlyPerson> getAllElderly() throws RemoteException;

    // Find elderly person by ID
    ElderlyPerson getElderlyById(int id) throws RemoteException;

    // Update elderly person record
    boolean updateElderly(ElderlyPerson person) throws RemoteException;

    // Delete elderly person record
    boolean deleteElderly(int id) throws RemoteException;

    // Appointments
    List<Appointment> getAppointmentsForElderly(int elderlyId) throws RemoteException;
    boolean bookAppointment(Appointment appointment) throws RemoteException;
    boolean updateAppointment(Appointment appointment) throws RemoteException;
    boolean cancelAppointment(int appointmentId) throws RemoteException;

    // Caregivers
    List<Caregiver> getAllCaregivers() throws RemoteException;
    Caregiver getCaregiverById(int id) throws RemoteException;

    // Health Records
    List<HealthRecord> getHealthRecordsForElderly(int elderlyId) throws RemoteException;
    boolean updateHealthRecord(HealthRecord record) throws RemoteException;
}
