/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author USER
 */

@Entity
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional=false)
    private ElderlyPerson elderly;

    @ManyToOne(optional=false)
    private Caregiver caregiver;

    @Column(nullable=false)
    private LocalDateTime dateTime;

    @Column
    private String purpose;

    @Column
    private String medication;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;

    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    // getters/setters
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
