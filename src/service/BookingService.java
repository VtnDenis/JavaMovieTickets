package service;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT bookingId, username, movieId, numTickets, totalPrice, bookingDate FROM booking";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("bookingId"),
                        rs.getString("username"),
                        rs.getInt("movieId"),
                        rs.getInt("numTickets"),
                        rs.getDouble("totalPrice"),
                        rs.getDate("bookingDate").toString()
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
        return bookings;
    }

    public void addBooking(Booking booking) {
        String sql = "INSERT INTO booking (bookingId, username, movieId, numTickets, totalPrice, bookingDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getUsername());
            pstmt.setInt(3, booking.getMovieId());
            pstmt.setInt(4, booking.getNumTickets());
            pstmt.setDouble(5, booking.getTotalPrice());

            // utilise Date.valueOf avec format yyyy-MM-dd
            pstmt.setDate(6, Date.valueOf(booking.getBookingDate()));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur addBooking: " + e.getMessage());
        }

    }

    public String getNextBookingId() {
        String sql = "SELECT bookingId FROM booking";
        int maxNum = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("bookingId");
                if (id != null && id.matches("B\\d+")) {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la génération du nouvel ID : " + e.getMessage());
        }

        return "B" + String.format("%03d", maxNum + 1);
    }

    public static void main(String[] args) {
            BookingService service = new BookingService();

            // Avant ajout
            System.out.println("Réservations existantes :");
            List<Booking> bookingsAvant = service.getAllBookings();
            for (Booking b : bookingsAvant) {
                System.out.printf("ID: %s, User: %s, Film ID: %d, Tickets: %d, Prix total: %.2f, Date: %s%n",
                        b.getBookingId(), b.getUsername(), b.getMovieId(), b.getNumTickets(), b.getTotalPrice(), b.getBookingDate());
            }

            // Ajout d'une nouvelle réservation
            String nextId = service.getNextBookingId();
            Booking newBooking = new Booking(nextId, "louis.dupont", 4, 3, 30.0, "2025-05-31");
            service.addBooking(newBooking);
            System.out.println("\n✅ Nouvelle réservation ajoutée avec ID : " + nextId);

            // Après ajout
            System.out.println("\nRéservations après ajout :");
            List<Booking> bookingsApres = service.getAllBookings();
            for (Booking b : bookingsApres) {
                System.out.printf("ID: %s, User: %s, Film ID: %d, Tickets: %d, Prix total: %.2f, Date: %s%n",
                        b.getBookingId(), b.getUsername(), b.getMovieId(), b.getNumTickets(), b.getTotalPrice(), b.getBookingDate());
            }
        }
}
