package view;

import dao.MedicationDAO;
import model.Medication;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

/**
 * Medication Manager JFrame.
 * Lets seniors add, view, and manage medication reminders.
 */
public class MedicationManager extends JFrame {
    private final User user;
    private JTextField nameField = new JTextField(15);
    private JTextField dosageField = new JTextField(10);
    private JTextField timeField = new JTextField(8); // HH:mm
    private JTextField notesField = new JTextField(15);
    private JTable table = new JTable();

    public MedicationManager(User user) {
        this.user = user;
        setTitle("Medications");
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(5,2,8,8));
        form.add(new JLabel("Medication Name:")); form.add(nameField);
        form.add(new JLabel("Dosage:")); form.add(dosageField);
        form.add(new JLabel("Reminder Time:")); form.add(timeField);
        form.add(new JLabel("Notes:")); form.add(notesField);

        JButton addBtn = new JButton("Add");
        JButton refreshBtn = new JButton("Refresh");
        form.add(addBtn); form.add(refreshBtn);

        addBtn.addActionListener(e -> addMedication());
        refreshBtn.addActionListener(e -> loadTable());

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
    }

    private void addMedication() {
        try {
            String name = nameField.getText().trim();
            String dosage = dosageField.getText().trim();
            LocalTime time = LocalTime.parse(timeField.getText().trim());
            String notes = notesField.getText().trim();

            if (name.isEmpty() || dosage.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Name and dosage required");
                return;
            }

            Medication m = new Medication();
            m.setUserId(user.getUserId());
            m.setMedName(name);
            m.setDosage(dosage);
            m.setReminderTime(time);
            m.setNotes(notes);

            int res = new MedicationDAO().createMedication(m);
            JOptionPane.showMessageDialog(this, res>0 ? "Medication saved" : "Save failed");
            loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid time format (HH:mm)");
        }
    }

    private void loadTable() {
        List<Medication> list = new MedicationDAO().getAllMedications(user.getUserId());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Dosage","Time","Notes"}, 0);
        for (Medication m : list) {
            model.addRow(new Object[]{m.getMedId(), m.getMedName(), m.getDosage(), m.getReminderTime(), m.getNotes()});
        }
        table.setModel(model);
    }
}
