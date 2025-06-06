package gui;

import model.Customer;
import model.Movie;
import model.Booking;
import service.CustomerService;
import service.MovieService;
import service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeView extends JFrame {
    private MovieService movieService;
    private CustomerService customerService;
    private BookingService bookingService;

    private JTabbedPane tabbedPane;

    private JPanel movieManagementPanel;
    private JTable movieTable;
    private JButton addMovieButton, editMovieButton, deleteMovieButton;

    private JPanel customerListPanel;
    private JTable customerTable;

    private JPanel bookingListPanel;
    private JTable bookingTable;

    public EmployeeView() {
        this.movieService = new MovieService();
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();

        setTitle("Employee Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        initMovieManagementPanel();
        initCustomerListPanel();
        initBookingListPanel();

        tabbedPane.addTab("Manage Movies", movieManagementPanel);
        tabbedPane.addTab("View Customers", customerListPanel);
        tabbedPane.addTab("View Bookings", bookingListPanel);

        add(tabbedPane);
        loadMovies();
        loadCustomers();
        loadBookings();
    }

    private void initMovieManagementPanel() {
        movieManagementPanel = new JPanel(new BorderLayout());
        movieTable = new JTable();
        movieManagementPanel.add(new JScrollPane(movieTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addMovieButton = new JButton("Add Movie");
        editMovieButton = new JButton("Edit Movie");
        deleteMovieButton = new JButton("Delete Movie");

        addMovieButton.addActionListener(e -> addMovieAction());
        editMovieButton.addActionListener(e -> editMovieAction());
        deleteMovieButton.addActionListener(e -> deleteMovieAction());

        buttonPanel.add(addMovieButton);
        buttonPanel.add(editMovieButton);
        buttonPanel.add(deleteMovieButton);
        movieManagementPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initCustomerListPanel() {
        customerListPanel = new JPanel(new BorderLayout());
        customerTable = new JTable();
        customerListPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }

    private void initBookingListPanel() {
        bookingListPanel = new JPanel(new BorderLayout());
        bookingTable = new JTable();
        bookingListPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
    }

    private void loadMovies() {
        List<Movie> movies = movieService.getAllMovies();
        String[] columnNames = {"ID", "Title", "Genre", "Release Date", "Running Time", "Price", "Showtimes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Movie movie : movies) {
            Object[] row = {movie.getId(), movie.getTitle(), movie.getGenre(), movie.getReleaseDate(), movie.getRunningTime(), movie.getPrice(), movie.getShowtimes()};
            model.addRow(row);
        }
        movieTable.setModel(model);
    }

    private void loadCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        String[] columnNames = {"Username", "Full Name", "Is Student"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Customer customer : customers) {
            Object[] row = {customer.getUsername(), customer.getFullName(), customer.isStudent()};
            model.addRow(row);
        }
        customerTable.setModel(model);
    }

    private void loadBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        String[] columnNames = {"Booking ID", "Username", "Movie ID", "Num Tickets", "Total Price", "Booking Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Booking booking : bookings) {
            Object[] row = {booking.getBookingId(), booking.getUsername(), booking.getMovieId(), booking.getNumTickets(), booking.getTotalPrice(), booking.getBookingDate()};
            model.addRow(row);
        }
        bookingTable.setModel(model);
    }

    private void addMovieAction() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField releaseDateField = new JTextField();
        JTextField runningTimeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField showtimesField = new JTextField();

        Object[] message = {
            "ID:", idField,
            "Title:", titleField,
            "Genre:", genreField,
            "Release Date (dd/MM/yyyy):", releaseDateField,
            "Running Time (mins):", runningTimeField,
            "Price:", priceField,
            "Showtimes (e.g. 10:00,14:30,19:00):", showtimesField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Movie", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String genre = genreField.getText();
            String releaseDate = releaseDateField.getText();
            int runningTime = Integer.parseInt(runningTimeField.getText());
            double price = Double.parseDouble(priceField.getText());
            String showtimes = showtimesField.getText();
            Movie movie = new Movie(id, title, genre, releaseDate, runningTime, price, showtimes);
            movieService.addMovie(movie);
            loadMovies();
        }
    }

    private void editMovieAction() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            int movieId = (Integer) movieTable.getValueAt(selectedRow, 0);
            Movie movieToEdit = movieService.getMovieById(movieId);

            JTextField titleField = new JTextField(movieToEdit.getTitle());
            JTextField genreField = new JTextField(movieToEdit.getGenre());
            JTextField releaseDateField = new JTextField(movieToEdit.getReleaseDate());
            JTextField runningTimeField = new JTextField(String.valueOf(movieToEdit.getRunningTime()));
            JTextField priceField = new JTextField(String.valueOf(movieToEdit.getPrice()));
            JTextField showtimesField = new JTextField(movieToEdit.getShowtimes());

            Object[] message = {
                "Title:", titleField,
                "Genre:", genreField,
                "Release Date (dd/MM/yyyy):", releaseDateField,
                "Running Time (mins):", runningTimeField,
                "Price:", priceField,
                "Showtimes (e.g. 10:00,14:30,19:00):", showtimesField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Movie", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                movieToEdit.setTitle(titleField.getText());
                movieToEdit.setGenre(genreField.getText());
                movieToEdit.setReleaseDate(releaseDateField.getText());
                movieToEdit.setRunningTime(Integer.parseInt(runningTimeField.getText()));
                movieToEdit.setPrice(Double.parseDouble(priceField.getText()));
                movieToEdit.setShowtimes(showtimesField.getText());
                movieService.updateMovie(movieToEdit);
                loadMovies();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a movie to edit.");
        }
    }

    private void deleteMovieAction() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            int movieId = (Integer) movieTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this movie?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                movieService.deleteMovie(movieId);
                loadMovies();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a movie to delete.");
        }
    }
}
