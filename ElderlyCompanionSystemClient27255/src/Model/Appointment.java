package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Shared Appointment model for RMI between client and server.
 * Mirrors the server-side entity shape (without JPA annotations).
 */
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    private Integer id;
    private ElderlyPerson elderly;
    private Caregiver caregiver;
    private LocalDateTime dateTime;
    private String purpose;
    private String medication;
    private Status status = Status.SCHEDULED;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ElderlyPerson getElderly() { return elderly; }
    public void setElderly(ElderlyPerson elderly) { this.elderly = elderly; }

    public Caregiver getCaregiver() { return caregiver; }
    public void setCaregiver(Caregiver caregiver) { this.caregiver = caregiver; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
