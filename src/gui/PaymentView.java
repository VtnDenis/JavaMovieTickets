package gui;

import model.*;
import service.PaymentService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentView extends JFrame {
    private User user;
    private Movie movie;
    private Booking booking;
    private Discount discount;

    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField cardExpiryField;
    private JTextField cardCVVField;
    private JLabel amountLabel;

    private PaymentService paymentService; // Suppose que tu as un service pour gérer les paiements

    public PaymentView(User user, Movie movie, Booking booking, Discount discount) {
        this.user = user;
        this.movie = movie;
        this.booking = booking;
        this.discount = discount;
        this.paymentService = new PaymentService();

        setTitle("Payment for " + movie.getTitle());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField();
        formPanel.add(cardNumberField);

        formPanel.add(new JLabel("Card Holder Name:"));
        cardHolderField = new JTextField();
        formPanel.add(cardHolderField);

        formPanel.add(new JLabel("Expiry Date (MM/YY):"));
        cardExpiryField = new JTextField();
        formPanel.add(cardExpiryField);

        formPanel.add(new JLabel("CVV:"));
        cardCVVField = new JTextField();
        formPanel.add(cardCVVField);

        formPanel.add(new JLabel("Amount to Pay:"));
        amountLabel = new JLabel(String.format(Locale.US, "$%.2f", booking.getTotalPrice()));
        amountLabel.setFont(amountLabel.getFont().deriveFont(Font.BOLD));
        formPanel.add(amountLabel);

        add(formPanel, BorderLayout.CENTER);

        JButton payButton = new JButton("Confirm Payment");
        payButton.addActionListener(e -> processPayment());
        add(payButton, BorderLayout.SOUTH);
    }

    private void processPayment() {
        String cardNumber = cardNumberField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String cardExpiry = cardExpiryField.getText().trim();
        String cardCVV = cardCVVField.getText().trim();

        if (cardNumber.isEmpty() || cardHolder.isEmpty() || cardExpiry.isEmpty() || cardCVV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all card details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cardNumber.matches("\\d{13,19}")) {
            JOptionPane.showMessageDialog(this, "Invalid card number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cardExpiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Expiry date must be in MM/YY format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cardCVV.matches("\\d{3,4}")) {
            JOptionPane.showMessageDialog(this, "Invalid CVV.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Création d'un objet Payment (paymentId à 0 ou géré par la base)
        String paymentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Payment payment = new Payment(0, booking.getBookingId(), paymentDate,
                booking.getTotalPrice(), "Credit Card",
                cardNumber, cardHolder, cardExpiry, cardCVV);

        // Enregistrer le paiement via un service (à implémenter)
        boolean success = paymentService.addPayment(payment);

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment successful! Thank you.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
