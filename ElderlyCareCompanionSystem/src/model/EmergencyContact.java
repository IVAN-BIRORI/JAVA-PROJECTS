package model;

/**
 * Represents an emergency contact for a user.
 */
public class EmergencyContact {
    private int contactId;
    private int userId;
    private String name;
    private String relationship;
    private String phoneNumber;
    private int priority;

    public EmergencyContact() {}

    public int getContactId() { return contactId; }
    public void setContactId(int contactId) { this.contactId = contactId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
