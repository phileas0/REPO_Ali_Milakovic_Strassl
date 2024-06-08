package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.Interface.Observable;
import at.ac.fhcampuswien.fhmdb.Interface.Observer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {
    private static WatchlistRepository instance;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;
    private List<Observer> observers = new ArrayList<>();

    private WatchlistRepository() {
        this.watchlistDao = DatabaseManager.getDatabaseManager().getWatchlistDao();
    }

    public static WatchlistRepository getInstance() {
        if (instance == null) {
            synchronized (WatchlistRepository.class) { // Thread-safe singleton
                if (instance == null) {
                    instance = new WatchlistRepository();
                }
            }
        }
        return instance;
    }

    // Alle Einträge der Watchlist abrufen
    public List<WatchlistMovieEntity> findAll() throws SQLException {
        return watchlistDao.queryForAll();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Einen Film zur Watchlist hinzufügen
    public void addToWatchlist(WatchlistMovieEntity watchlistMovie) throws SQLException {
        try {
            if(!isMovieInWatchlist(watchlistMovie.getApiId())) {
                watchlistDao.createIfNotExists(watchlistMovie);
                notifyObservers("Added to watchlist: " + watchlistMovie.getMovie().getTitle());
            } else notifyObservers("Movie already in watchlist");
        } catch (SQLException e) {
            System.out.println("Error adding to watchlist: " + e.getMessage());
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