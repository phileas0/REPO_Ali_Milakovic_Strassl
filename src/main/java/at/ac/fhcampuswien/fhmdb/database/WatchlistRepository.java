package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Integer> watchlistDao;

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
    public void removeFromWatchlist(int id) throws SQLException {
        watchlistDao.deleteById(id);
    }
}
