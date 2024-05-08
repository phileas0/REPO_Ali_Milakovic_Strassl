package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Arrays;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = false, id = true)
    private String apiId;  // Set as primary key

    @DatabaseField
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

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public String getApiId() {
        return apiId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static MovieEntity convertMovieToMovieEntity(Movie movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(movie.getTitle());
        movieEntity.setDescription(movie.getDescription());
        movieEntity.setGenres(movie.getGenresString());
        movieEntity.setReleaseYear(movie.getReleaseYear());
        movieEntity.setRating(movie.getNumericRating());
        movieEntity.setApiId(movie.getId());
        return movieEntity;
    }

    public static Movie convertToMovie(MovieEntity movieEntity) {
        Movie movie = new Movie();
        movie.setId(movieEntity.getApiId());
        movie.setTitle(movieEntity.getTitle());
        movie.setDescription(movieEntity.getDescription());
        movie.setGenres(Arrays.asList(movieEntity.getGenres().split(", ")));
        movie.setReleaseYear(movieEntity.getReleaseYear());
        movie.setRating(Double.parseDouble(String.valueOf(movieEntity.getRating())));
        return movie;
    }
}