package dao;

import model.Medication;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for Medications.
 * Similar style to your BankAccountDao: create, getAll, update, delete.
 */
public class MedicationDAO {

    // Create a new medication record
    public int createMedication(Medication m) {
        String sql = "INSERT INTO medications (user_id, med_name, dosage, reminder_time, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getUserId());
            ps.setString(2, m.getMedName());
            ps.setString(3, m.getDosage());
            ps.setTime(4, Time.valueOf(m.getReminderTime()));
            ps.setString(5, m.getNotes());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve all medications for a user
    public List<Medication> getAllMedications(int userId) {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE user_id = ? ORDER BY reminder_time";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Medication m = new Medication();
                m.setMedId(rs.getInt("med_id"));
                m.setUserId(rs.getInt("user_id"));
                m.setMedName(rs.getString("med_name"));
                m.setDosage(rs.getString("dosage"));
                m.setReminderTime(rs.getTime("reminder_time").toLocalTime());
                m.setNotes(rs.getString("notes"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update medication details
    public int updateMedication(Medication m) {
        String sql = "UPDATE medications SET med_name=?, dosage=?, reminder_time=?, notes=? WHERE med_id=?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getMedName());
            ps.setString(2, m.getDosage());
            ps.setTime(3, Time.valueOf(m.getReminderTime()));
            ps.setString(4, m.getNotes());
            ps.setInt(5, m.getMedId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Delete a medication
    public int deleteMedication(int medId) {
        String sql = "DELETE FROM medications WHERE med_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
