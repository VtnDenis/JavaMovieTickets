package service;

import model.Payment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PaymentService {

    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Ajout d'un paiement en base de données
    public boolean addPayment(Payment payment) {
        String insert = "INSERT INTO payment (paymentId, bookingId, paymentDate, amount, paymentMethod, cardNumber, cardHolderName, cardExpiry, cardCvv) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setInt(1, payment.getPaymentId());
            stmt.setString(2, payment.getBookingId());
            stmt.setString(3, payment.getPaymentDate());
            stmt.setDouble(4, payment.getAmount());
            stmt.setString(5, payment.getPaymentMethod());
            stmt.setString(6, payment.getCardNumber());
            stmt.setString(7, payment.getCardHolderName());
            stmt.setString(8, payment.getCardExpiry());
            stmt.setString(9, payment.getCardCVV());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement du paiement : " + e.getMessage());
            return false;
        }
    }

    // Création d’un objet Payment à partir d’une saisie console sécurisée
    public Payment createPaymentFromInput(int paymentId, String bookingId, double amount, String paymentDate) {
        Scanner scanner = new Scanner(System.in);
        String paymentMethod = "CB";

        String cardNumber;
        do {
            System.out.print("Numéro de carte (16 chiffres) : ");
            cardNumber = scanner.nextLine().trim();
        } while (!cardNumber.matches("\\d{16}"));

        String cardHolderName;
        do {
            System.out.print("Nom du titulaire (lettres et espaces uniquement) : ");
            cardHolderName = scanner.nextLine().trim();
        } while (!cardHolderName.matches("[a-zA-Z ]{2,}"));

        String cardExpiry;
        do {
            System.out.print("Date d'expiration (MM/YY) : ");
            cardExpiry = scanner.nextLine().trim();
        } while (!cardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}"));

        String cardCVV;
        do {
            System.out.print("CVV (3 chiffres) : ");
            cardCVV = scanner.nextLine().trim();
        } while (!cardCVV.matches("\\d{3}"));

        return new Payment(paymentId, bookingId, paymentDate, amount, paymentMethod,
                cardNumber, cardHolderName, cardExpiry, cardCVV);
    }
}
