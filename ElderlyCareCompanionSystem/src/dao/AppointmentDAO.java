package dao;

import model.Appointment;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Appointment entity.
 * Provides CRUD operations: create, read, update, delete.
 * Mirrors the style of your BankAccountDao for clarity.
 */
public class AppointmentDAO {

    // Create a new appointment
    public int createAppointment(Appointment a) {
        String sql = "INSERT INTO appointments (user_id, title, appointment_date, appointment_time, location) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getTitle());
            ps.setDate(3, Date.valueOf(a.getAppointmentDate()));
            ps.setTime(4, Time.valueOf(a.getAppointmentTime()));
            ps.setString(5, a.getLocation());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve all appointments for a user
    public List<Appointment> getAllAppointments(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE user_id = ? ORDER BY appointment_date, appointment_time";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setTitle(rs.getString("title"));
                a.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
                a.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
                a.setLocation(rs.getString("location"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update an appointment
    public int updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET title=?, appointment_date=?, appointment_time=?, location=? WHERE appointment_id=?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setDate(2, Date.valueOf(a.getAppointmentDate()));
            ps.setTime(3, Time.valueOf(a.getAppointmentTime()));
            ps.setString(4, a.getLocation());
            ps.setInt(5, a.getAppointmentId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Delete an appointment
    public int deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
