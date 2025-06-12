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
            pstmt.setDate(6, Date.valueOf(booking.getBookingDate()));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }

    public String getNextBookingId() {
        String sql = "SELECT bookingId FROM booking ORDER BY bookingId DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("bookingId");
                if (lastId != null && lastId.matches("B\\d+")) {
                    int num = Integer.parseInt(lastId.substring(1));
                    return "B" + String.format("%03d", num + 1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la génération du nouvel ID : " + e.getMessage());
        }
        return "B001";
    }
    public static void main(String[] args) {
        BookingService service = new BookingService();

        List<Booking> bookings = service.getAllBookings();
        System.out.println("Réservations en base :");
        for (Booking b : bookings) {
            System.out.printf("ID: %s, User: %s, Film ID: %d, Tickets: %d, Prix total: %.2f, Date: %s%n",
                    b.getBookingId(), b.getUsername(), b.getMovieId(), b.getNumTickets(), b.getTotalPrice(), b.getBookingDate());
        }

        // Exemple d'ajout d'une nouvelle réservation
        String nextId = service.getNextBookingId();
        Booking newBooking = new Booking(nextId, "emma.lune", 2, 3, 30.0, "2025-06-12");
        service.addBooking(newBooking);
        System.out.println("Nouvelle réservation ajoutée avec ID : " + nextId);
    }
}
