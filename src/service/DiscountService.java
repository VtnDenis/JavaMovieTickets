package service;

import model.Discount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountService {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        String query = "SELECT * FROM discount";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String description = rs.getString("description");
                double percentage = rs.getDouble("percentage");
                boolean active = rs.getBoolean("active");

                discounts.add(new Discount(code, description, percentage, active));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réductions : " + e.getMessage());
        }

        return discounts;
    }

    public void addDiscount(Discount discount) {
        String query = "INSERT INTO discount (code, description, percentage, active) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, discount.getCode());
            stmt.setString(2, discount.getDescription());
            stmt.setDouble(3, discount.getPercentage());
            stmt.setBoolean(4, discount.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réduction : " + e.getMessage());
        }
    }

    public void updateDiscount(Discount discount) {
        String query = "UPDATE discount SET description = ?, percentage = ?, active = ? WHERE code = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, discount.getDescription());
            stmt.setDouble(2, discount.getPercentage());
            stmt.setBoolean(3, discount.isActive());
            stmt.setString(4, discount.getCode());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la réduction : " + e.getMessage());
        }
    }

    public void deleteDiscount(String code) {
        String query = "DELETE FROM discount WHERE code = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réduction : " + e.getMessage());
        }
    }

    public Discount getDiscountByCode(String code) {
        String query = "SELECT * FROM discount WHERE code = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("description");
                    double percentage = rs.getDouble("percentage");
                    boolean active = rs.getBoolean("active");

                    return new Discount(code, description, percentage, active);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la réduction : " + e.getMessage());
        }

        return null;
    }
    public static void main(String[] args) {
        DiscountService discountService = new DiscountService();

        System.out.println("Liste des réductions :");
        List<Discount> discounts = discountService.getAllDiscounts();

        if (discounts.isEmpty()) {
            System.out.println("Aucune réduction trouvée.");
        } else {
            for (Discount d : discounts) {
                System.out.println("Code : " + d.getCode());
                System.out.println("Description : " + d.getDescription());
                System.out.println("Pourcentage : " + d.getPercentage() + "%");
                System.out.println("Active : " + (d.isActive() ? "Oui" : "Non"));
                System.out.println("-----------------------------");
            }
        }
    }
}
