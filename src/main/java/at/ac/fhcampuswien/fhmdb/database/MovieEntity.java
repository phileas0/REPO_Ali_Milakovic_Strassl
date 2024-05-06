package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField
    private String description;

    @DatabaseField
    private String genres;

    @DatabaseField
    private int releaseYear;

    @DatabaseField
    private double rating;

    public long getID() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Setter for genres
    public void setGenres(String genres) {
        this.genres = genres;
    }

    // Setter for release year
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    // Setter for rating
    public void setRating(double rating) {
        this.rating = rating;
    }

    public static MovieEntity convertMovieToMovieEntity(Movie movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(movie.getTitle());
        movieEntity.setDescription(movie.getDescription());
        movieEntity.setGenres(movie.getGenresString());
        movieEntity.setReleaseYear(movie.getReleaseYear());
        movieEntity.setRating(movie.getNumericRating());

        return movieEntity;
    }

}