package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    // TODO add more properties here
    private List<Genre> genres;
    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres(){ return genres;}
    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        movies.add(new Movie ("Poor Things","Porn Movie with Emma Stone and Mark Ruffalo", Arrays.asList(Genre.DRAMA, Genre.ADVENTURE, Genre.FANTASY, Genre.ROMANCE)));

        return movies;
    }
}
