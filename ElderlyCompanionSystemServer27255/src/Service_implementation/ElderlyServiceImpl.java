/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_implementation;

/**
 *
 * @author USER
 */

import Service.ElderlyService;
import dao.AccountDao;
import dao.AppointmentDao;
import dao.CaregiverDao;
import dao.ElderlyPersonDao;
import dao.HealthRecordDao;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import model.Account;
import model.Appointment;
import model.Caregiver;
import model.ElderlyPerson;
import model.HealthRecord;
import util.HibernateUtil;

public class ElderlyServiceImpl extends UnicastRemoteObject implements ElderlyService {

    // In-memory fallbacks when Hibernate/DB is not available
    private List<ElderlyPerson> elderlyList = new ArrayList<>();
    private List<Caregiver> caregiverList = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();
    private List<HealthRecord> healthList = new ArrayList<>();

    private AtomicInteger idCounter = new AtomicInteger(1);
    private AtomicInteger caregiverIdCounter = new AtomicInteger(1);
    private AtomicInteger appointmentIdCounter = new AtomicInteger(1);
    private AtomicInteger healthIdCounter = new AtomicInteger(1);

    private ElderlyPersonDao elderlyDao;
    private AccountDao accountDao;
    private CaregiverDao caregiverDao;
    private AppointmentDao appointmentDao;
    private HealthRecordDao healthDao;

    public ElderlyServiceImpl() throws RemoteException {
        super();
        // DAOs will be initialized lazily
        System.out.println("ElderlyServiceImpl initialized - DAOs will be created lazily");
    }

    private boolean isDbAvailable() {
        try {
            return HibernateUtil.getSessionFactory() != null;
        } catch (Throwable t) {
            return false;
        }
    }

    private ElderlyPersonDao getElderlyDao() {
        if (elderlyDao == null) {
            if (!isDbAvailable()) {
                // No real database behind Hibernate; use in-memory lists instead.
                System.out.println("ElderlyServiceImpl: no DB available, using in-memory elderly list.");
                return null;
            }
            try {
                elderlyDao = new ElderlyPersonDao();
            } catch (Exception e) {
                System.out.println("Failed to initialize ElderlyPersonDao: " + e.getMessage());
                elderlyDao = null;
            }
        }
        return elderlyDao;
    }

    private AccountDao getAccountDao() {
        if (accountDao == null) {
            if (!isDbAvailable()) {
                System.out.println("ElderlyServiceImpl: no DB available, AccountDao disabled (in-memory only).");
                return null;
            }
            try {
                accountDao = new AccountDao();
            } catch (Exception e) {
                System.out.println("Failed to initialize AccountDao: " + e.getMessage());
                accountDao = null;
            }
        }
        return accountDao;
    }

    private CaregiverDao getCaregiverDao() {
        if (caregiverDao == null) {
            if (!isDbAvailable()) {
                System.out.println("ElderlyServiceImpl: no DB available, CaregiverDao disabled (in-memory only).");
                return null;
            }
            try {
                caregiverDao = new CaregiverDao();
            } catch (Exception e) {
                System.out.println("Failed to initialize CaregiverDao: " + e.getMessage());
                caregiverDao = null;
            }
        }
        return caregiverDao;
    }

    private AppointmentDao getAppointmentDao() {
        if (appointmentDao == null) {
            if (!isDbAvailable()) {
                System.out.println("ElderlyServiceImpl: no DB available, AppointmentDao disabled (in-memory only).");
                return null;
            }
            try {
                appointmentDao = new AppointmentDao();
            } catch (Exception e) {
                System.out.println("Failed to initialize AppointmentDao: " + e.getMessage());
                appointmentDao = null;
            }
        }
        return appointmentDao;
    }

    private HealthRecordDao getHealthDao() {
        if (healthDao == null) {
            if (!isDbAvailable()) {
                System.out.println("ElderlyServiceImpl: no DB available, HealthRecordDao disabled (in-memory only).");
                return null;
            }
            try {
                healthDao = new HealthRecordDao();
            } catch (Exception e) {
                System.out.println("Failed to initialize HealthRecordDao: " + e.getMessage());
                healthDao = null;
            }
        }
        return healthDao;
    }

