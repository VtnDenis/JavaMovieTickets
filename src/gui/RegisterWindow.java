package gui;

import service.UserService;

import javax.swing.*;
import java.awt.*;
import javax.swing.SwingUtilities;

public class RegisterWindow extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton customerRadio; // Kept for backward compatibility
    private JButton registerButton;
    private JButton cancelButton;
    private UserService userService;

    public RegisterWindow(LoginWindow parent) {
        super(parent, "Register New Account", true); // true makes it modal
        userService = new UserService();
        setSize(350, 250);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        // No longer asking for role - all registrations are for customers
        customerRadio = new JRadioButton("Customer");
        customerRadio.setSelected(true);
        customerRadio.setVisible(false); // Hidden as it's no longer a choice

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        registerButton.addActionListener(e -> register());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        // All registrations are for customers
        boolean isEmployee = false;

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean created = userService.createUser(username, password, isEmployee);
        if (created) {
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the registration window

            // Reopen the login window
            SwingUtilities.invokeLater(() -> {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registration failed. Please ensure:\n" +
                "- Username is at least 3 characters without spaces\n" +
                "- Password is at least 8 characters with at least one letter and one digit\n" +
                "- Username is not already taken", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // No need for this method anymore as JDialog has built-in modality support
}
