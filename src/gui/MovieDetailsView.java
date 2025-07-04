package gui;

import model.Booking;
import model.Customer;
import model.Discount;
import model.Movie;
import model.User;
import service.BookingService;
import service.CustomerService;
import service.DiscountService;
import service.MovieService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDetailsView extends JFrame {
    private User currentUser;
    private Movie movie;
    private CustomerService customerService;
    private BookingService bookingService;
    private DiscountService discountService;

    private JTextArea synopsisArea;
    private JSpinner ticketsSpinner;
    private JComboBox<Discount> discountComboBox;
    private JComboBox<String> sessionDateComboBox;
    private JComboBox<String> sessionTimeComboBox;
    private JLabel priceLabel;
    private JButton buyButton;

    public MovieDetailsView(User user, Movie movie) {
        this.currentUser = user;
        this.movie = movie;
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();
        this.discountService = new DiscountService();

        setTitle(movie.getTitle() + " - Details");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for movie details
        JPanel movieDetailsPanel = new JPanel(new BorderLayout(15, 0));

        // Movie poster on the left
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setPreferredSize(new Dimension(150, 225));

        // Load movie poster
        ImageIcon posterIcon = null;
        try {
            String posterPath = movie.getPoster();
            if (posterPath != null && !posterPath.isEmpty()) {
                posterIcon = new ImageIcon(posterPath);
                // Scale the image to a reasonable size
                Image img = posterIcon.getImage();
                Image scaledImg = img.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
                posterIcon = new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            System.out.println("Error loading poster for " + movie.getTitle() + ": " + e.getMessage());
        }

        // If poster couldn't be loaded, use a placeholder
        if (posterIcon == null) {
            JLabel titleLabel = new JLabel(movie.getTitle(), JLabel.CENTER);
            titleLabel.setPreferredSize(new Dimension(150, 225));
            titleLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            posterPanel.add(titleLabel, BorderLayout.CENTER);
        } else {
            JLabel posterLabel = new JLabel(posterIcon);
            posterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            posterPanel.add(posterLabel, BorderLayout.CENTER);
        }

        movieDetailsPanel.add(posterPanel, BorderLayout.WEST);

        // Movie info on the right
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(titleLabel);

        infoPanel.add(new JLabel("Genre: " + movie.getGenre()));
        infoPanel.add(new JLabel("Release Date: " + movie.getReleaseDate()));
        infoPanel.add(new JLabel("Running Time: " + movie.getRunningTime() + " minutes"));
        infoPanel.add(new JLabel("Price: $" + String.format(Locale.US, "%.2f", movie.getPrice())));

        movieDetailsPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(movieDetailsPanel, BorderLayout.NORTH);

        // Center panel with synopsis and showtimes
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));

        // Synopsis
        JPanel synopsisPanel = new JPanel(new BorderLayout(0, 5));
        JLabel synopsisLabel = new JLabel("Synopsis:");
        synopsisLabel.setFont(new Font("Arial", Font.BOLD, 14));
        synopsisPanel.add(synopsisLabel, BorderLayout.NORTH);

        synopsisArea = new JTextArea(movie.getSynopsis());
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setEditable(false);
        synopsisArea.setRows(6);
        synopsisArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);
        synopsisPanel.add(synopsisScrollPane, BorderLayout.CENTER);

        centerPanel.add(synopsisPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for booking options
        JPanel bookingPanel = new JPanel(new GridBagLayout());
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Book Tickets"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Number of tickets
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        bookingPanel.add(ticketsLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ticketsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        bookingPanel.add(ticketsSpinner, gbc);

        // Discounts
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel discountsLabel = new JLabel("Available Discounts:");
        bookingPanel.add(discountsLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        discountComboBox = new JComboBox<>();
        // Add a "No Discount" option
        discountComboBox.addItem(new Discount("NONE", "No Discount", 0.0, true));
        // Add all active discounts from the service
        List<Discount> discounts = discountService.getAllDiscounts();
        for (Discount discount : discounts) {
            if (discount.isActive()) {
                discountComboBox.addItem(discount);
            }
        }
        // Set a custom renderer to show the description instead of toString()
        discountComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Discount) {
                    Discount discount = (Discount) value;
                    setText(discount.getDescription() + " (" + discount.getPercentage() + "%)");
                }
                return this;
            }
        });
        bookingPanel.add(discountComboBox, gbc);

        // Session date
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel sessionDateLabel = new JLabel("Session Date:");
        bookingPanel.add(sessionDateLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sessionDateComboBox = new JComboBox<>();

        // Add available dates (next 7 days)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);  // Add one day
            sessionDateComboBox.addItem(dateFormat.format(calendar.getTime()));
        }

        bookingPanel.add(sessionDateComboBox, gbc);

        // Session time
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel sessionTimeLabel = new JLabel("Session Time:");
        bookingPanel.add(sessionTimeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sessionTimeComboBox = new JComboBox<>();

        // Parse showtimes from the movie
        String[] showtimeArray = movie.getShowtimes().split(",");
        for (String showtime : showtimeArray) {
            sessionTimeComboBox.addItem(showtime.trim());
        }

        bookingPanel.add(sessionTimeComboBox, gbc);

        // Total price
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel totalPriceLabel = new JLabel("Total Price:");
        bookingPanel.add(totalPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceLabel = new JLabel(String.format(Locale.US, "%.2f", movie.getPrice()));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD));
        bookingPanel.add(priceLabel, gbc);

        // Buy button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buyButton = new JButton("Buy Tickets");
        buyButton.setPreferredSize(new Dimension(0, 35));
        buyButton.setFont(buyButton.getFont().deriveFont(Font.BOLD));
        buyButton.addActionListener(e -> processBooking());
        bookingPanel.add(buyButton, gbc);

        mainPanel.add(bookingPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Update price when spinner or discount changes
        ticketsSpinner.addChangeListener(e -> updatePrice());
        discountComboBox.addActionListener(e -> updatePrice());

        updatePrice();
    }

    private void updatePrice() {
        int numTickets = (Integer) ticketsSpinner.getValue();
        Discount selectedDiscount = (Discount) discountComboBox.getSelectedItem();

        double totalPrice = movie.getPrice() * numTickets;

        // Apply discount if selected and active
        if (selectedDiscount != null && selectedDiscount.isActive()) {
            // Special case for student discount - check if customer is a student
            if ("STUDENT".equals(selectedDiscount.getCode())) {
                Customer customer = customerService.getCustomerByUsername(currentUser.getUsername());
                if (customer != null && customer.isStudent()) {
                    totalPrice *= (1 - selectedDiscount.getPercentage() / 100.0);
                } else {
                    // If not a student, show a message and reset to "No Discount"
                    JOptionPane.showMessageDialog(this, 
                        "Student discount can only be applied to student accounts.", 
                        "Discount Not Applied", 
                        JOptionPane.INFORMATION_MESSAGE);
                    // Find and select the "No Discount" option
                    for (int i = 0; i < discountComboBox.getItemCount(); i++) {
                        Discount item = discountComboBox.getItemAt(i);
                        if ("NONE".equals(item.getCode())) {
                            discountComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    // Don't apply discount
                }
            } else if (!"NONE".equals(selectedDiscount.getCode())) {
                // Apply non-student discount
                totalPrice *= (1 - selectedDiscount.getPercentage() / 100.0);
            }
        }

        // Format with decimal point for consistency
        priceLabel.setText(String.format(Locale.US, "%.2f", totalPrice));
    }
    private void processBooking() {
        int numTickets = (Integer) ticketsSpinner.getValue();
        Discount selectedDiscount = (Discount) discountComboBox.getSelectedItem();

        // Parse the price
        double totalPrice = Double.parseDouble(priceLabel.getText().replace(',', '.'));
        String bookingId = bookingService.getNextBookingId();
        String bookingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sessionDate = (String) sessionDateComboBox.getSelectedItem();
        String sessionTime = (String) sessionTimeComboBox.getSelectedItem();

        // Normaliser le format de l'heure pour s'assurer qu'il soit au format HH:mm:ss
        if (sessionTime != null && sessionTime.matches("\\d{2}:\\d{2}")) {
            sessionTime = sessionTime + ":00"; // Ajouter les secondes si elles manquent
        }

        Booking booking = new Booking(
                bookingId,
                currentUser.getUsername(),
                movie.getId(),
                numTickets,
                totalPrice,
                bookingDate,
                sessionDate,
                sessionTime
        );
        bookingService.addBooking(booking);

        // Ouvre la nouvelle fenêtre de paiement
        SwingUtilities.invokeLater(() -> {
            PaymentView paymentView = new PaymentView(currentUser, movie, booking, selectedDiscount);
            paymentView.setVisible(true);
        });

        // Optionnel : ferme la fenêtre actuelle
        dispose();
    }


}
