package model;

import java.time.LocalTime;

/**
 * Represents a medication reminder for a user.
 */
public class Medication {
    private int medId;
    private int userId;
    private String medName;
    private String dosage;
    private LocalTime reminderTime;
    private String notes;

    public Medication() {}

    public int getMedId() { return medId; }
    public void setMedId(int medId) { this.medId = medId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMedName() { return medName; }
    public void setMedName(String medName) { this.medName = medName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
