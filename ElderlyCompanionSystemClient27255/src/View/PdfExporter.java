/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

/**
 * Simple PDF exporter wrapper.
 * For this exam project, Excel and CSV exports are fully implemented.
 * PDF export requires the external iText library; if it is not present,
 * we show a clear message instead of failing compilation.
 */

import javax.swing.*;

public class PdfExporter {
    public static void exportPdf(JTable table) {
        if (table == null) {
            JOptionPane.showMessageDialog(null, "Table is null", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Try to detect iText at runtime. If present, the student can
            // implement a full export using it; for now we just acknowledge it.
            Class.forName("com.itextpdf.text.Document");
            JOptionPane.showMessageDialog(table,
                    "iText library detected. You can implement full PDF export using iText.",
                    "PDF Export Stub",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException ex) {
            // Library not on classpath – inform the user but keep the application working.
            JOptionPane.showMessageDialog(table,
                    "PDF export requires the iText library. Add iText JAR to the project lib/ folder " +
                    "and classpath if you want real PDF generation.\n" +
                    "Excel and CSV exports are already fully functional.",
                    "PDF Dependency Missing",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
