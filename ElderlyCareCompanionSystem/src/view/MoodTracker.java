package view;

import dao.MoodDAO;
import model.Mood;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Mood Tracker JFrame.
 * Allows seniors to log their daily mood and view history.
 */
public class MoodTracker extends JFrame {
    private final User user;
    private JTextField moodField = new JTextField(15);
    private JTextField noteField = new JTextField(20);
    private JTable table = new JTable();

    public MoodTracker(User user) {
        this.user = user;
        setTitle("Mood Tracker");
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.add(new JLabel("Mood (Happy, Sad, Calm, etc.):")); form.add(moodField);
        form.add(new JLabel("Note:")); form.add(noteField);

        JButton addBtn = new JButton("Add");
        JButton refreshBtn = new JButton("Refresh");
        form.add(addBtn); form.add(refreshBtn);

        addBtn.addActionListener(e -> addMood());
        refreshBtn.addActionListener(e -> loadTable());

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadTable();
    }

    private void addMood() {
        String moodLevel = moodField.getText().trim();
        String note = noteField.getText().trim();

        if (moodLevel.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Mood required");
            return;
        }

        Mood m = new Mood();
        m.setUserId(user.getUserId());
        m.setMoodLevel(moodLevel);
        m.setNote(note);

        int res = new MoodDAO().createMood(m);
        JOptionPane.showMessageDialog(this, res>0 ? "Mood saved" : "Save failed");
        loadTable();
    }

    private void loadTable() {
        List<Mood> list = new MoodDAO().getAllMoods(user.getUserId());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Mood","Note","Logged At"}, 0);
        for (Mood m : list) {
            model.addRow(new Object[]{m.getMoodId(), m.getMoodLevel(), m.getNote(), m.getLoggedAt()});
        }
        table.setModel(model);
    }
}
