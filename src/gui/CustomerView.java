package gui;

import model.Booking;
import model.Customer;
import model.Movie;
import model.User;
import service.BookingService;
import service.CustomerService;
import service.MovieService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerView extends JFrame {
    private User currentUser;
    private MovieService movieService;
    private CustomerService customerService;
    private BookingService bookingService;

    private JComboBox<Movie> movieComboBox;
    private JSpinner ticketsSpinner;
    private JCheckBox studentCheckBox;
    private JLabel priceLabel;
    private JButton buyButton;

    public CustomerView(User user) {
        this.currentUser = user;
        this.movieService = new MovieService();
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();

        setTitle("Customer View - " + currentUser.getUsername());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        selectionPanel.add(new JLabel("Select Movie:"));
        movieComboBox = new JComboBox<>();
        List<Movie> movies = movieService.getAllMovies();
        for (Movie movie : movies) {
            movieComboBox.addItem(movie);
        }
        selectionPanel.add(movieComboBox);

        selectionPanel.add(new JLabel("Number of Tickets:"));
        ticketsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        selectionPanel.add(ticketsSpinner);

        selectionPanel.add(new JLabel("Student Discount:"));
        studentCheckBox = new JCheckBox();
        selectionPanel.add(studentCheckBox);

        selectionPanel.add(new JLabel("Total Price:"));
        priceLabel = new JLabel("0.0");
        selectionPanel.add(priceLabel);

        add(selectionPanel, BorderLayout.CENTER);

        buyButton = new JButton("Buy Tickets");
        buyButton.addActionListener(e -> processBooking());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(buyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        movieComboBox.addActionListener(e -> updatePrice());
        ticketsSpinner.addChangeListener(e -> updatePrice());
        studentCheckBox.addActionListener(e -> updatePrice());

        updatePrice();
    }

    private void updatePrice() {
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        int numTickets = (Integer) ticketsSpinner.getValue();
        boolean isStudent = studentCheckBox.isSelected();

        if (selectedMovie != null) {
            double totalPrice = selectedMovie.getPrice() * numTickets;
            Customer customer = customerService.getCustomerByUsername(currentUser.getUsername());
            if (isStudent && customer != null && customer.isStudent()) {
                totalPrice *= 0.8; // 20% discount
            }
            // Utiliser un format avec point décimal pour la cohérence
            priceLabel.setText(String.format(Locale.US, "%.2f", totalPrice));
        }
    }

    private void processBooking() {
        Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
        int numTickets = (Integer) ticketsSpinner.getValue();
        // Corriger le parsing du prix en remplaçant la virgule par un point
        double totalPrice = Double.parseDouble(priceLabel.getText().replace(',', '.'));
        String bookingId = bookingService.getNextBookingId();
        String bookingDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Booking booking = new Booking(bookingId, currentUser.getUsername(), selectedMovie.getId(), numTickets, totalPrice, bookingDate);
        bookingService.addBooking(booking);

        JOptionPane.showMessageDialog(this, "Payment Successful! Booking ID: " + bookingId, "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }
}
