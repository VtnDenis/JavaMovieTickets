package model;

public class Screen {
    private int screenId;
    private int movieId;
    private int capacity;

    public Screen(int screenId, int movieId, int capacity) {
        this.screenId = screenId;
        this.movieId = movieId;
        this.capacity = capacity;
    }

    public int getScreenId() {
        return screenId;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getCapacity() {
        return capacity;
    }
}
