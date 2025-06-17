package gui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox createAccountCheckBox;
    private JRadioButton employeeRadio;
    private JRadioButton customerRadio;
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
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("New Account ?"));
        createAccountCheckBox = new JCheckBox();
        formPanel.add(createAccountCheckBox);

        formPanel.add(new JLabel("You are an:"));
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        employeeRadio = new JRadioButton("Employee");
        customerRadio = new JRadioButton("Customer");
        customerRadio.setSelected(true);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(employeeRadio);
        roleGroup.add(customerRadio);
        rolePanel.add(employeeRadio);
        rolePanel.add(customerRadio);
        formPanel.add(rolePanel);

        // Bouton
        JPanel buttonPanel = new JPanel(new BorderLayout());
        loginButton = new JButton("OK");
        loginButton.setPreferredSize(new Dimension(0, 30));
        buttonPanel.add(loginButton, BorderLayout.CENTER);

        loginButton.addActionListener(e -> process());

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void process() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (createAccountCheckBox.isSelected()) {
            boolean isEmployee = employeeRadio.isSelected();
            boolean created = userService.createUser(username, password, isEmployee);
            if (created) {
                JOptionPane.showMessageDialog(this, "Succes!");
            } else {
                JOptionPane.showMessageDialog(this, "Error");
                return;
            }
        }

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
}
