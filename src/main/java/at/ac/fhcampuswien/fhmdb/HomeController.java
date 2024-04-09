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

        loadMovies();
    }


    private void loadMovies() {
        Platform.runLater(() -> {
            observableMovies.setAll(MovieAPI.fetchAllMovies()); // Holt alle Filme ohne Filter

        });
    }
    private void applyFilterAndDisplayResults() {
        String query = searchField.getText().trim().toLowerCase();
        String selectedGenreName = genreComboBox.getSelectionModel().getSelectedItem().toString();
        Genre genre = "ALL MOVIES".equals(selectedGenreName) ? null : Genre.valueOf(selectedGenreName);


        List<Movie> filteredMovies = MovieAPI.fetchMovies(query, genre, null, null);
        Platform.runLater(() -> {
            observableMovies.setAll(filteredMovies);
            movieListView.refresh();
        });
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


    public void ascending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
    }

    public void descending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
    }

    public String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitleForTest().length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirector().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

}