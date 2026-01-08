package model;

import java.time.LocalDateTime;

/**
 * Represents a mood entry logged by a user.
 */
public class Mood {
    private int moodId;
    private int userId;
    private String moodLevel;
    private String note;
    private LocalDateTime loggedAt;

    public Mood() {}

    public int getMoodId() { return moodId; }
    public void setMoodId(int moodId) { this.moodId = moodId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMoodLevel() { return moodLevel; }
    public void setMoodLevel(String moodLevel) { this.moodLevel = moodLevel; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}
