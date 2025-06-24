# Movie Ticket Booking System

## Description
This Java application allows customers to book movie tickets and employees to manage movies and reservations. It uses Java Swing for the graphical interface and MySQL database for data storage.

The application was developed as part of an object-oriented programming project in Java, with the goal of creating a complete movie ticket booking system that meets the needs of both customers and cinema management.

## Features

### For Customers
- Login with username and password
- Browse available movies with their details (title, genre, release date, runtime, price)
- View movie posters and synopses
- View showtimes for each movie
- Book tickets with selection of number of seats
- Choose specific session dates and times
- Apply different discounts (student, welcome offers)
- Secure payment processing with card details
- View booking history

### For Employees
- View all bookings made
- Complete movie management (add, edit, delete)
- Manage movie posters and synopses
- View customer information
- Manage screens and showtimes
- Process payments
- View detailed sales statistics
- Access payment records

## Installation
1. Clone this repository
2. Open the project in IntelliJ IDEA or your preferred Java IDE
3. Make sure JDK 23 is installed
4. Set up a MySQL database named 'cinema' (import the SQL dump from `src/cinemadb.txt`)
5. Configure the database connection settings in `src/config.properties`
6. Run the `Main.java` class

## Usage

### Login Interface
The application opens with a login window where the user must enter their username and password. Depending on the user's role (customer or employee), the corresponding interface will be displayed.

### Customer Interface
The customer interface is organized into two tabs:
- **Book Tickets**: Allows booking tickets by selecting a movie, number of tickets, and an applicable discount
- **Booking History**: Displays the customer's booking history

### Employee Interface
The employee interface is organized into three tabs:
- **Manage Movies**: Allows adding, editing, or deleting movies
- **View Customers**: Displays the list of customers with their information
- **View Bookings**: Displays all bookings made

## Project Structure

### Class Diagram
```mermaid
classDiagram
direction LR

class JFrame {
  <<Java Swing Class>>
}

%% ==== MODEL ====
class User {
  +String username
  +String password
  +String role
  +getUsername(): String
  +getPassword(): String
  +getRole(): String
  +isEmployee(): boolean
  +isCustomer(): boolean
}

class Customer {
  +String username
  +String fullName
  +boolean student
  +getUsername(): String
  +getFullName(): String
  +isStudent(): boolean
  +setFullName(fullName: String): void
  +setStudent(student: boolean): void
}

class Movie {
  +int id
  +String title
  +String genre
  +String releaseDate
  +int runningTime
  +double price
  +String showtimes
  +toString(): String
}

class Booking {
  +String bookingId
  +String username
  +int movieId
  +int numTickets
  +double totalPrice
  +String bookingDate
}

class Discount {
  +String code
  +String description
  +double percentage
  +boolean active
  +getCode(): String
  +getDescription(): String
  +getPercentage(): double
  +isActive(): boolean
  +setDescription(desc: String): void
  +setPercentage(perc: double): void
  +setActive(active: boolean): void
}

%% ==== SERVICES ====
class UserService {
  +String USER_FILE_PATH
  +getAllUsers(): List~User~
  +authenticateUser(username: String, password: String): User
}

class CustomerService {
  +String CUSTOMER_FILE_PATH
  +getAllCustomers(): List~Customer~
  +getCustomerByUsername(username: String): Customer
  +saveCustomers(customers: List~Customer~): void
}

class MovieService {
  +String MOVIE_FILE_PATH
  +getAllMovies(): List~Movie~
  +getMovieById(id: int): Movie
  +saveMovies(movies: List~Movie~): void
  +addMovie(movie: Movie): void
  +updateMovie(movie: Movie): void
  +deleteMovie(id: int): void
}

class BookingService {
  +String BOOKING_FILE_PATH
  +getAllBookings(): List~Booking~
  +saveBookings(bookings: List~Booking~): void
  +addBooking(booking: Booking): void
  +getNextBookingId(): String
}

class DiscountService {
  +String DISCOUNT_FILE_PATH
  +getAllDiscounts(): List~Discount~
  +saveDiscounts(discounts: List~Discount~): void
  +addDiscount(discount: Discount): void
  +updateDiscount(discount: Discount): void
  +deleteDiscount(code: String): void
  +getDiscountByCode(code: String): Discount
}

%% ==== GUI ====
class LoginWindow {
  +JTextField usernameField
  +JPasswordField passwordField
  +JButton loginButton
  +UserService userService
  +authenticate(): void
}

class CustomerView {
  +User currentUser
  +MovieService movieService
  +CustomerService customerService
  +BookingService bookingService
  +DiscountService discountService
  +JComboBox movieComboBox
  +JSpinner ticketsSpinner
  +JComboBox discountComboBox
  +JLabel priceLabel
  +JButton buyButton
  +JTabbedPane tabbedPane
  +JTable bookingHistoryTable
  +updatePrice(): void
  +processBooking(): void
  +loadBookingHistory(): void
}

class EmployeeView {
  +MovieService movieService
  +CustomerService customerService
  +BookingService bookingService
  +JTabbedPane tabbedPane
  +JTable movieTable
  +JTable customerTable
  +JTable bookingTable
  +loadMovies(): void
  +loadCustomers(): void
  +loadBookings(): void
  +addMovieAction(): void
  +editMovieAction(): void
  +deleteMovieAction(): void
}

%% ==== MAIN ====
class Main {
  +main(args: String[]): void
}

%% ==== HERITAGE ====
JFrame <|-- LoginWindow
JFrame <|-- CustomerView
JFrame <|-- EmployeeView

%% ==== ASSOCIATIONS ====
UserService "1" o-- "*" User : manages
CustomerService "1" o-- "*" Customer : manages
MovieService "1" o-- "*" Movie : manages
BookingService "1" o-- "*" Booking : manages
DiscountService "1" o-- "*" Discount : manages

LoginWindow --> UserService : userService
CustomerView --> User : currentUser
CustomerView --> MovieService : movieService
CustomerView --> CustomerService : customerService
CustomerView --> BookingService : bookingService
CustomerView --> DiscountService : discountService
EmployeeView --> MovieService
EmployeeView --> CustomerService
EmployeeView --> BookingService

%% ==== DEPENDANCES ====
Main ..> LoginWindow
LoginWindow ..> UserService
LoginWindow ..> CustomerView
LoginWindow ..> EmployeeView
CustomerView ..> Movie
CustomerView ..> Booking
CustomerView ..> Discount
CustomerView ..> MovieService
CustomerView ..> CustomerService
CustomerView ..> BookingService
CustomerView ..> DiscountService
EmployeeView ..> Movie
EmployeeView ..> Customer
EmployeeView ..> Booking
EmployeeView ..> MovieService
EmployeeView ..> CustomerService
EmployeeView ..> BookingService

%% ==== FOREIGN KEY ====
Booking ..> User : via username
Booking ..> Movie : via movieId
%% ---- Styling for packages ----
classDef modelColor fill:#f9f,stroke:#333,stroke-width:2px
classDef serviceColor fill:#9cf,stroke:#333,stroke-width:2px
classDef guiColor fill:#9fc,stroke:#333,stroke-width:2px
classDef defaultColor fill:#lightgrey,stroke:#333,stroke-width:2px
classDef externalsColor fill:#eee,stroke:#666,stroke-width:1px,color:black

%% === Attributes of the model package ===
class User ::: modelColor
class Customer ::: modelColor
class Movie ::: modelColor
class Booking ::: modelColor
class Discount ::: modelColor

%% === Attributes of the service package ===
class UserService ::: serviceColor
class CustomerService ::: serviceColor
class MovieService ::: serviceColor
class BookingService ::: serviceColor
class DiscountService ::: serviceColor

%% === Attributes of the gui package ===
class LoginWindow ::: guiColor
class CustomerView ::: guiColor
class EmployeeView ::: guiColor

%% === Main class (default) ===
class Main ::: defaultColor

%% === JFrame class (Swing) ===
class JFrame ::: externalsColor
```

