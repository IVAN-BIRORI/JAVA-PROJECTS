package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Lightweight Caregiver model used on the client side.
 */
public class Caregiver implements Serializable {
    // Must match server-side Caregiver serialVersionUID for RMI serialization
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;

    // Optional extra client-side fields
    private String phoneNumber;
    private String specialization;
    private String qualifications;

    private transient Set<Appointment> appointments = new HashSet<>();
    private transient Set<ElderlyPerson> elderlyPeople = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }

    public Set<Appointment> getAppointments() {
        if (appointments == null) {
            appointments = new HashSet<>();
        }
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<ElderlyPerson> getElderlyPeople() {
        if (elderlyPeople == null) {
            elderlyPeople = new HashSet<>();
        }
        return elderlyPeople;
    }

    public void setElderlyPeople(Set<ElderlyPerson> elderlyPeople) {
        this.elderlyPeople = elderlyPeople;
    }

    @Override
    public String toString() {
        return name + (specialization != null ? " (" + specialization + ")" : "");
    }
}
