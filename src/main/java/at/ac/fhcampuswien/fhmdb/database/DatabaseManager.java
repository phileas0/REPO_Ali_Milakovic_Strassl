package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.WatchListController;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:fhmdb";
    private ConnectionSource connectionSource;

    public DatabaseManager() {
        try {
            // Create a connection source to our database
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            // Create tables
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (Exception e) {
            System.out.println("Error creating database connection: " + e.getMessage());
        }
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    // Implement cleanup resources method
    public void closeConnection() throws Exception {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (IOException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
