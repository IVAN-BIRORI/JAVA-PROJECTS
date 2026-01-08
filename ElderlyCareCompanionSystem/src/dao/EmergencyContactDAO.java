package dao;

import model.EmergencyContact;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for Emergency Contacts.
 */
public class EmergencyContactDAO {

    // Create a new emergency contact
    public int createContact(EmergencyContact c) {
        String sql = "INSERT INTO emergency_contacts (user_id, name, relationship, phone_number, priority) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getRelationship());
            ps.setString(4, c.getPhoneNumber());
            ps.setInt(5, c.getPriority());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve all contacts for a user
    public List<EmergencyContact> getAllContacts(int userId) {
        List<EmergencyContact> list = new ArrayList<>();
        String sql = "SELECT * FROM emergency_contacts WHERE user_id = ? ORDER BY priority ASC";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EmergencyContact c = new EmergencyContact();
                c.setContactId(rs.getInt("contact_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setName(rs.getString("name"));
                c.setRelationship(rs.getString("relationship"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setPriority(rs.getInt("priority"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Delete a contact
    public int deleteContact(int contactId) {
        String sql = "DELETE FROM emergency_contacts WHERE contact_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, contactId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