### Package Organization
- `model/`: contains model classes (User, Customer, Movie, Booking, Discount)
- `service/`: contains service classes for data management (UserService, CustomerService, MovieService, BookingService, DiscountService)
- `gui/`: contains user interface classes (LoginWindow, CustomerView, EmployeeView)

## Database Structure
The application uses a MySQL database with the following tables:
- `users`: contains user authentication information (username, password, role)
- `customer`: contains customer information (username, fullName, student)
- `movie`: contains movie information (id, title, genre, releaseDate, runningTime, price, showtimes, poster, synopsis)
- `booking`: contains booking information (bookingId, username, movieId, numTickets, totalPrice, bookingDate, sessionDate, sessionTime)
- `discount`: contains information about available discounts (code, description, percentage, active)
- `payment`: tracks payment information (paymentId, bookingId, paymentDate, amount, paymentMethod, card details)
- `screen`: manages cinema screens (screenId, movieId, capacity)

## Discount System
The application implements a flexible discount system:
- Student discount (10%): verification of student status in the customer profile
- Welcome discount (5%): for new customers (code: BIENVENUE5)
- Additional discounts can be added through the database

## Test Accounts

### Customers
- Username: emma.lune, Password: azerty123
- Username: louis.dupont, Password: monpass45

### Employees
- Username: claire.baudoin, Password: admin789
- Username: marc.lemans, Password: gestion2024

## Technologies Used
- Java 23
- Java Swing for the graphical interface
- MySQL database for data persistence
- JDBC for database connectivity

## Architecture
The application follows a three-tier architecture:
1. **Presentation Layer**: GUI classes (LoginWindow, CustomerView, EmployeeView)
2. **Business Layer**: Services (UserService, CustomerService, MovieService, BookingService, DiscountService, PaymentService, ScreenService)
3. **Data Layer**: Models (User, Customer, Movie, Booking, Discount, Payment, Screen) and MySQL database

This architecture allows for a clear separation of concerns and facilitates code maintenance.

## Security Features
- Password hashing for user authentication
- Card details protection in payment processing
- Input validation to prevent SQL injection
- Session management for authenticated users
