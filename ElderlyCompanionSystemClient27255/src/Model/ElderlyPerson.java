package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Simplified ElderlyPerson model shared with the server via RMI.
 */
public class ElderlyPerson implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String nationalId;
    private String contact; // phone/email for communication

    private transient Set<Caregiver> caregivers = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Set<Caregiver> getCaregivers() {
        if (caregivers == null) {
            caregivers = new HashSet<>();
        }
        return caregivers;
    }

    public void setCaregivers(Set<Caregiver> caregivers) {
        this.caregivers = caregivers;
    }
}
