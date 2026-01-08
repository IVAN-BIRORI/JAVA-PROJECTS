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
import Service.AuthService;          
import Service.ElderlyService;
import model.ElderlyPerson;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginForm extends JFrame {
    private final JTextField txtUsername = new JTextField(20);
    private final JTextField txtDestination = new JTextField(20); // email or phone
    private final JTextField txtOtp = new JTextField(6);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnSendOtp = new JButton("Send OTP");
    private final JButton btnLoginOtp = new JButton("Login with OTP");
    private final JButton btnLoginPassword = new JButton("Login with Password");
    public static String SESSION_TOKEN = null;

    public LoginForm() {
        setTitle("Elderly Companion System - Login");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        p.setBackground(new Color(240, 248, 255));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(labelFont);
        p.add(lblUsername); p.add(txtUsername);
        txtUsername.setFont(fieldFont);

        JLabel lblDestination = new JLabel("Destination (email/phone):");
        lblDestination.setFont(labelFont);
        p.add(lblDestination); p.add(txtDestination);
        txtDestination.setFont(fieldFont);

        JLabel lblOtp = new JLabel("OTP:");
        lblOtp.setFont(labelFont);
        p.add(lblOtp); p.add(txtOtp);
        txtOtp.setFont(fieldFont);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(labelFont);
        p.add(lblPassword); p.add(txtPassword);
        txtPassword.setFont(fieldFont);

        btnSendOtp.setBackground(new Color(70, 130, 180));
        btnSendOtp.setForeground(Color.WHITE);
        btnSendOtp.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(btnSendOtp);

        btnLoginOtp.setBackground(new Color(34, 139, 34));
        btnLoginOtp.setForeground(Color.WHITE);
        btnLoginOtp.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(btnLoginOtp);

        p.add(new JLabel("")); // Empty cell

        btnLoginPassword.setBackground(new Color(255, 140, 0));
        btnLoginPassword.setForeground(Color.WHITE);
        btnLoginPassword.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(btnLoginPassword);

        add(p);

        btnSendOtp.addActionListener(e -> sendOtp());
        btnLoginOtp.addActionListener(e -> loginWithOtp());
        btnLoginPassword.addActionListener(e -> loginWithPassword());
    }

    private void sendOtp() {
        try {
            String username = txtUsername.getText().trim();
            String dest = txtDestination.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dest.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Destination (email or phone) is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Sending OTP for username: " + username);
            AuthService auth = ClientConnector.authService();
            String otp = auth.sendOtp(username, dest);
            JOptionPane.showMessageDialog(this,
                    "An OTP has been sent. For testing it is: " + otp,
                    "OTP Sent",
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("OTP sent successfully: " + otp);
        } catch (Exception ex) {
            System.out.println("Error sending OTP: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginWithOtp() {
        try {
            String username = txtUsername.getText().trim();
            String otp = txtOtp.getText().trim();
            if (username.isEmpty() || otp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and OTP are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Logging in with OTP for username: " + username);
            AuthService auth = ClientConnector.authService();
            String token = auth.loginWithOtp(username, otp);
            SESSION_TOKEN = token;
            System.out.println("OTP login successful, token: " + token);
            JOptionPane.showMessageDialog(this, "Login successful.", "Info", JOptionPane.INFORMATION_MESSAGE);
            openDashboard();
        } catch (Exception ex) {
            System.out.println("OTP login failed: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginWithPassword() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            System.out.println("Logging in with password for username: " + username);
            AuthService auth = ClientConnector.authService();
            boolean ok = auth.login(username, password);
            if (ok) {
                SESSION_TOKEN = "PASSWORD-LOGIN-" + System.currentTimeMillis();
                System.out.println("Password login successful");
                JOptionPane.showMessageDialog(this,
                        "Login successful. (Use password 1234 for demo)",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                openDashboard();
            } else {
                System.out.println("Password login failed - invalid credentials");
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials. For demo, the password is 1234.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.out.println("Password login error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard() {
        try {
            // Always make sure there is at least one elderly person in the system
            ElderlyService elderlyService = ClientConnector.elderlyService();
            List<ElderlyPerson> all;
            try {
                all = elderlyService.getAllElderly();
            } catch (Exception ex) {
                System.out.println("getAllElderly failed: " + ex.getMessage());
                all = new ArrayList<>();
            }

            if (all == null || all.isEmpty()) {
                // Try to create a default elderly profile so that the dashboard has data to work with,
                // but do NOT fail the login flow if this creation fails.
                try {
                    ElderlyPerson p = new ElderlyPerson();
                    p.setName("Default Elderly");
                    p.setNationalId("DEFAULT123456");
                    String contact = txtDestination.getText().trim();
                    if (contact.isEmpty()) {
                        contact = "+250000000000"; // generic contact if none entered
                    }
                    p.setContact(contact);

                    elderlyService.addElderly(p); // ignore boolean result; server will use DB or in-memory
                } catch (Exception createEx) {
                    System.out.println("Default elderly creation failed: " + createEx.getMessage());
                }

                // Try to reload after attempting to create default
                try {
                    all = elderlyService.getAllElderly();
                } catch (Exception reloadEx) {
                    System.out.println("Reload getAllElderly failed: " + reloadEx.getMessage());
                    all = new ArrayList<>();
                }
            }

            if (all == null || all.isEmpty() || all.get(0).getId() == null) {
                // No elderly profile available from server. Redirect user to registration form
                // instead of showing an error.
                JOptionPane.showMessageDialog(this,
                        "Login succeeded. Please register an elderly profile first.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                new ElderlyRegistrationForm().setVisible(true);
                dispose();
                return;
            }

            int elderlyId = all.get(0).getId();
            new MainDashboard(elderlyId).setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error opening dashboard: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
