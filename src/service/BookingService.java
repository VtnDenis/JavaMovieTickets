package service;

import model.Booking;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String BOOKING_FILE_PATH = "bookings.txt";

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides et commentaires
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        Booking booking = new Booking(parts[0], parts[1], Integer.parseInt(parts[2]),
                                                  Integer.parseInt(parts[3]), Double.parseDouble(parts[4]), parts[5]);
                        bookings.add(booking);
                    } else {
                        System.out.println("Invalid format in bookings file: " + line);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error parsing booking line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bookings file: " + e.getMessage());
        }
        return bookings;
    }

    public void saveBookings(List<Booking> bookings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE_PATH))) {
            for (Booking booking : bookings) {
                writer.write(booking.getBookingId() + "," + booking.getUsername() + "," +
                             booking.getMovieId() + "," + booking.getNumTickets() + "," +
                             booking.getTotalPrice() + "," + booking.getBookingDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing bookings file: " + e.getMessage());
        }
    }

    public void addBooking(Booking booking) {
        List<Booking> bookings = getAllBookings();
        bookings.add(booking);
        saveBookings(bookings);
    }

    public String getNextBookingId() {
        List<Booking> bookings = getAllBookings();
        if (bookings.isEmpty()) {
            return "B001";
        }
        try {
            String lastId = bookings.get(bookings.size() - 1).getBookingId();
            // Vérifier que l'ID commence par 'B' suivi de chiffres
            if (lastId != null && lastId.matches("B\\d+")) {
                int num = Integer.parseInt(lastId.substring(1));
                return "B" + String.format("%03d", num + 1);
            } else {
                System.out.println("Invalid booking ID format: " + lastId + ", using default");
                return "B001";
            }
        } catch (Exception e) {
            System.out.println("Error generating booking ID: " + e.getMessage());
            return "B001";
        }
    }
}
