package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a scheduled appointment for a user.
 */
public class Appointment {
    private int appointmentId;
    private int userId;
    private String title;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String location;

    public Appointment() {}

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
