package model;

import java.io.Serializable;

/**
 * Shared HealthRecord model for RMI between client and server.
 * Mirrors the core server-side fields used in the dashboard.
 */
public class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private ElderlyPerson elderly;
    private String vitalSigns; // e.g., "BP:120/80; HR:72"
    private String notes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ElderlyPerson getElderly() { return elderly; }
    public void setElderly(ElderlyPerson elderly) { this.elderly = elderly; }

    public String getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(String vitalSigns) { this.vitalSigns = vitalSigns; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
