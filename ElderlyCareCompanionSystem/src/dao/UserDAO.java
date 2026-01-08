package dao;

import model.User;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for User entity.
 * Provides CRUD operations: create, read, update, delete.
 * Similar style to your BankAccountDao for consistency.
 */
public class UserDAO {

    // Create a new user
    public int createUser(User u) {
        String sql = "INSERT INTO users (name, email, password_hash, date_of_birth) VALUES (?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setDate(4, Date.valueOf(u.getDateOfBirth()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve a user by email (used for login)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return u;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update user details
    public int updateUser(User u) {
        String sql = "UPDATE users SET name=?, password_hash=?, date_of_birth=? WHERE email=?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getPasswordHash());
            ps.setDate(3, Date.valueOf(u.getDateOfBirth()));
            ps.setString(4, u.getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Delete a user by email
    public int deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
