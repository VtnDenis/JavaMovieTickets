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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerView extends JFrame {
    private User currentUser;
    private MovieService movieService;
    private CustomerService customerService;
    private BookingService bookingService;
    private DiscountService discountService;

    private Movie selectedMovie;
    private JTextArea showtimesValueArea;
    private JSpinner ticketsSpinner;
    private JComboBox<Discount> discountComboBox;
    private JLabel priceLabel;
    private JButton buyButton;

    private JTabbedPane tabbedPane;
    private JPanel bookingPanel;
    private JPanel historyPanel;
    private JTable bookingHistoryTable;

    public CustomerView(User user) {
        this.currentUser = user;
        this.movieService = new MovieService();
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();
        this.discountService = new DiscountService();

        setTitle("Customer View - " + currentUser.getUsername());
        setSize(600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize booking panel
        bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Use GridBagLayout for better control over component placement
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Movie selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel movieLabel = new JLabel("Select Movie:");
        selectionPanel.add(movieLabel, gbc);

        // Create a panel for movie selection with GridLayout (rows of 2 movies)
        JPanel movieSelectionPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        movieSelectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        List<Movie> movies = movieService.getAllMovies();
        ButtonGroup movieButtonGroup = new ButtonGroup();
        
        for (Movie movie : movies) {
            JPanel moviePanel = new JPanel(new BorderLayout(0, 5));
            moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Load movie poster
            ImageIcon posterIcon = null;
            try {
                String posterPath = movie.getPoster();
                if (posterPath != null && !posterPath.isEmpty()) {
                    posterIcon = new ImageIcon(posterPath);
                    // Scale the image to a reasonable size
                    Image img = posterIcon.getImage();
                    Image scaledImg = img.getScaledInstance(120, 180, Image.SCALE_SMOOTH);
                    posterIcon = new ImageIcon(scaledImg);
                }
            } catch (Exception e) {
                System.out.println("Error loading poster for " + movie.getTitle() + ": " + e.getMessage());
            }
            
            // If poster couldn't be loaded, use a placeholder
            if (posterIcon == null) {
                JLabel titleLabel = new JLabel(movie.getTitle(), JLabel.CENTER);
                titleLabel.setPreferredSize(new Dimension(120, 180));
                titleLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                moviePanel.add(titleLabel, BorderLayout.CENTER);
            } else {
                JLabel posterLabel = new JLabel(posterIcon);
                posterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                moviePanel.add(posterLabel, BorderLayout.CENTER);
            }
            
            // Add checkbox for selection
            JRadioButton movieRadioButton = new JRadioButton(movie.getTitle());
            movieRadioButton.setHorizontalAlignment(JRadioButton.CENTER);
            movieButtonGroup.add(movieRadioButton);
            moviePanel.add(movieRadioButton, BorderLayout.SOUTH);
            
            // Add action listener to handle movie selection
            movieRadioButton.addActionListener(e -> {
                selectedMovie = movie;
                // Format showtimes for better readability
                String showtimes = movie.getShowtimes();
                showtimes = showtimes.replace("|", ", ");
                showtimesValueArea.setText(showtimes);
                updatePrice();
            });
            
            movieSelectionPanel.add(moviePanel);
        }
        
        // Set the first movie as selected by default if there are movies
        if (!movies.isEmpty()) {
            selectedMovie = movies.get(0);
            // Select the first radio button
            if (movieButtonGroup.getButtonCount() > 0) {
                JRadioButton firstButton = (JRadioButton) movieButtonGroup.getElements().nextElement();
                firstButton.setSelected(true);
            }
        }
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane movieScrollPane = new JScrollPane(movieSelectionPanel);
        movieScrollPane.setPreferredSize(new Dimension(0, 500));
        selectionPanel.add(movieScrollPane, gbc);

        // Showtimes
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel showtimesLabel = new JLabel("Showtimes:");
        selectionPanel.add(showtimesLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        // Use JTextArea instead of JLabel for better text wrapping
        showtimesValueArea = new JTextArea(2, 20);
        showtimesValueArea.setEditable(false);
        showtimesValueArea.setLineWrap(true);
        showtimesValueArea.setWrapStyleWord(true);
        showtimesValueArea.setBackground(selectionPanel.getBackground());
        showtimesValueArea.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane showtimesScrollPane = new JScrollPane(showtimesValueArea);
        showtimesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        selectionPanel.add(showtimesScrollPane, gbc);
        
        // Initialize showtimes for the default selected movie
        if (selectedMovie != null) {
            String showtimes = selectedMovie.getShowtimes();
            showtimes = showtimes.replace("|", ", ");
            showtimesValueArea.setText(showtimes);
        }

        // Number of tickets
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        selectionPanel.add(ticketsLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ticketsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        selectionPanel.add(ticketsSpinner, gbc);

        // Discounts
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel discountsLabel = new JLabel("Available Discounts:");
        selectionPanel.add(discountsLabel, gbc);

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
        selectionPanel.add(discountComboBox, gbc);

        // Total price
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel totalPriceLabel = new JLabel("Total Price:");
        selectionPanel.add(totalPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceLabel = new JLabel("0.0");
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD));
        selectionPanel.add(priceLabel, gbc);

        bookingPanel.add(selectionPanel, BorderLayout.CENTER);

        // Buy button panel with full width button
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        buyButton = new JButton("Buy Tickets");
        buyButton.setPreferredSize(new Dimension(0, 35)); // Height only, width will expand
        buyButton.setFont(buyButton.getFont().deriveFont(Font.BOLD));
        buyButton.addActionListener(e -> processBooking());

        buttonPanel.add(buyButton, BorderLayout.CENTER);
        bookingPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Initialize history panel
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bookingHistoryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookingHistoryTable);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh button with improved styling
        JPanel refreshPanel = new JPanel(new BorderLayout(10, 0));
        refreshPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton refreshButton = new JButton("Refresh Bookings");
        refreshButton.setPreferredSize(new Dimension(0, 35)); // Height only, width will expand
        refreshButton.setFont(refreshButton.getFont().deriveFont(Font.BOLD));
        refreshButton.addActionListener(e -> loadBookingHistory());

        refreshPanel.add(refreshButton, BorderLayout.CENTER);
        historyPanel.add(refreshPanel, BorderLayout.SOUTH);

        // Add tabs to tabbed pane
        tabbedPane.addTab("Book Tickets", bookingPanel);
        tabbedPane.addTab("Booking History", historyPanel);

        add(tabbedPane);

        // Load booking history
        loadBookingHistory();

        // Update price when spinner or discount changes
        ticketsSpinner.addChangeListener(e -> updatePrice());
        discountComboBox.addActionListener(e -> updatePrice());

        updatePrice();
    }

    private void updatePrice() {
        int numTickets = (Integer) ticketsSpinner.getValue();
        Discount selectedDiscount = (Discount) discountComboBox.getSelectedItem();

        if (selectedMovie != null) {
            double totalPrice = selectedMovie.getPrice() * numTickets;

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

            // Utiliser un format avec point décimal pour la cohérence
            priceLabel.setText(String.format(Locale.US, "%.2f", totalPrice));
        }
    }

    private void processBooking() {
        int numTickets = (Integer) ticketsSpinner.getValue();
        Discount selectedDiscount = (Discount) discountComboBox.getSelectedItem();

        // Corriger le parsing du prix en remplaçant la virgule par un point
        double totalPrice = Double.parseDouble(priceLabel.getText().replace(',', '.'));
        String bookingId = bookingService.getNextBookingId();
        String bookingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Booking booking = new Booking(bookingId, currentUser.getUsername(), selectedMovie.getId(), numTickets, totalPrice, bookingDate);
        bookingService.addBooking(booking);

        // Prepare confirmation message
        StringBuilder message = new StringBuilder();
        message.append("Payment Successful!\n\n");
        message.append("Booking ID: ").append(bookingId).append("\n");
        message.append("Movie: ").append(selectedMovie.getTitle()).append("\n");
        message.append("Tickets: ").append(numTickets).append("\n");

        // Add discount information if applicable
        if (selectedDiscount != null && !"NONE".equals(selectedDiscount.getCode()) && selectedDiscount.isActive()) {
            message.append("Discount Applied: ").append(selectedDiscount.getDescription())
                   .append(" (").append(selectedDiscount.getPercentage()).append("%)").append("\n");
        }

        message.append("Total Price: $").append(String.format(Locale.US, "%.2f", totalPrice));

        JOptionPane.showMessageDialog(this, message.toString(), "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);

        // Refresh booking history
        loadBookingHistory();
    }

    private void loadBookingHistory() {
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Movie> allMovies = movieService.getAllMovies();

        // Filter bookings for current user
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : allBookings) {
            if (booking.getUsername().equals(currentUser.getUsername())) {
                userBookings.add(booking);
            }
        }

        // Create table model
        String[] columnNames = {"Booking ID", "Movie", "Tickets", "Price", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add bookings to table
        for (Booking booking : userBookings) {
            // Find movie title
            String movieTitle = "Unknown";
            for (Movie movie : allMovies) {
                if (movie.getId() == booking.getMovieId()) {
                    movieTitle = movie.getTitle();
                    break;
                }
            }

            Object[] row = {
                booking.getBookingId(),
                movieTitle,
                booking.getNumTickets(),
                String.format(Locale.US, "%.2f", booking.getTotalPrice()),
                booking.getBookingDate()
            };
            model.addRow(row);
        }

        bookingHistoryTable.setModel(model);
    }
}