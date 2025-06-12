package service;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        // Test d'authentification
        String testUsername = "marc.lemans";
        String testPassword = "gestion2024";
        User authenticated = userService.authenticateUser(testUsername, testPassword);
        if (authenticated != null) {
            System.out.println("Connexion réussie pour : " + authenticated.getUsername() +
                    " (rôle : " + authenticated.getRole() + ")");
        } else {
            System.out.println("Échec de l'authentification pour l'utilisateur : " + testUsername);
        }
    }
}
