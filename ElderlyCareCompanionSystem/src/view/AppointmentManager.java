package view;

import dao.AppointmentDAO;
import model.Appointment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Appointment Manager JFrame.
 * Demonstrates full CRUD: Add, Refresh(View All), Update, Delete.
 */
public class AppointmentManager extends JFrame {
    private AppointmentDAO dao;

    private final User user;
    private JTextField idField = new JTextField(5);       // Appointment ID
    private JTextField titleField = new JTextField(15);   // Title
    private JTextField dateField = new JTextField(10);    // yyyy-mm-dd
    private JTextField timeField = new JTextField(8);     // HH:mm
    private JTextField locationField = new JTextField(12);// Location
    private JTable table = new JTable();

    public AppointmentManager(User user) {
        this.user = user;
        setTitle("Appointments");
        setSize(700, 400);
        setLocationRelativeTo(null);

        // Form panel
        JPanel form = new JPanel(new GridLayout(6,2,8,8));
        form.add(new JLabel("ID:")); form.add(idField);
        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Date:")); form.add(dateField);
        form.add(new JLabel("Time:")); form.add(timeField);
        form.add(new JLabel("Location:")); form.add(locationField);

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton refreshBtn = new JButton("Refresh");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        form.add(addBtn); 
        form.add(refreshBtn);
        form.add(updateBtn);
        form.add(deleteBtn);

        // Button actions
        addBtn.addActionListener(e -> addAppointment());
        refreshBtn.addActionListener(e -> loadTable());

        updateBtn.addActionListener(e -> updateAppointment());
        deleteBtn.addActionListener(e -> deleteAppointment());

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Auto-fill fields when row clicked
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(table.getValueAt(row, 0).toString());
                    titleField.setText(table.getValueAt(row, 1).toString());
                    dateField.setText(table.getValueAt(row, 2).toString());
                    timeField.setText(table.getValueAt(row, 3).toString());
                    locationField.setText(table.getValueAt(row, 4).toString());
                }
            }
        });

        loadTable();
    }

    // CREATE
    private void addAppointment() {
        try {
            String title = titleField.getText().trim();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            String location = locationField.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Title required");
                return;
            }
            if (date.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,"Date must be today or future");
                return;
            }

            Appointment a = new Appointment();
            a.setUserId(user.getUserId());
            a.setTitle(title);
            a.setAppointmentDate(date);
            a.setAppointmentTime(time);
            a.setLocation(location);

            int res = new AppointmentDAO().createAppointment(a);
            JOptionPane.showMessageDialog(this, res>0 ? "Appointment saved" : "Save failed");
            loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format");
        }
    }

    // READ
    private void loadTable() {
        List<Appointment> list = new AppointmentDAO().getAllAppointments(user.getUserId());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Title","Date","Time","Location"}, 0);
        for (Appointment a : list) {
            model.addRow(new Object[]{
                a.getAppointmentId(), a.getTitle(), a.getAppointmentDate(), a.getAppointmentTime(), a.getLocation()
            });
        }
        table.setModel(model);
    }

    // UPDATE
    private void updateAppointment() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String title = titleField.getText().trim();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            String location = locationField.getText().trim();

            Appointment a = new Appointment();
            a.setAppointmentId(id);
            a.setUserId(user.getUserId());
            a.setTitle(title);
            a.setAppointmentDate(date);
            a.setAppointmentTime(time);
            a.setLocation(location);

            int res = new AppointmentDAO().updateAppointment(a);
            JOptionPane.showMessageDialog(this, res > 0 ? "Appointment updated" : "Update failed");
            loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating appointment: " + ex.getMessage());
        }
    }

    // DELETE
    private void deleteAppointment() {
        try {
            int id = Integer.parseInt(idField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this appointment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int res = new AppointmentDAO().deleteAppointment(id);
                JOptionPane.showMessageDialog(this, res > 0 ? "Appointment deleted" : "Delete failed");
                loadTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting appointment: " + ex.getMessage());
        }
    }
}
