package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.HomeController;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository() {
        this.watchlistDao = DatabaseManager.getDatabaseManager().getWatchlistDao();
    }

    // Alle Einträge der Watchlist abrufen
    public List<WatchlistMovieEntity> findAll() throws SQLException {
        return watchlistDao.queryForAll();
    }

    // Einen Film zur Watchlist hinzufügen
    public void addToWatchlist(WatchlistMovieEntity watchlistMovie) throws SQLException {
        try {
            if(!isMovieInWatchlist(watchlistMovie.getApiId())) {
                watchlistDao.createIfNotExists(watchlistMovie);
                System.out.println("Added to watchlist: " + watchlistMovie.getMovie().getTitle());
            } else System.out.println("Movie already in watchlist");
        } catch (SQLException e) {
            System.err.println("Error adding to watchlist: " + e.getMessage());
            throw e;
        }
    }

    public boolean isMovieInWatchlist(String apiId) throws SQLException {
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
        queryBuilder.where().eq("apiId", apiId);
        return queryBuilder.queryForFirst() != null;
    }

    // Einen Eintrag aus der Watchlist löschen
    public void removeFromWatchlist(String apiId) throws SQLException {
        DeleteBuilder<WatchlistMovieEntity, Long> deleteBuilder = watchlistDao.deleteBuilder();
        deleteBuilder.where().eq("apiId", apiId);
        int deletedRows = deleteBuilder.delete();
        if (deletedRows > 0) {
            System.out.println(deletedRows + " rows deleted successfully.");
        } else {
            System.out.println("No rows deleted. Entity not found with API ID: " + apiId);
        }
    }
}