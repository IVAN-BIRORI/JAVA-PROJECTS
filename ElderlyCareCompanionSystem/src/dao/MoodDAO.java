package dao;

import model.Mood;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations for Mood tracking.
 */
public class MoodDAO {

    // Create a new mood entry
    public int createMood(Mood m) {
        String sql = "INSERT INTO moods (user_id, mood_level, note) VALUES (?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getUserId());
            ps.setString(2, m.getMoodLevel());
            ps.setString(3, m.getNote());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Retrieve all moods for a user
    public List<Mood> getAllMoods(int userId) {
        List<Mood> list = new ArrayList<>();
        String sql = "SELECT * FROM moods WHERE user_id = ? ORDER BY logged_at DESC";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mood m = new Mood();
                m.setMoodId(rs.getInt("mood_id"));
                m.setUserId(rs.getInt("user_id"));
                m.setMoodLevel(rs.getString("mood_level"));
                m.setNote(rs.getString("note"));
                m.setLoggedAt(rs.getTimestamp("logged_at").toLocalDateTime());
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Delete a mood entry
    public int deleteMood(int moodId) {
        String sql = "DELETE FROM moods WHERE mood_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, moodId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
