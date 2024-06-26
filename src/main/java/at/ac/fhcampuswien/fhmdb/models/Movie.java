package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String id;
    private String title;
    private String description;
    private List<String> genres;
    private int releaseYear;
    private double rating;
    private String imgUrl;
    private List<String> mainCast;
    private List<String> writers;
    private List<String> director;
    private int lengthInMinutes;

    public Movie() {
    }


    public Movie(String id, String title, String description, List<String> genres, int releaseYear, double rating, String imgUrl, List<String> mainCast, List<String> writers, List<String> director, int lengthInMinutes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.imgUrl = imgUrl;
        this.mainCast = mainCast;
        this.writers = writers;
        this.director = director;
        this.lengthInMinutes = lengthInMinutes;
    }

    public String getTitle() {
        // Überprüfen, ob das Release-Jahr bereits im Titel enthalten ist
        if (!title.contains("(" + releaseYear + ")")) {
            return title + " (" + releaseYear + ")";
        }
        return title;
    }

    public String getTitleForTest() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenresString() {
        return String.join(", ", genres);
    }

    public List<String> getGenres() {
        return genres;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public String getRating() {
        return String.valueOf(rating) + " / 10";
    }

    public double getNumericRating() {
        return rating;
    }

    public List<String> getDirector() {
        return director;
    }
    public List<String> getWriters() {
        return writers;
    }
    public List<String> getMainCast() {
        return mainCast;
    }

    public String getId() {
        return id;
    }

    // Setter für ID, falls benötigt
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();

        return movies;
    }

    @Override
    public String toString() {
        return getTitle() + " - " + getDescription();
    }
}