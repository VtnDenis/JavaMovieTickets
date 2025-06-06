package model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String releaseDate;
    private int runningTime;
    private double price;
    private String showtimes;

    public Movie(int id, String title, String genre, String releaseDate, int runningTime, double price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.runningTime = runningTime;
        this.price = price;
        this.showtimes = "";
    }

    public Movie(int id, String title, String genre, String releaseDate, int runningTime, double price, String showtimes) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.runningTime = runningTime;
        this.price = price;
        this.showtimes = showtimes;
    }

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

    public String getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(String showtimes) {
        this.showtimes = showtimes;
    }

    @Override
    public String toString() {
        return title;
    }
}
