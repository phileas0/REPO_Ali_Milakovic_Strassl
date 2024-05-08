package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
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
            try {
                createConnectSource();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            try {
                createTables();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e){
            try {
                throw new DatabaseException("Error initializing database: " + e.getMessage(), e);
            } catch (DatabaseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static DatabaseManager getDatabaseManager(){
        if(instance == null) instance = new DatabaseManager();
        return instance;
    }

    public static void createConnectSource() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, user, password);
        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to database: " + e.getMessage(), e);
        }
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private static void createTables() throws DatabaseException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Error creating tables: " + e.getMessage(), e);
        }
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        if (watchlistDao == null) {
            try {
                watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            } catch (SQLException e) {
                try {
                    throw new DatabaseException("Error initializing DAO: " + e.getMessage(), e);
                } catch (DatabaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return watchlistDao;
    }

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
