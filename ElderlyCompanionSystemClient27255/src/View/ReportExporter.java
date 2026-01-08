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


import java.awt.HeadlessException;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import java.io.IOException;

public class ReportExporter {
    public static void exportCsv(JTable table) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("elderly_report.csv"));
            if (chooser.showSaveDialog(table) == JFileChooser.APPROVE_OPTION) {
                TableModel tm = table.getModel();
                try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
                    for (int col = 0; col < tm.getColumnCount(); col++) {
                        fw.write(tm.getColumnName(col));
                        if (col < tm.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                    for (int row = 0; row < tm.getRowCount(); row++) {
                        for (int col = 0; col < tm.getColumnCount(); col++) {
                            fw.write(String.valueOf(tm.getValueAt(row, col)));
                            if (col < tm.getColumnCount() - 1) fw.write(",");
                        }
                        fw.write("\n");
                    }
                }
                JOptionPane.showMessageDialog(table, "CSV exported successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(table, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
