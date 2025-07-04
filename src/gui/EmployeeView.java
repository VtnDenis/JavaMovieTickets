package gui;

import model.Customer;
import model.Discount;
import model.Movie;
import model.Booking;
import model.User;
import service.CustomerService;
import service.DiscountService;
import service.MovieService;
import service.BookingService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeView extends JFrame {
    private MovieService movieService;
    private CustomerService customerService;
    private BookingService bookingService;
    private DiscountService discountService;
    private UserService userService;

    private JPanel userManagementPanel;
    private JTable userTable;
    private JButton addUserButton, editUserButton, deleteUserButton;

    private JTabbedPane tabbedPane;

    private JPanel movieManagementPanel;
    private JTable movieTable;
    private JButton addMovieButton, editMovieButton, deleteMovieButton;

    private JPanel customerListPanel;
    private JTable customerTable;
    private JButton addCustomerButton, editCustomerButton, deleteCustomerButton;

    private JPanel bookingListPanel;
    private JTable bookingTable;

    private JPanel discountManagementPanel;
    private JTable discountTable;
    private JButton addDiscountButton, editDiscountButton, deleteDiscountButton, toggleDiscountButton;

    public EmployeeView() {
        this.movieService = new MovieService();
        this.customerService = new CustomerService();
        this.bookingService = new BookingService();
        this.discountService = new DiscountService();
        this.userService = new UserService();

        setTitle("Employee Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        initMovieManagementPanel();
        initCustomerListPanel();
        initBookingListPanel();
        initDiscountManagementPanel();
        initUserManagementPanel();

        tabbedPane.addTab("Manage Movies", movieManagementPanel);
        tabbedPane.addTab("Manage Customers", customerListPanel);
        tabbedPane.addTab("View Bookings", bookingListPanel);
        tabbedPane.addTab("Manage Discounts", discountManagementPanel);
        tabbedPane.addTab("Manage Users", userManagementPanel);

        add(tabbedPane);
        loadMovies();
        loadCustomers();
        loadBookings();
        loadDiscounts();
        loadUsers();
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

        JPanel buttonPanel = new JPanel();
        addCustomerButton = new JButton("Add Customer");
        editCustomerButton = new JButton("Edit Customer");
        deleteCustomerButton = new JButton("Delete Customer");

        addCustomerButton.addActionListener(e -> addCustomerAction());
        editCustomerButton.addActionListener(e -> editCustomerAction());
        deleteCustomerButton.addActionListener(e -> deleteCustomerAction());

        buttonPanel.add(addCustomerButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(deleteCustomerButton);
        customerListPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initDiscountManagementPanel() {
        discountManagementPanel = new JPanel(new BorderLayout());
        discountTable = new JTable();
        discountManagementPanel.add(new JScrollPane(discountTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addDiscountButton = new JButton("Add Discount");
        editDiscountButton = new JButton("Edit Discount");
        deleteDiscountButton = new JButton("Delete Discount");
        toggleDiscountButton = new JButton("Toggle Active");

        addDiscountButton.addActionListener(e -> addDiscountAction());
        editDiscountButton.addActionListener(e -> editDiscountAction());
        deleteDiscountButton.addActionListener(e -> deleteDiscountAction());
        toggleDiscountButton.addActionListener(e -> toggleDiscountAction());

        buttonPanel.add(addDiscountButton);
        buttonPanel.add(editDiscountButton);
        buttonPanel.add(deleteDiscountButton);
        buttonPanel.add(toggleDiscountButton);
        discountManagementPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initBookingListPanel() {
        bookingListPanel = new JPanel(new BorderLayout());
        bookingTable = new JTable();
        bookingListPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
    }

    private void loadMovies() { /// adapter le jframe
        List<Movie> movies = movieService.getAllMovies();

        String[] columnNames = {
                "ID", "Title", "Genre", "Release Date", "Duration (min)", "Price (£)", "Showtimes", "Poster", "Synopsis"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Movie movie : movies) {
            Object[] row = {
                    movie.getId(),
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getReleaseDate(),                       // java.sql.Date ou String
                    movie.getRunningTime(),                       // durée en minutes
                    String.format("%.2f", movie.getPrice()),      // prix formaté
                    movie.getShowtimes(),                         // texte, ex: "14:00, 17:00"
                    movie.getPoster(),                            // URL ou nom du fichier image
                    movie.getSynopsis()                           // texte description
            };
            model.addRow(row);
        }
        movieTable.setModel(model);
        movieTable.setEnabled(false); // optionnel : empêche modification dans la JTable
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

    private void loadDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();
        String[] columnNames = {"Code", "Description", "Percentage", "Active"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Discount discount : discounts) {
            Object[] row = {discount.getCode(), discount.getDescription(), discount.getPercentage(), discount.isActive()};
            model.addRow(row);
        }
        discountTable.setModel(model);
    }

    private void addMovieAction() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField releaseDateField = new JTextField(); // format : yyyy-MM-dd
        JTextField runningTimeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField showtimesField = new JTextField();
        JTextField posterField = new JTextField(); // pour URL ou chemin de l’affiche
        JTextArea synopsisArea = new JTextArea(5, 20);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);

        Object[] message = {
                "ID:", idField,
                "Title:", titleField,
                "Genre:", genreField,
                "Release Date (yyyy-MM-dd):", releaseDateField,
                "Running Time (mins):", runningTimeField,
                "Price:", priceField,
                "Showtimes (e.g. 10:00,14:30,19:00):", showtimesField,
                "Poster (URL or path):", posterField,
                "Synopsis:", synopsisScrollPane
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                String genre = genreField.getText();
                String releaseDate = releaseDateField.getText();
                int runningTime = Integer.parseInt(runningTimeField.getText());
                double price = Double.parseDouble(priceField.getText());
                String showtimes = showtimesField.getText();
                String poster = posterField.getText();
                String synopsis = synopsisArea.getText();

                Movie movie = new Movie(id, title, genre, releaseDate, runningTime, price, showtimes, poster, synopsis);
                movieService.addMovie(movie);
                loadMovies();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding the movie: " + ex.getMessage());
            }
        }
    }


    private void editMovieAction() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow >= 0) {
            int movieId = (Integer) movieTable.getValueAt(selectedRow, 0);
            Movie movieToEdit = movieService.getMovieById(movieId);

            JTextField titleField = new JTextField(movieToEdit.getTitle());
            JTextField genreField = new JTextField(movieToEdit.getGenre());
            JTextField releaseDateField = new JTextField(movieToEdit.getReleaseDate()); // format : yyyy-MM-dd
            JTextField runningTimeField = new JTextField(String.valueOf(movieToEdit.getRunningTime()));
            JTextField priceField = new JTextField(String.valueOf(movieToEdit.getPrice()));
            JTextField showtimesField = new JTextField(movieToEdit.getShowtimes());
            JTextField posterField = new JTextField(movieToEdit.getPoster());
            JTextArea synopsisArea = new JTextArea(movieToEdit.getSynopsis(), 5, 20);
            synopsisArea.setLineWrap(true);
            synopsisArea.setWrapStyleWord(true);
            JScrollPane synopsisScrollPane = new JScrollPane(synopsisArea);

            Object[] message = {
                    "Title:", titleField,
                    "Genre:", genreField,
                    "Release Date (yyyy-MM-dd):", releaseDateField,
                    "Running Time (mins):", runningTimeField,
                    "Price:", priceField,
                    "Showtimes (e.g. 10:00,14:30,19:00):", showtimesField,
                    "Poster (URL or path):", posterField,
                    "Synopsis:", synopsisScrollPane
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    movieToEdit.setTitle(titleField.getText());
                    movieToEdit.setGenre(genreField.getText());
                    movieToEdit.setReleaseDate(releaseDateField.getText()); // vérifie bien que la date est au format attendu
                    movieToEdit.setRunningTime(Integer.parseInt(runningTimeField.getText()));
                    movieToEdit.setPrice(Double.parseDouble(priceField.getText()));
                    movieToEdit.setShowtimes(showtimesField.getText());
                    movieToEdit.setPoster(posterField.getText());
                    movieToEdit.setSynopsis(synopsisArea.getText());

                    movieService.updateMovie(movieToEdit);
                    loadMovies();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating the movie: " + ex.getMessage());
                }
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

    private void addCustomerAction() {
        JTextField usernameField = new JTextField();
        JTextField fullNameField = new JTextField();
        JCheckBox studentCheckBox = new JCheckBox();

        Object[] message = {
            "Username:", usernameField,
            "Full Name:", fullNameField,
            "Is Student:", studentCheckBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            boolean isStudent = studentCheckBox.isSelected();

            // Check if username already exists
            if (customerService.getCustomerByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "A customer with this username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = new Customer(username, fullName, isStudent);
            customerService.addCustomer(customer);
            loadCustomers();
        }
    }

    private void editCustomerAction() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) customerTable.getValueAt(selectedRow, 0);
            Customer customerToEdit = customerService.getCustomerByUsername(username);

            if (customerToEdit != null) {
                JTextField fullNameField = new JTextField(customerToEdit.getFullName());
                JCheckBox studentCheckBox = new JCheckBox();
                studentCheckBox.setSelected(customerToEdit.isStudent());

                Object[] message = {
                    "Full Name:", fullNameField,
                    "Is Student:", studentCheckBox
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    customerToEdit.setFullName(fullNameField.getText());
                    customerToEdit.setStudent(studentCheckBox.isSelected());
                    customerService.updateCustomer(customerToEdit);
                    loadCustomers();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
        }
    }

    private void deleteCustomerAction() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) customerTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerService.deleteCustomer(username);
                loadCustomers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
        }
    }

    private void addDiscountAction() {
        JTextField codeField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField percentageField = new JTextField();
        JCheckBox activeCheckBox = new JCheckBox();
        activeCheckBox.setSelected(true); // Default to active

        Object[] message = {
            "Code:", codeField,
            "Description:", descriptionField,
            "Percentage:", percentageField,
            "Active:", activeCheckBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Discount", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String code = codeField.getText().toUpperCase(); // Convert to uppercase for consistency
            String description = descriptionField.getText();
            double percentage;

            try {
                percentage = Double.parseDouble(percentageField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid percentage.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean active = activeCheckBox.isSelected();

            // Check if code already exists
            if (discountService.getDiscountByCode(code) != null) {
                JOptionPane.showMessageDialog(this, "A discount with this code already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Discount discount = new Discount(code, description, percentage, active);
            discountService.addDiscount(discount);
            loadDiscounts();
        }
    }

    private void editDiscountAction() {
        int selectedRow = discountTable.getSelectedRow();
        if (selectedRow >= 0) {
            String code = (String) discountTable.getValueAt(selectedRow, 0);
            Discount discountToEdit = discountService.getDiscountByCode(code);

            if (discountToEdit != null) {
                JTextField descriptionField = new JTextField(discountToEdit.getDescription());
                JTextField percentageField = new JTextField(String.valueOf(discountToEdit.getPercentage()));
                JCheckBox activeCheckBox = new JCheckBox();
                activeCheckBox.setSelected(discountToEdit.isActive());

                Object[] message = {
                    "Description:", descriptionField,
                    "Percentage:", percentageField,
                    "Active:", activeCheckBox
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edit Discount", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    double percentage;
                    try {
                        percentage = Double.parseDouble(percentageField.getText());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid percentage.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    discountToEdit.setDescription(descriptionField.getText());
                    discountToEdit.setPercentage(percentage);
                    discountToEdit.setActive(activeCheckBox.isSelected());
                    discountService.updateDiscount(discountToEdit);
                    loadDiscounts();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a discount to edit.");
        }
    }

    private void deleteDiscountAction() {
        int selectedRow = discountTable.getSelectedRow();
        if (selectedRow >= 0) {
            String code = (String) discountTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this discount?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                discountService.deleteDiscount(code);
                loadDiscounts();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a discount to delete.");
        }
    }

    private void toggleDiscountAction() {
        int selectedRow = discountTable.getSelectedRow();
        if (selectedRow >= 0) {
            String code = (String) discountTable.getValueAt(selectedRow, 0);
            Discount discount = discountService.getDiscountByCode(code);

            if (discount != null) {
                // Toggle the active status
                discount.setActive(!discount.isActive());
                discountService.updateDiscount(discount);
                loadDiscounts();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a discount to toggle its active status.");
        }
    }

    private void initUserManagementPanel() {
        userManagementPanel = new JPanel(new BorderLayout());
        userTable = new JTable();
        userManagementPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addUserButton = new JButton("Add Employee");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");

        addUserButton.addActionListener(e -> addUserAction());
        editUserButton.addActionListener(e -> editUserAction());
        deleteUserButton.addActionListener(e -> deleteUserAction());

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        userManagementPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        List<User> users = userService.getAllUsers();
        String[] columnNames = {"Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (User user : users) {
            Object[] row = {user.getUsername(), user.getRole()};
            model.addRow(row);
        }
        userTable.setModel(model);
    }

    private void addUserAction() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create employee user (isEmployee = true)
            boolean created = userService.createUser(username, password, true);
            if (created) {
                JOptionPane.showMessageDialog(this, "Employee account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Creation failed. Please ensure:\n" +
                    "- Username is at least 3 characters without spaces\n" +
                    "- Password is at least 8 characters with at least one letter and one digit\n" +
                    "- Username is not already taken", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editUserAction() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) userTable.getValueAt(selectedRow, 0);

            JPasswordField passwordField = new JPasswordField();

            Object[] message = {
                "New Password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Change User Password", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String password = new String(passwordField.getPassword());

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // For now, we'll just show a message since we don't have a method to update passwords
                JOptionPane.showMessageDialog(this, "Password change functionality not implemented yet.", "Not Implemented", JOptionPane.INFORMATION_MESSAGE);
                // In a real implementation, you would call a method like userService.updatePassword(username, password)
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }

    private void deleteUserAction() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) userTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // For now, we'll just show a message since we don't have a method to delete users
                JOptionPane.showMessageDialog(this, "User deletion functionality not implemented yet.", "Not Implemented", JOptionPane.INFORMATION_MESSAGE);
                // In a real implementation, you would call a method like userService.deleteUser(username)
                // loadUsers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }
}
