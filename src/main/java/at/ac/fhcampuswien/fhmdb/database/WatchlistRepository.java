package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository(ConnectionSource connectionSource) throws SQLException {
        watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
    }

    // Alle Einträge der Watchlist abrufen
    public List<WatchlistMovieEntity> findAll() throws SQLException {
        return watchlistDao.queryForAll();
    }

    // Einen Film zur Watchlist hinzufügen
    public void addToWatchlist(WatchlistMovieEntity watchlistMovie) throws SQLException {
        watchlistDao.create(watchlistMovie);
    }

    // Einen Eintrag aus der Watchlist löschen
    public void removeFromWatchlist(String apiId) throws SQLException {
        // Annahme: Du hast bereits eine Methode, die per ID löscht, die aber angepasst werden muss
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
