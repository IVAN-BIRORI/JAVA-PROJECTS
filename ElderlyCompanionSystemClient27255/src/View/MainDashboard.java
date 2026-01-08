/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

/**
 *
 * @author USER
 */

import controller.ClientConnector;
import Service.ElderlyService;
import model.Appointment;
import model.Caregiver;
import model.HealthRecord;
import model.ElderlyPerson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainDashboard extends JFrame {
    private ElderlyService elderlyService;
    private int currentElderlyId; // Assume we get this from login

    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel appointmentPanel;
    private JPanel medicationPanel;
    private JPanel healthPanel;
    private JPanel profilePanel;
    private JPanel reportPanel;
    
    private JTextArea appointmentArea;
    private JTextArea medArea;
    private JTextArea healthArea;

    // Profile fields
    private JTextField profileNameField;
    private JTextField profileContactField;

    public MainDashboard(int elderlyId) {
        this.currentElderlyId = elderlyId;
        try {
            elderlyService = ClientConnector.elderlyService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection error: " + e.getMessage() + "\nPlease ensure the server is running.");
            dispose();
            return;
        }

        setTitle("Elderly Companion System - Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Top-level menu so user can choose actions (appointments, elderly care, registration, reports)
        JMenuBar menuBar = new JMenuBar();

        JMenu menuActions = new JMenu("Actions");
        JMenuItem miAppointments = new JMenuItem("Appointments");
        JMenuItem miElderlyRegistration = new JMenuItem("Elderly Registration");
        JMenuItem miHealth = new JMenuItem("Health Records");
        JMenuItem miReports = new JMenuItem("Reports");

        miAppointments.addActionListener(e -> tabbedPane.setSelectedComponent(appointmentPanel));
        miHealth.addActionListener(e -> tabbedPane.setSelectedComponent(healthPanel));
        miReports.addActionListener(e -> tabbedPane.setSelectedComponent(reportPanel));
        miElderlyRegistration.addActionListener(e -> {
            // Open registration form so user can insert elderly data into the database
            new ElderlyRegistrationForm().setVisible(true);
        });

        menuActions.add(miAppointments);
        menuActions.add(miHealth);
        menuActions.add(miReports);
        menuActions.addSeparator();
        menuActions.add(miElderlyRegistration);

        JMenu menuSession = new JMenu("Session");
        JMenuItem miLogout = new JMenuItem("Logout");
        miLogout.addActionListener(e -> {
            LoginForm.SESSION_TOKEN = null;
            new LoginForm().setVisible(true);
            dispose();
        });
        menuSession.add(miLogout);

        menuBar.add(menuActions);
        menuBar.add(menuSession);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();

        createAppointmentPanel();
        createMedicationPanel();
        createHealthPanel();
        createProfilePanel();
        createReportPanel();

        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Medications", medicationPanel);
        tabbedPane.addTab("Health Records", healthPanel);
        tabbedPane.addTab("Profile", profilePanel);
        tabbedPane.addTab("Reports", reportPanel);

        add(tabbedPane);
    }

    private void createAppointmentPanel() {
        appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBackground(new Color(240, 248, 255));

        // Top panel for booking
        JPanel bookPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        bookPanel.setBorder(BorderFactory.createTitledBorder("Book Appointment"));
        bookPanel.setBackground(new Color(240, 248, 255));

        JTextField caregiverField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd HH:mm");
        JTextField purposeField = new JTextField();
        JTextField medicationField = new JTextField();
        JButton bookBtn = new JButton("Book Appointment");

        bookPanel.add(new JLabel("Caregiver Name:"));
        bookPanel.add(caregiverField);
        bookPanel.add(new JLabel("Date & Time:"));
        bookPanel.add(dateField);
        bookPanel.add(new JLabel("Purpose:"));
        bookPanel.add(purposeField);
        bookPanel.add(new JLabel("Medication:"));
        bookPanel.add(medicationField);
        bookPanel.add(new JLabel(""));
        bookPanel.add(bookBtn);

        bookBtn.addActionListener(e -> {
            try {
                String caregiverName = caregiverField.getText().trim();
                if (caregiverName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Caregiver name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(dateField.getText(), formatter);

                Appointment app = new Appointment();
                app.setElderly(elderlyService.getElderlyById(currentElderlyId));

                // Allow user to type any caregiver name; create caregiver object on the fly.
                Caregiver caregiver = new Caregiver();
                caregiver.setName(caregiverName);
                caregiver.setEmail(caregiverName.toLowerCase().replace(" ", "") + "@example.com");
                app.setCaregiver(caregiver);

                app.setDateTime(dateTime);
                app.setPurpose(purposeField.getText());
                app.setMedication(medicationField.getText());

                if (elderlyService.bookAppointment(app)) {
                    JOptionPane.showMessageDialog(this, "Appointment booked!");
                    loadAppointments();
                    loadMedications();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Bottom panel for viewing
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(BorderFactory.createTitledBorder("My Appointments"));
        appointmentArea = new JTextArea();
        appointmentArea.setEditable(false);
        viewPanel.add(new JScrollPane(appointmentArea), BorderLayout.CENTER);

        appointmentPanel.add(bookPanel, BorderLayout.NORTH);
        appointmentPanel.add(viewPanel, BorderLayout.CENTER);

        loadAppointments();
    }

    private void loadAppointments() {
        try {
            List<Appointment> apps = elderlyService.getAppointmentsForElderly(currentElderlyId);
            StringBuilder sb = new StringBuilder();
            for (Appointment app : apps) {
                sb.append("Caregiver: ").append(app.getCaregiver().getName())
                  .append(", Date: ").append(app.getDateTime())
                  .append(", Purpose: ").append(app.getPurpose())
                  .append(", Medication: ").append(app.getMedication())
                  .append("\n");
            }
            appointmentArea.setText(sb.toString());
        } catch (Exception e) {
            appointmentArea.setText("Error loading appointments: " + e.getMessage());
        }
    }

    private void createMedicationPanel() {
        medicationPanel = new JPanel(new BorderLayout());
        medicationPanel.setBackground(new Color(240, 248, 255));
        medicationPanel.setBorder(BorderFactory.createTitledBorder("Recommended Medications"));

        medArea = new JTextArea();
        medArea.setEditable(false);
        medicationPanel.add(new JScrollPane(medArea), BorderLayout.CENTER);

        // Load medications from health records or appointments
        loadMedications();
    }

    private void loadMedications() {
        try {
            List<Appointment> apps = elderlyService.getAppointmentsForElderly(currentElderlyId);
            StringBuilder sb = new StringBuilder();
            for (Appointment app : apps) {
                if (app.getMedication() != null && !app.getMedication().isEmpty()) {
                    sb.append("Appointment: ").append(app.getPurpose())
                      .append(", Medication: ").append(app.getMedication())
                      .append("\n");
                }
            }
            medArea.setText(sb.toString());
        } catch (Exception e) {
            medArea.setText("Error loading medications: " + e.getMessage());
        }
    }

    private void createHealthPanel() {
        healthPanel = new JPanel(new BorderLayout());
        healthPanel.setBackground(new Color(240, 248, 255));
        healthPanel.setBorder(BorderFactory.createTitledBorder("Health Records"));

        healthArea = new JTextArea();
        healthArea.setEditable(false);
        healthPanel.add(new JScrollPane(healthArea), BorderLayout.CENTER);

        loadHealthRecords();
    }

    private void loadHealthRecords() {
        try {
            List<HealthRecord> records = elderlyService.getHealthRecordsForElderly(currentElderlyId);
            StringBuilder sb = new StringBuilder();
            for (HealthRecord rec : records) {
                sb.append("Vital Signs: ").append(rec.getVitalSigns())
                  .append(", Notes: ").append(rec.getNotes())
                  .append("\n");
            }
            healthArea.setText(sb.toString());
        } catch (Exception e) {
            healthArea.setText("Error loading health records: " + e.getMessage());
        }
    }

    private void createProfilePanel() {
        profilePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        profilePanel.setBackground(new Color(240, 248, 255));
        profilePanel.setBorder(BorderFactory.createTitledBorder("My Profile"));

        profileNameField = new JTextField();
        profileContactField = new JTextField();
        JButton updateBtn = new JButton("Update Profile");
        JButton deleteBtn = new JButton("Delete Profile");
        JButton refreshBtn = new JButton("Refresh");

        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(profileNameField);
        profilePanel.add(new JLabel("Contact:"));
        profilePanel.add(profileContactField);
        profilePanel.add(updateBtn);
        profilePanel.add(deleteBtn);
        profilePanel.add(new JLabel(""));
        profilePanel.add(refreshBtn);

        // Load current profile
        loadProfile();

        updateBtn.addActionListener(e -> {
            try {
                ElderlyPerson person = elderlyService.getElderlyById(currentElderlyId);
                if (person == null) {
                    JOptionPane.showMessageDialog(this, "No profile found to update.");
                    return;
                }
                String newName = profileNameField.getText().trim();
                String newContact = profileContactField.getText().trim();
                if (newName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!newContact.matches("^\\+?\\d{10,13}$")) {
                    JOptionPane.showMessageDialog(this, "Contact must be a valid phone number (10–13 digits).", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                person.setName(newName);
                person.setContact(newContact);
                if (elderlyService.updateElderly(person)) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update profile.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this profile?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                if (elderlyService.deleteElderly(currentElderlyId)) {
                    JOptionPane.showMessageDialog(this, "Profile deleted.");
                    // After delete, return to login screen
                    new LoginForm().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete profile.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting profile: " + ex.getMessage());
            }
        });

        refreshBtn.addActionListener(e -> loadProfile());
    }

    private void loadProfile() {
        try {
            ElderlyPerson person = elderlyService.getElderlyById(currentElderlyId);
            if (person != null) {
                profileNameField.setText(person.getName());
                profileContactField.setText(person.getContact());
            }
        } catch (Exception e) {
            // Show minimal message but don't block UI
            System.out.println("Error loading profile: " + e.getMessage());
        }
    }

    private void createReportPanel() {
        reportPanel = new JPanel(new FlowLayout());
        reportPanel.setBackground(new Color(240, 248, 255));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Generate Reports"));

        JButton pdfBtn = new JButton("Export to PDF");
        JButton excelBtn = new JButton("Export to Excel");
        JButton csvBtn = new JButton("Export to CSV");

        // Disable PDF export to avoid dependency warnings; Excel/CSV remain fully functional.
        pdfBtn.setEnabled(false);
        pdfBtn.setToolTipText("PDF export disabled (use Excel or CSV)");

        excelBtn.addActionListener(e -> {
            try {
                List<ElderlyPerson> elderly = elderlyService.getAllElderly();
                String[] columns = {"ID", "Name", "National ID", "Contact"};
                Object[][] data = new Object[elderly.size()][4];
                for (int i = 0; i < elderly.size(); i++) {
                    ElderlyPerson p = elderly.get(i);
                    data[i][0] = p.getId();
                    data[i][1] = p.getName();
                    data[i][2] = p.getNationalId();
                    data[i][3] = p.getContact();
                }
                JTable table = new JTable(data, columns);
                ExcelExporter.exportExcel(table);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        csvBtn.addActionListener(e -> {
            try {
                List<ElderlyPerson> elderly = elderlyService.getAllElderly();
                String[] columns = {"ID", "Name", "National ID", "Contact"};
                Object[][] data = new Object[elderly.size()][4];
                for (int i = 0; i < elderly.size(); i++) {
                    ElderlyPerson p = elderly.get(i);
                    data[i][0] = p.getId();
                    data[i][1] = p.getName();
                    data[i][2] = p.getNationalId();
                    data[i][3] = p.getContact();
                }
                JTable table = new JTable(data, columns);
                ReportExporter.exportCsv(table);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        reportPanel.add(pdfBtn);
        reportPanel.add(excelBtn);
        reportPanel.add(csvBtn);
    }
}