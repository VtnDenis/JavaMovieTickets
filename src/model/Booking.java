package model;

public class Booking {
    private String bookingId;
    private String username;
    private int movieId;
    private int numTickets;
    private double totalPrice;
    private String bookingDate;
    private String sessionDate;
    private String sessionTime;

    public Booking(String bookingId, String username, int movieId, int numTickets, double totalPrice, String bookingDate, String sessionDate, String sessionTime) {
        this.bookingId = bookingId;
        this.username = username;
        this.movieId = movieId;
        this.numTickets = numTickets;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
    }

    // Constructor without sessionTime for backward compatibility
    public Booking(String bookingId, String username, int movieId, int numTickets, double totalPrice, String bookingDate, String sessionDate) {
        this(bookingId, username, movieId, numTickets, totalPrice, bookingDate, sessionDate, "");
    }

    // Constructor without sessionDate and sessionTime for backward compatibility
    public Booking(String bookingId, String username, int movieId, int numTickets, double totalPrice, String bookingDate) {
        this(bookingId, username, movieId, numTickets, totalPrice, bookingDate, bookingDate, "");
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUsername() {
        return username;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }
}
