/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author USER
 */
// Server: model/HealthRecord.java

@Entity
public class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional=false)
    private ElderlyPerson elderly;

    @Column(nullable=false)
    private String vitalSigns; // e.g., "BP:120/80; HR:72"

    @Column(nullable=false)
    private String notes;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ElderlyPerson getElderly() { return elderly; }
    public void setElderly(ElderlyPerson elderly) { this.elderly = elderly; }

    public String getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(String vitalSigns) { this.vitalSigns = vitalSigns; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
