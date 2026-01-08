package view;

import dao.EmergencyContactDAO;
import model.EmergencyContact;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Emergency Contacts JFrame.
 * Lets seniors add and manage emergency contacts.
 */
public class EmergencyContactsView extends JFrame {
    private final User user;
    private JTextField nameField = new JTextField(15);
    private JTextField relationshipField = new JTextField(10);
    private JTextField phoneField = new JTextField(12);
    private JTextField priorityField = new JTextField(5);
    private JTable table = new JTable();

    public EmergencyContactsView(User user) {
        this.user = user;
        setTitle("Emergency Contacts");
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(5,2,8,8));
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Relationship:")); form.add(relationshipField);
        form.add(new JLabel("Phone Number:")); form.add(phoneField);
        form.add(new JLabel("Priority (1=highest):")); form.add(priorityField);

        JButton addBtn = new JButton("Add");
        JButton refreshBtn = new JButton("Refresh");
        form.add(addBtn); form.add(refreshBtn);

        addBtn.addActionListener(e -> addContact());
        refreshBtn.addActionListener(e -> loadTable());

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
    }

    private void addContact() {
        try {
            String name = nameField.getText().trim();
            String relationship = relationshipField.getText().trim();
            String phone = phoneField.getText().trim();
            int priority = Integer.parseInt(priorityField.getText().trim());

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Name and phone required");
                return;
            }

            EmergencyContact c = new EmergencyContact();
            c.setUserId(user.getUserId());
            c.setName(name);
            c.setRelationship(relationship);
            c.setPhoneNumber(phone);
            c.setPriority(priority);

            int res = new EmergencyContactDAO().createContact(c);
            JOptionPane.showMessageDialog(this, res>0 ? "Contact saved" : "Save failed");
            loadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid priority (must be a number)");
        }
    }

    private void loadTable() {
        List<EmergencyContact> list = new EmergencyContactDAO().getAllContacts(user.getUserId());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Relationship","Phone","Priority"}, 0);
        for (EmergencyContact c : list) {
            model.addRow(new Object[]{c.getContactId(), c.getName(), c.getRelationship(), c.getPhoneNumber(), c.getPriority()});
        }
        table.setModel(model);
    }
}
