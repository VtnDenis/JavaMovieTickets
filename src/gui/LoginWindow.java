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
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        loginButton = new JButton("Login");
        panel.add(loginButton);
        panel.add(new JLabel());

        loginButton.addActionListener(e -> authenticate());

        add(panel);
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
