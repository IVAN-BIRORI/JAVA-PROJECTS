package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for navigating to different modules.
 */
public class Dashboard extends JFrame {
    public Dashboard(User user) {
        setTitle("ElderlyCare - Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton appointmentsBtn = new JButton("Appointments");
        JButton medicationsBtn = new JButton("Medications");
        JButton moodsBtn = new JButton("Mood Tracker");
        JButton contactsBtn = new JButton("Emergency Contacts");

        JPanel p = new JPanel(new GridLayout(2,2,10,10));
        p.add(appointmentsBtn); p.add(medicationsBtn);
        p.add(moodsBtn); p.add(contactsBtn);
        add(p);

        appointmentsBtn.addActionListener(e -> new AppointmentManager(user).setVisible(true));
        medicationsBtn.addActionListener(e -> new MedicationManager(user).setVisible(true));
        moodsBtn.addActionListener(e -> new MoodTracker(user).setVisible(true));
        contactsBtn.addActionListener(e -> new EmergencyContactsView(user).setVisible(true));
    }
}
