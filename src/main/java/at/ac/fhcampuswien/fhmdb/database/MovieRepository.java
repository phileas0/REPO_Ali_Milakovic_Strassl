package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Integer> movieDao;

    public MovieRepository(ConnectionSource connectionSource) throws SQLException {
        movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
    }

    // Lesen aller Filme
    public List<MovieEntity> findAll() throws SQLException {
        return movieDao.queryForAll();
    }

    // Einen Film hinzufügen
    public void addMovie(MovieEntity movie) throws SQLException {
        movieDao.create(movie);
    }

    // Einen Film nach ID löschen
    public void deleteMovie(int id) throws SQLException {
        movieDao.deleteById(id);
    }

    // Einen Film aktualisieren
    public void updateMovie(MovieEntity movie) throws SQLException {
        movieDao.update(movie);
    }

    // Einen Film nach ID suchen
    public MovieEntity findById(int id) throws SQLException {
        return movieDao.queryForId(id);
    }
}
