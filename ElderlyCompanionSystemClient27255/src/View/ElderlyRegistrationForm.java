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
import model.ElderlyPerson;
import Service.ElderlyService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ElderlyRegistrationForm extends JFrame {
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtNationalId = new JTextField(20);
    private final JTextField txtContact = new JTextField(20);
    private final JButton btnSave = new JButton("Save Elderly");
    private final JButton btnLoad = new JButton("Load All");
    private final JTable tbl = new JTable(new DefaultTableModel(new Object[]{"ID", "Name", "National ID", "Contact"}, 0));
    private final JLabel statusLabel = new JLabel("Ready");

    public ElderlyRegistrationForm() {
        setTitle("Elderly Companion System - Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background
        getContentPane().setBackground(new Color(245, 245, 245));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Register New Elderly Person"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(labelFont);
        formPanel.add(lblName, gbc);
        gbc.gridx = 1;
        txtName.setFont(fieldFont);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNid = new JLabel("National ID:");
        lblNid.setFont(labelFont);
        formPanel.add(lblNid, gbc);
        gbc.gridx = 1;
        txtNationalId.setFont(fieldFont);
        formPanel.add(txtNationalId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblContact = new JLabel("Contact:");
        lblContact.setFont(labelFont);
        formPanel.add(lblContact, gbc);
        gbc.gridx = 1;
        txtContact.setFont(fieldFont);
        formPanel.add(txtContact, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        btnSave.setBackground(new Color(34, 139, 34));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(btnSave, gbc);
        gbc.gridx = 1;
        btnLoad.setBackground(new Color(70, 130, 180));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(btnLoad, gbc);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Registered Elderly Persons"));
        tablePanel.add(new JScrollPane(tbl), BorderLayout.CENTER);
        tbl.setFont(new Font("Arial", Font.PLAIN, 12));
        tbl.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(245, 245, 245));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(statusLabel);

        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveElderly());
        btnLoad.addActionListener(e -> loadElderly());
    }

    private void saveElderly() {
        try {
            System.out.println("Starting saveElderly...");
            // Validation BUZINESS
            String name = txtName.getText().trim();
            String nid = txtNationalId.getText().trim();
            String contact = txtContact.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nid.length() < 8) {
                JOptionPane.showMessageDialog(this, "National ID must be at least 8 characters.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!contact.matches("^\\+?\\d{10,13}$")) {
                JOptionPane.showMessageDialog(this, "Contact must be a valid phone number (10–13 digits).", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Validation passed, connecting to service...");
            ElderlyService svc = ClientConnector.elderlyService();
            ElderlyPerson person = new ElderlyPerson();
            person.setName(name);
            person.setNationalId(nid);
            person.setContact(contact);

            try {
                // Match the shared RMI interface
                boolean ok = svc.addElderly(person);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Elderly registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    statusLabel.setText("Elderly saved successfully!");
                    System.out.println("Elderly saved successfully!");
                } else {
                    statusLabel.setText("Save failed on server.");
                    System.out.println("Save failed on server.");
                }
            } catch (Exception callEx) {
                // Offline fallback if server unreachable
                String msg = (callEx.getMessage() != null ? callEx.getMessage().toLowerCase() : "");
                boolean connRefused = msg.contains("connection refused")
                        || callEx.getCause() instanceof java.net.ConnectException;
                if (connRefused) {
                    System.out.println("Server unreachable, saving offline...");
                    saveOffline(person);
                    statusLabel.setText("Server unreachable — saved locally and will sync later.");
                } else {
                    statusLabel.setText("Error saving elderly: " + callEx.getMessage());
                    System.out.println("Error saving elderly: " + callEx.getMessage());
                }
            }

            clearForm();
            loadElderly();
        } catch (Exception ex) {
            statusLabel.setText("Error saving elderly: " + ex.getMessage());
            System.out.println("Exception in saveElderly: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadElderly() {
        System.out.println("Starting loadElderly...");
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);
        try {
            ElderlyService svc = ClientConnector.elderlyService();
            List<ElderlyPerson> list = svc.getAllElderly(); // Match interface
            System.out.println("Retrieved " + list.size() + " elderly persons from server.");
            for (ElderlyPerson e : list) {
                model.addRow(new Object[]{e.getId(), e.getName(), e.getNationalId(), e.getContact()});
            }
            model.fireTableDataChanged(); // Ensure table updates
            statusLabel.setText("Loaded " + list.size() + " records from server.");
        } catch (Exception ex) {
            System.out.println("Error loading from server: " + ex.getMessage());
            // Load offline if server unreachable
            try {
                List<ElderlyPerson> local = loadOffline();
                System.out.println("Loaded " + local.size() + " records from offline storage.");
                for (ElderlyPerson e : local) {
                    model.addRow(new Object[]{"—", e.getName(), e.getNationalId(), e.getContact()});
                }
                model.fireTableDataChanged();
                statusLabel.setText("Loaded " + local.size() + " records from offline storage.");
            } catch (Exception readEx) {
                statusLabel.setText("Error loading data: " + ex.getMessage());
                System.out.println("Error loading offline: " + readEx.getMessage());
            }
        }
    }

    private void saveOffline(ElderlyPerson e) throws IOException {
        Path p = Paths.get(System.getProperty("user.home"), "elderly_offline.csv");
        boolean exists = Files.exists(p);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(p.toFile(), true))) {
            if (!exists) {
                w.write("name,nationalId,contact");
                w.newLine();
            }
            w.write(String.format("%s,%s,%s", escapeCsv(e.getName()), escapeCsv(e.getNationalId()), escapeCsv(e.getContact())));
            w.newLine();
        }
    }

    private List<ElderlyPerson> loadOffline() throws IOException {
        Path p = Paths.get(System.getProperty("user.home"), "elderly_offline.csv");
        List<ElderlyPerson> out = new ArrayList<>();
        if (!Files.exists(p)) return out;
        List<String> lines = Files.readAllLines(p);
        for (int i = 1; i < lines.size(); i++) {
            String ln = lines.get(i);
            if (ln == null || ln.trim().isEmpty()) continue;
            String[] parts = ln.split(",", -1);
            ElderlyPerson e = new ElderlyPerson();
            e.setName(parts.length > 0 ? parts[0].trim() : "");
            e.setNationalId(parts.length > 1 ? parts[1].trim() : "");
            e.setContact(parts.length > 2 ? parts[2].trim() : "");
            out.add(e);
        }
        return out;
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"").replace(',', ' ');
    }

    private void clearForm() {
        txtName.setText("");
        txtNationalId.setText("");
        txtContact.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElderlyRegistrationForm().setVisible(true));
    }
}
