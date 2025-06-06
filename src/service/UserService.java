package service;

import model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USER_FILE_PATH = "users.txt";

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    User user = new User(parts[0], parts[1], parts[2]);
                    users.add(user);
                } else {
                    System.out.println("Invalid format in line: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
        return users;
    }

    public User authenticateUser(String username, String password) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
