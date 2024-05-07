package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseManager {
    public static final String DB_URL = "jdbc:h2:./db/moviesdb";
    public static final String user = "user";
    public static final String password = "123";
    private static ConnectionSource connectionSource;
    private Dao<MovieEntity, Long> movieDao;
    public static Dao<WatchlistMovieEntity, Long> watchlistDao;


    private static DatabaseManager instance;
    private DatabaseManager(){
        try {
            createConnectSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static DatabaseManager getDatabaseManager(){
        if(instance == null) instance = new DatabaseManager();
        return instance;
    }

    public static void createConnectSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DB_URL, user, password);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        if (watchlistDao == null) {
            try {
                watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            } catch (SQLException e) {
                System.err.println("Error initializing DAO: " + e.getMessage());
            }
        }
        return watchlistDao;
    }

    // Method to close the database connection
    public void closeConnection() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
