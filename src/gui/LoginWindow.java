package gui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserService userService;

    public LoginWindow() {
        userService = new UserService();
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulaire
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> openRegisterWindow());

        loginButton = new JButton("OK");
        loginButton.addActionListener(e -> process());

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void process() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = userService.authenticateUser(username, password);
        if (user != null) {
            if (user.isEmployee()) {
                new EmployeeView().setVisible(true);
            } else {
                new CustomerView(user).setVisible(true);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error Username");
        }
    }

    private void openRegisterWindow() {
        setVisible(false); // Hide the login window
        RegisterWindow registerWindow = new RegisterWindow(this);
        registerWindow.setVisible(true);
    }
}
