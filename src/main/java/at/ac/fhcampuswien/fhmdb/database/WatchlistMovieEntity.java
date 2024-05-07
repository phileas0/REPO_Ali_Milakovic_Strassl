package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private MovieEntity movie;

    @DatabaseField
    private String apiId;

    public long getId() {
        return id;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public String getApiId() {
        return apiId;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