    @Override
    public boolean addElderly(ElderlyPerson person) throws RemoteException {
        try {
            if (getElderlyDao() != null) {
                try {
                    // Use Hibernate
                    getElderlyDao().saveElderlyPerson(person);

                    // Create Account
                    if (getAccountDao() != null) {
                        Account account = new Account();
                        account.setElderlyPerson(person);
                        account.setNumber("ACC" + person.getId());
                        account.setBalance(BigDecimal.ZERO);
                        getAccountDao().save(account);
                    }

                    // Assign Caregiver (find first or create)
                    if (getCaregiverDao() != null) {
                        List<Caregiver> caregivers = getCaregiverDao().findAll();
                        Caregiver caregiver;
                        if (caregivers.isEmpty()) {
                            caregiver = new Caregiver();
                            caregiver.setName("Default Caregiver");
                            caregiver.setEmail("default@example.com");
                            getCaregiverDao().save(caregiver);
                        } else {
                            caregiver = caregivers.get(0);
                        }
                        person.getCaregivers().add(caregiver);
                        getElderlyDao().updateElderlyPerson(person); // Update relationships
                    }

                    // Create Appointment
                    if (getAppointmentDao() != null) {
                        Appointment appointment = new Appointment();
                        appointment.setElderly(person);
                        appointment.setCaregiver(person.getCaregivers().isEmpty() ? null : person.getCaregivers().iterator().next());
                        appointment.setDateTime(java.time.LocalDateTime.now());
                        appointment.setStatus(Appointment.Status.SCHEDULED);
                        getAppointmentDao().save(appointment);
                    }

                    // Create Health Record
                    if (getHealthDao() != null) {
                        HealthRecord health = new HealthRecord();
                        health.setElderly(person);
                        health.setVitalSigns("N/A");
                        health.setNotes("Initial health check required.");
                        getHealthDao().save(health);
                    }
                    return true;
                } catch (Exception e) {
                    System.out.println("DB save failed, falling back to in-memory: " + e.getMessage());
                    // Fall back to in-memory
                    person.setId(idCounter.getAndIncrement());
                    elderlyList.add(person);
                    System.out.println("Added elderly person in-memory: " + person.getName());
                }
            } 

            // In-memory branch (either no DAO or DB failed above)
            if (person.getId() == null) {
                person.setId(idCounter.getAndIncrement());
            }
            if (!elderlyList.contains(person)) {
                elderlyList.add(person);
            }
            System.out.println("Ensured elderly person in-memory: " + person.getName());

            // In-memory default caregiver
            Caregiver caregiver;
            if (caregiverList.isEmpty()) {
                caregiver = new Caregiver();
                caregiver.setId(caregiverIdCounter.getAndIncrement());
                caregiver.setName("Default Caregiver");
                caregiver.setEmail("default@example.com");
                caregiverList.add(caregiver);
            } else {
                caregiver = caregiverList.get(0);
            }
            person.getCaregivers().add(caregiver);

            // In-memory initial appointment
            Appointment appointment = new Appointment();
            appointment.setId(appointmentIdCounter.getAndIncrement());
            appointment.setElderly(person);
            appointment.setCaregiver(caregiver);
            appointment.setDateTime(java.time.LocalDateTime.now());
            appointment.setStatus(Appointment.Status.SCHEDULED);
            appointmentList.add(appointment);

            // In-memory initial health record
            HealthRecord health = new HealthRecord();
            health.setId(healthIdCounter.getAndIncrement());
            health.setElderly(person);
            health.setVitalSigns("N/A");
            health.setNotes("Initial health check required.");
            healthList.add(health);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ElderlyPerson> getAllElderly() throws RemoteException {
        try {
            if (getElderlyDao() != null) {
                try {
                    return getElderlyDao().getAllElderly();
                } catch (Exception e) {
                    System.out.println("DB query failed, falling back to in-memory: " + e.getMessage());
                    return new ArrayList<>(elderlyList);
                }
            } else {
                // Return in-memory list
                return new ArrayList<>(elderlyList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public ElderlyPerson getElderlyById(int id) throws RemoteException {
        try {
            if (getElderlyDao() != null) {
                return getElderlyDao().findElderlyById(id);
            } else {
                // Search in-memory list
                return elderlyList.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateElderly(ElderlyPerson person) throws RemoteException {
        try {
            if (getElderlyDao() != null) {
                getElderlyDao().updateElderlyPerson(person);
            } else {
                // Update in-memory list
                for (int i = 0; i < elderlyList.size(); i++) {
                    if (elderlyList.get(i).getId() == person.getId()) {
                        elderlyList.set(i, person);
                        break;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteElderly(int id) throws RemoteException {
        try {
            if (getElderlyDao() != null) {
                getElderlyDao().deleteElderly(id);
            } else {
                // Remove from in-memory lists
                elderlyList.removeIf(e -> e.getId() == id);
                appointmentList.removeIf(a -> a.getElderly() != null && a.getElderly().getId() == id);
                healthList.removeIf(h -> h.getElderly() != null && h.getElderly().getId() == id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Appointments
    @Override
    public List<Appointment> getAppointmentsForElderly(int elderlyId) throws RemoteException {
        try {
            if (getAppointmentDao() != null) {
                return getAppointmentDao().getAppointmentsForElderly(elderlyId);
            } else {
                // Return in-memory appointments for this elderly
                List<Appointment> res = new ArrayList<>();
                for (Appointment a : appointmentList) {
                    if (a.getElderly() != null && a.getElderly().getId() == elderlyId) {
                        res.add(a);
                    }
                }
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean bookAppointment(Appointment appointment) throws RemoteException {
        try {
            if (getAppointmentDao() != null) {
                getAppointmentDao().save(appointment);
            } else {
                // Store in in-memory list
                if (appointment.getId() == null) {
                    appointment.setId(appointmentIdCounter.getAndIncrement());
                }
                appointmentList.add(appointment);
                System.out.println("Appointment booked in-memory: " + appointment);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAppointment(Appointment appointment) throws RemoteException {
        try {
            if (getAppointmentDao() != null) {
                getAppointmentDao().update(appointment);
            } else {
                // Update in-memory list
                for (int i = 0; i < appointmentList.size(); i++) {
                    if (appointmentList.get(i).getId() == appointment.getId()) {
                        appointmentList.set(i, appointment);
                        break;
                    }
                }
                System.out.println("Appointment updated in-memory: " + appointment);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelAppointment(int appointmentId) throws RemoteException {
        try {
            if (getAppointmentDao() != null) {
                getAppointmentDao().delete(appointmentId);
            } else {
                // Remove from in-memory list
                appointmentList.removeIf(a -> a.getId() == appointmentId);
                System.out.println("Appointment cancelled in-memory: " + appointmentId);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Caregivers
    @Override
    public List<Caregiver> getAllCaregivers() throws RemoteException {
        try {
            if (getCaregiverDao() != null) {
                return getCaregiverDao().findAll();
            } else {
                // Return in-memory caregivers
                return new ArrayList<>(caregiverList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Caregiver getCaregiverById(int id) throws RemoteException {
        try {
            if (getCaregiverDao() != null) {
                return getCaregiverDao().findById(id);
            } else {
                // Search in-memory caregivers
                for (Caregiver c : caregiverList) {
                    if (c.getId() != null && c.getId() == id) {
                        return c;
                    }
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Health Records
    @Override
    public List<HealthRecord> getHealthRecordsForElderly(int elderlyId) throws RemoteException {
        try {
            if (getHealthDao() != null) {
                return getHealthDao().getHealthRecordsForElderly(elderlyId);
            } else {
                // Return in-memory health records for this elderly
                List<HealthRecord> res = new ArrayList<>();
                for (HealthRecord h : healthList) {
                    if (h.getElderly() != null && h.getElderly().getId() == elderlyId) {
                        res.add(h);
                    }
                }
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateHealthRecord(HealthRecord record) throws RemoteException {
        try {
            if (getHealthDao() != null) {
                getHealthDao().update(record);
            } else {
                // Update in-memory list
                boolean updated = false;
                for (int i = 0; i < healthList.size(); i++) {
                    if (healthList.get(i).getId() == record.getId()) {
                        healthList.set(i, record);
                        updated = true;
                        break;
                    }
                }
                if (!updated) {
                    if (record.getId() == null) {
                        record.setId(healthIdCounter.getAndIncrement());
                    }
                    healthList.add(record);
                }
                System.out.println("Health record updated in-memory: " + record);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
