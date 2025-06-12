package model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String releaseDate;
    private int runningTime;
    private double price;
    private String showtimes;
    private String poster;
    private String synopsis;

    public Movie(int id, String title, String genre, String releaseDate, int runningTime, double price,
                 String showtimes, String poster, String synopsis) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.runningTime = runningTime;
        this.price = price;
        this.showtimes = showtimes;
        this.poster = poster;
        this.synopsis = synopsis;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public double getPrice() {
        return price;
    }

    public String getShowtimes() {
        return showtimes;
    }

    public String getPoster() {
        return poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setShowtimes(String showtimes) {
        this.showtimes = showtimes;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return title;
    }
}
