package service;

import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        String query = "SELECT * FROM customer";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String fullName = rs.getString("fullName");
                boolean isStudent = rs.getBoolean("student");

                customers.add(new Customer(username, fullName, isStudent));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }

        return customers;
    }

    public Customer getCustomerByUsername(String username) {
        String query = "SELECT * FROM customer WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("fullName");
                boolean isStudent = rs.getBoolean("student");
                return new Customer(username, fullName, isStudent);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du client : " + e.getMessage());
        }

        return null;
    }

    public void addCustomer(Customer customer) {
        String query = "INSERT INTO customer (username, fullName, student) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customer.getUsername());
            pstmt.setString(2, customer.getFullName());
            pstmt.setBoolean(3, customer.isStudent());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    public void updateCustomer(Customer customer) {
        String query = "UPDATE customer SET fullName = ?, student = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setBoolean(2, customer.isStudent());
            pstmt.setString(3, customer.getUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du client : " + e.getMessage());
        }
    }

    public void deleteCustomer(String username) {
        String query = "DELETE FROM customer WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();

        System.out.println("Liste des clients :");
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("Aucun client trouvé.");
        } else {
            for (Customer c : customers) {
                System.out.println("Username : " + c.getUsername());
                System.out.println("Nom complet : " + c.getFullName());
                System.out.println("Étudiant : " + (c.isStudent() ? "Oui" : "Non"));
                System.out.println("---------------------------");
            }
        }
    }
}
