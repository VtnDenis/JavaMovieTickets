package gui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;

    public LoginWindow() {
        userService = new UserService();
        setTitle("Login");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel for username and password
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        // Button panel for login button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(0, 30)); // Set preferred height
        buttonPanel.add(loginButton, BorderLayout.CENTER);

        loginButton.addActionListener(e -> authenticate());

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        User user = userService.authenticateUser(username, password);

        if (user != null) {
            if (user.isEmployee()) {
                EmployeeView employeeView = new EmployeeView();
                employeeView.setVisible(true);
            } else {
                CustomerView customerView = new CustomerView(user);
                customerView.setVisible(true);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        }
    }
}
