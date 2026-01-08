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


import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelExporter {
    public static void exportExcel(JTable table) {
        if (table == null) {
            JOptionPane.showMessageDialog(null, "Table is null", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("elderly_report.xlsx"));
        chooser.setDialogTitle("Save Excel File");
        
        int userSelection = chooser.showSaveDialog(table);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return; // User cancelled the operation
        }

        File fileToSave = chooser.getSelectedFile();
        // Ensure the file has .xlsx extension
        if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
        }

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            TableModel tm = table.getModel();
            XSSFSheet sheet = wb.createSheet("Report");
            
            // Create header row
            XSSFRow header = sheet.createRow(0);
            for (int c = 0; c < tm.getColumnCount(); c++) {
                XSSFCell cell = header.createCell(c);
                cell.setCellValue(tm.getColumnName(c));
            }
            
            // Create data rows
            for (int r = 0; r < tm.getRowCount(); r++) {
                XSSFRow row = sheet.createRow(r + 1);
                for (int c = 0; c < tm.getColumnCount(); c++) {
                    Object value = tm.getValueAt(r, c);
                    XSSFCell cell = row.createCell(c);
                    if (value != null) {
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < tm.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write the workbook to file
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                wb.write(fos);
                JOptionPane.showMessageDialog(table, 
                    "Excel file has been exported successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(table, 
                "Error exporting to Excel: " + ex.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(table, 
                "An unexpected error occurred: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
