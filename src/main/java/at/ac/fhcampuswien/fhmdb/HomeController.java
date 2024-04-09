package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    private JFXListView<Movie> movieListView;
    private MovieAPI movieApiService = new MovieAPI();
    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genreComboBox.getItems().addAll("ALL MOVIES");
        for (Genre genre : Genre.values()) {
            genreComboBox.getItems().add(genre.name());
        }
        genreComboBox.getSelectionModel().select("ALL MOVIES");

        loadMovies();
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        searchBtn.setOnAction(actionEvent -> applyFilterAndDisplayResults());
        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchBtn.fire();
            }
        });

        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                descending();
                sortBtn.setText("Sort (desc)");
            } else {
                ascending();
                sortBtn.setText("Sort (asc)");
            }
            movieListView.refresh();
        });
    }

    private void loadMovies() {
        Platform.runLater(() -> {
            observableMovies.setAll(MovieAPI.fetchAllMovies());
        });
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public List<Movie> movieFilter(String query, Genre selectedGenre) {
        query = query.trim().toLowerCase();

        // Bereite die Liste für gefilterte Filme vor
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean matchesQuery = query.isEmpty() ||
                    movie.getTitle().toLowerCase().contains(query) ||
                    movie.getDescription().toLowerCase().contains(query);

            boolean matchesGenre = selectedGenre == null ||
                    movie.getGenres().contains(selectedGenre);

            if (matchesQuery && matchesGenre) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }

    public void applyFilterAndDisplayResults() {
        String query = searchField.getText().trim().toLowerCase(); // Lese den Suchbegriff aus der UI
        Genre selectedGenre = null; // Initialisiere selectedGenre
        String selectedGenreName = (String) genreComboBox.getSelectionModel().getSelectedItem();

        if (!"ALL MOVIES".equals(selectedGenreName)) {
            try {
                selectedGenre = Genre.valueOf(selectedGenreName.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Fehlerbehandlung, falls das Genre nicht gefunden wird
            }
        }

        List<Movie> filteredMovies = movieFilter(query, selectedGenre);
        observableMovies.setAll(filteredMovies); // Aktualisiere observableMovies mit den gefilterten Filmen
        movieListView.refresh(); // Aktualisiere die ListView
    }

    public void ascending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
    }

    public void descending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream().mapToInt(movie -> movie.getTitle().length()).max().orElse(0);
    }

}