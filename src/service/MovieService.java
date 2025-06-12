package service;

import model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieService {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM movie";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Movie movie = new Movie(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("releaseDate"),
                            rs.getInt("runningTime"),
                            rs.getDouble("price"),
                            rs.getString("showtimes"),
                            rs.getString("poster"),
                            rs.getString("synopsis")
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des films : " + e.getMessage());
        }
        return movies;
    }

    public Movie getMovieById(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM movie WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("releaseDate"),
                            rs.getInt("runningTime"),
                            rs.getDouble("price"),
                            rs.getString("showtimes"),
                            rs.getString("poster"),
                            rs.getString("synopsis")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du film : " + e.getMessage());
        }
        return null;
    }

    public void addMovie(Movie movie) {
        String query = "INSERT INTO movie (id, title, genre, releaseDate, runningTime, price, showtimes, poster, synopsis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, movie.getId());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.setString(4, movie.getReleaseDate());
            stmt.setInt(5, movie.getRunningTime());
            stmt.setDouble(6, movie.getPrice());
            stmt.setString(7, movie.getShowtimes());
            stmt.setString(8, movie.getPoster());
            stmt.setString(9, movie.getSynopsis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du film : " + e.getMessage());
        }
    }

    public void updateMovie(Movie movie) {
        String query = "UPDATE movie SET title = ?, genre = ?, releaseDate = ?, runningTime = ?, price = ?, showtimes = ?, poster = ?, synopsis = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setString(3, movie.getReleaseDate());
            stmt.setInt(4, movie.getRunningTime());
            stmt.setDouble(5, movie.getPrice());
            stmt.setString(6, movie.getShowtimes());
            stmt.setString(7, movie.getPoster());
            stmt.setString(8, movie.getSynopsis());
            stmt.setInt(9, movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du film : " + e.getMessage());
        }
    }

    public void deleteMovie(int id) {
        String query = "DELETE FROM movie WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du film : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MovieService service = new MovieService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Lister les films");
            System.out.println("2. Ajouter un film");
            System.out.println("3. Modifier un film");
            System.out.println("4. Supprimer un film");
            System.out.println("5. Voir un film par ID");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    List<Movie> movies = service.getAllMovies();
                    for (Movie m : movies) {
                        System.out.println(m.getId() + " - " + m.getTitle());
                    }
                    break;

                case 2:
                    System.out.println("Ajouter un nouveau film :");
                    System.out.print("ID : ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Titre : ");
                    String title = scanner.nextLine();
                    System.out.print("Genre : ");
                    String genre = scanner.nextLine();
                    System.out.print("Date de sortie : ");
                    String releaseDate = scanner.nextLine();
                    System.out.print("Durée (minutes) : ");
                    int runningTime = Integer.parseInt(scanner.nextLine());
                    System.out.print("Prix : ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Horaires : ");
                    String showtimes = scanner.nextLine();
                    System.out.print("URL de l'affiche : ");
                    String poster = scanner.nextLine();
                    System.out.print("Synopsis : ");
                    String synopsis = scanner.nextLine();

                    Movie newMovie = new Movie(id, title, genre, releaseDate, runningTime, price, showtimes, poster, synopsis);
                    service.addMovie(newMovie);
                    System.out.println("Film ajouté !");
                    break;

                case 3:
                    System.out.print("ID du film à modifier : ");
                    int updateId = Integer.parseInt(scanner.nextLine());
                    Movie toUpdate = service.getMovieById(updateId);
                    if (toUpdate == null) {
                        System.out.println("Film non trouvé.");
                        break;
                    }
                    System.out.print("Nouveau titre (" + toUpdate.getTitle() + ") : ");
                    toUpdate.setTitle(scanner.nextLine());
                    System.out.print("Nouveau genre : ");
                    toUpdate.setGenre(scanner.nextLine());
                    System.out.print("Nouvelle date de sortie : ");
                    toUpdate.setReleaseDate(scanner.nextLine());
                    System.out.print("Nouvelle durée : ");
                    toUpdate.setRunningTime(Integer.parseInt(scanner.nextLine()));
                    System.out.print("Nouveau prix : ");
                    toUpdate.setPrice(Double.parseDouble(scanner.nextLine()));
                    System.out.print("Nouveaux horaires : ");
                    toUpdate.setShowtimes(scanner.nextLine());
                    System.out.print("Nouvelle URL d'affiche : ");
                    toUpdate.setPoster(scanner.nextLine());
                    System.out.print("Nouveau synopsis : ");
                    toUpdate.setSynopsis(scanner.nextLine());

                    service.updateMovie(toUpdate);
                    System.out.println("Film mis à jour !");
                    break;

                case 4:
                    System.out.print("ID du film à supprimer : ");
                    int deleteId = Integer.parseInt(scanner.nextLine());
                    service.deleteMovie(deleteId);
                    System.out.println("Film supprimé !");
                    break;

                case 5:
                    System.out.print("ID du film à consulter : ");
                    int searchId = Integer.parseInt(scanner.nextLine());
                    Movie found = service.getMovieById(searchId);
                    if (found != null) {
                        System.out.println("Titre : " + found.getTitle());
                        System.out.println("Genre : " + found.getGenre());
                        System.out.println("Date : " + found.getReleaseDate());
                        System.out.println("Durée : " + found.getRunningTime());
                        System.out.println("Prix : " + found.getPrice());
                        System.out.println("Horaires : " + found.getShowtimes());
                        System.out.println("Affiche : " + found.getPoster());
                        System.out.println("Synopsis : " + found.getSynopsis());
                    } else {
                        System.out.println("Film non trouvé.");
                    }
                    break;

                case 0:
                    System.out.println("Fermeture du programme.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}
