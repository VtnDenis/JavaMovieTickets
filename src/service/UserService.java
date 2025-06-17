package service;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                users.add(new User(username, password, role));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    public boolean createUser(String username, String password, boolean isEmployee) {
        if (username == null || username.length() < 3 || username.contains(" ")) {
            System.out.println("Nom d'utilisateur invalide. Il doit contenir au moins 3 caractères et ne pas contenir d'espaces.");
            afficherReglesCreation();
            return false;
        }

        if (!isPasswordValid(password)) {
            afficherReglesCreation();
            return false;
        }

        String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            try (PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameQuery)) {
                checkUsernameStmt.setString(1, username);
                try (ResultSet rs = checkUsernameStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Nom d'utilisateur déjà utilisé.");
                        return false;
                    }
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                String role = isEmployee ? "employee" : "customer";
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, role);

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    private void afficherReglesCreation() {
        System.out.println("Règles de création de compte :");
        System.out.println("- Nom d'utilisateur : au moins 3 caractères, sans espaces");
        System.out.println("- Mot de passe : au moins 8 caractères, avec au moins une lettre et un chiffre");
    }

    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }

        return false;
    }


    public User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    return new User(username, password, role);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
        }

        return null;
    }
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);

        // Affichage des utilisateurs existants
        System.out.println("Liste des utilisateurs :");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            for (User u : users) {
                System.out.println("Nom d'utilisateur : " + u.getUsername());
                System.out.println("Mot de passe : " + u.getPassword());
                System.out.println("Rôle : " + u.getRole());
                System.out.println("-------------------------");
            }
        }

        System.out.print("Entrez le nom d'utilisateur (qui sera aussi l'ID) : ");
        String username = scanner.nextLine();

        System.out.print("Entrez le mot de passe : ");
        String password = scanner.nextLine();

        System.out.print("Est-ce un employé ? (true/false) : ");
        boolean isEmployee = scanner.nextBoolean();

        boolean created = userService.createUser(username, password, isEmployee);
    }


    }
