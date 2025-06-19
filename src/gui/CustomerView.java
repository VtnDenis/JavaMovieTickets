package gui;

import model.Booking;
import model.Movie;
import model.User;
import service.BookingService;
import service.MovieService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerView extends JFrame {
    private User currentUser;
    private MovieService movieService;
    private BookingService bookingService;

    private JTabbedPane tabbedPane;
    private JPanel moviesPanel;
    private JPanel historyPanel;
    private JTable bookingHistoryTable;

    public CustomerView(User user) {
        this.currentUser = user;
        this.movieService = new MovieService();
        this.bookingService = new BookingService();

        setTitle("Customer View - " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize movies panel
        initMoviesPanel();

        // Initialize history panel
        initHistoryPanel();

        // Add tabs to tabbed pane
        tabbedPane.addTab("Movies", moviesPanel);
        tabbedPane.addTab("Booking History", historyPanel);

        add(tabbedPane);

        // Load booking history
        loadBookingHistory();
    }

    private void initMoviesPanel() {
        moviesPanel = new JPanel(new BorderLayout());
        moviesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Available Movies");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));

        // Add document listener to update results as user types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateMovieDisplay(searchField.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateMovieDisplay(searchField.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateMovieDisplay(searchField.getText());
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Create header panel to hold title and search
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        moviesPanel.add(headerPanel, BorderLayout.NORTH);

        // Initialize with all movies
        updateMovieDisplay("");
    }

    private void updateMovieDisplay(String searchText) {
        // Get all movies and filter based on search text
        List<Movie> allMovies = movieService.getAllMovies();
        List<Movie> filteredMovies = filterMovies(allMovies, searchText);

        // Create a panel for movie grid with GridLayout (rows of 3 movies)
        JPanel movieGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        movieGridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (Movie movie : filteredMovies) {
            JPanel moviePanel = new JPanel(new BorderLayout(0, 5));
            moviePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));

            // Make the panel clickable
            moviePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            moviePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openMovieDetails(movie);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    moviePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    moviePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                    ));
                }
            });

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
                JLabel movieTitlePlaceholder = new JLabel(movie.getTitle(), JLabel.CENTER);
                movieTitlePlaceholder.setPreferredSize(new Dimension(150, 225));
                movieTitlePlaceholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                moviePanel.add(movieTitlePlaceholder, BorderLayout.CENTER);
            } else {
                JLabel posterLabel = new JLabel(posterIcon);
                posterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                moviePanel.add(posterLabel, BorderLayout.CENTER);
            }

            // Add movie title
            JLabel movieTitleLabel = new JLabel(movie.getTitle(), JLabel.CENTER);
            movieTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            moviePanel.add(movieTitleLabel, BorderLayout.SOUTH);

            movieGridPanel.add(moviePanel);
        }

        // Add the movie grid to a scroll pane
        JScrollPane scrollPane = new JScrollPane(movieGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        // Remove previous content if it exists
        Component centerComponent = ((BorderLayout)moviesPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            moviesPanel.remove(centerComponent);
        }

        moviesPanel.add(scrollPane, BorderLayout.CENTER);
        moviesPanel.revalidate();
        moviesPanel.repaint();
    }

    private List<Movie> filterMovies(List<Movie> movies, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return movies; // Return all movies if search text is empty
        }

        String searchLower = searchText.toLowerCase().trim();
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(searchLower)) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }

    private void initHistoryPanel() {
        historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Your Booking History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        historyPanel.add(titleLabel, BorderLayout.NORTH);

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
    }

    private void openMovieDetails(Movie movie) {
        MovieDetailsView detailsView = new MovieDetailsView(currentUser, movie);
        detailsView.setVisible(true);

        // Add a window listener to refresh booking history when the details view is closed
        detailsView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadBookingHistory();
            }
        });
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
        String[] columnNames = {"Booking ID", "Movie", "Tickets", "Price", "Booking Date", "Session Date", "Session Time"};
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
                booking.getBookingDate(),
                booking.getSessionDate(),
                booking.getSessionTime()
            };
            model.addRow(row);
        }

        bookingHistoryTable.setModel(model);
    }
}
