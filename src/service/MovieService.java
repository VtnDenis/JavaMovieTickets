package service;

import model.Movie;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private static final String MOVIE_FILE_PATH = "movies.txt";

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides et commentaires
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        String genre = parts[2];
                        String releaseDate = parts[3];
                        int runningTime = Integer.parseInt(parts[4]);
                        double price = Double.parseDouble(parts[5]);

                        Movie movie = new Movie(id, title, genre, releaseDate, runningTime, price);
                        movies.add(movie);
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing numeric value in line: " + line);
                    }
                } else {
                    System.out.println("Invalid format in line: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading movies file: " + e.getMessage());
        }
        return movies;
    }

    public Movie getMovieById(int id) {
        List<Movie> movies = getAllMovies();
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }

    public void saveMovies(List<Movie> movies) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIE_FILE_PATH));
            for (Movie movie : movies) {
                writer.write(movie.getId() + "," + movie.getTitle() + "," +
                             movie.getGenre() + "," + movie.getReleaseDate() + "," +
                             movie.getRunningTime() + "," + movie.getPrice());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving movies");
        }
    }

    public void addMovie(Movie movie) {
        List<Movie> movies = getAllMovies();
        movies.add(movie);
        saveMovies(movies);
    }

    public void updateMovie(Movie updatedMovie) {
        List<Movie> movies = getAllMovies();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == updatedMovie.getId()) {
                movies.set(i, updatedMovie);
                break;
            }
        }
        saveMovies(movies);
    }

    public void deleteMovie(int id) {
        List<Movie> movies = getAllMovies();
        movies.removeIf(movie -> movie.getId() == id);
        saveMovies(movies);
    }
}
