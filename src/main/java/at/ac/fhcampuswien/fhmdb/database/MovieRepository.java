package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class MovieRepository {
    private Dao<MovieEntity, Long> movieDao;

    public MovieRepository() {
        this.movieDao = DatabaseManager.getDatabaseManager().getMovieDao();
    }

    public void createOrUpdate(MovieEntity movieEntity) throws SQLException {
        MovieEntity existingMovie = findByApiId(movieEntity.getApiId());
        if (existingMovie == null) {
            movieDao.create(movieEntity);
        } else {
            movieEntity.setId(existingMovie.getId()); // Assuming 'id' is your generated or manually set ID
            movieDao.update(movieEntity);
        }
    }

    // Einen Film nach API ID löschen
    public void deleteMovieByApiId(String apiId) throws SQLException {
        DeleteBuilder<MovieEntity, Long> deleteBuilder = movieDao.deleteBuilder();
        deleteBuilder.where().eq("apiId", apiId);
        deleteBuilder.delete();
    }

    // Einen Film nach API ID suchen
    public MovieEntity findByApiId(String apiId) throws SQLException {
        QueryBuilder<MovieEntity, Long> queryBuilder = movieDao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);
        return movieDao.queryForFirst(queryBuilder.prepare());
    }
}
