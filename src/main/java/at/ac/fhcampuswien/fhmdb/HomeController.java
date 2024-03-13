package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        //genreComboBox.setPromptText("ALL MOVIES");
        genreComboBox.getItems().add("ALL MOVIES");
        genreComboBox.getItems().addAll(Arrays.stream(Genre.values()).map(Enum::name).collect(Collectors.toList()));
        genreComboBox.getSelectionModel().select("ALL MOVIES");

        // either set event handlers in the fxml file (onAction) or add them here
        searchBtn.setOnAction(actionEvent -> applyFilterAndDisplayResults());

        // Sort button example:
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

    private void ascending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
    }

    private void descending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
    }
}