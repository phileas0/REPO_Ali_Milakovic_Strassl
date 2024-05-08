package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.Interface.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.database.DatabaseManager.watchlistDao;

public class WatchListController implements Initializable {

    public VBox watchlistVBox;

    @FXML
    public ListView<Movie> watchlistListView;// Ensure this is set to handle Movie objects

    public Button switchToHomeButton;

    private WatchlistRepository watchlistRepository;
    private ObservableList<Movie> observableWatchlistMovies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watchlistRepository = new WatchlistRepository();
        loadWatchlist();

        ClickEventHandler<Movie> addToWatchlistHandler = null;

        watchlistListView.setCellFactory(lv -> new MovieCell(
                watchlistRepository,
                addToWatchlistHandler,  // This can be null if adding is not supported in this view
                onRemoveFromWatchlist,
                true  // 'true' because it's the watchlist screen, showing only the "Remove" button
        ));
    }

    private ClickEventHandler<Movie> onRemoveFromWatchlist = movie -> {
        if (movie != null) {
            try {
                watchlistRepository.removeFromWatchlist(movie.getId());
            } catch (SQLException e) {
                e.printStackTrace(); // Better to handle exceptions more gracefully
            }
        }
    };

    public void loadWatchlist() {
        try {
            List<WatchlistMovieEntity> watchlistEntries = watchlistRepository.findAll();
            List<Movie> watchlistMovies = watchlistEntries.stream()
                    .map(WatchlistMovieEntity::getMovie)
                    .filter(Objects::nonNull)  // Ensure movieEntity is not null before conversion
                    .map(MovieEntity::convertToMovie)
                    .collect(Collectors.toList());
            observableWatchlistMovies.setAll(watchlistMovies);
            watchlistListView.setItems(observableWatchlistMovies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToWatchlist(WatchlistMovieEntity watchlistMovie) throws SQLException {
        watchlistDao.createIfNotExists(watchlistMovie);
    }

    public void switchToHome(ActionEvent actionEvent) {
        try {
            // Load the home view FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent homeView = loader.load();

            // Get the current stage from the event source which is a component in the current scene
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the scene to the home view
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, you can add more sophisticated error handling and user feedback here.
        }
    }
}