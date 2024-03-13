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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
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

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Arrays.stream(Genre.values()).map(Enum::name).collect(Collectors.toList()));

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here
        searchBtn.setOnAction(actionEvent -> movieFilter());
        //genreComboBox.setOnAction(actionEvent -> movieFilter());

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if (sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
                descending();
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending
                ascending();
                sortBtn.setText("Sort (asc)");
            }
            movieListView.refresh();
        });
    }

    private void movieFilter() {
        // Den Suchbegriff aus dem Textfeld auslesen und in Kleinbuchstaben umwandeln.
        String query = searchField.getText().trim().toLowerCase();

        // Den Namen des ausgewählten Genres aus der ComboBox auslesen und in ein Genre-Objekt umwandeln.
        Genre selectedGenre = null;
        String selectedGenreName = (String) genreComboBox.getSelectionModel().getSelectedItem();
        if (selectedGenreName != null) {
            try {
                selectedGenre = Genre.valueOf(selectedGenreName.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Fehlerbehandlung, falls das Genre nicht gefunden wird.
            }
        }

        // Vorhandene Filme in observableMovies löschen.
        observableMovies.clear();

        // Durchlaufen aller Filme und Prüfen der Filterkriterien.
        for (Movie movie : allMovies) {
            boolean matchesQuery = query.isEmpty() ||
                    movie.getTitle().toLowerCase().contains(query) ||
                    movie.getDescription().toLowerCase().contains(query);

            boolean matchesGenre = selectedGenre == null ||
                    movie.getGenres().contains(selectedGenre);

            // Wenn ein Film beiden Kriterien entspricht, wird er der observableMovies-Liste hinzugefügt.
            if (matchesQuery && matchesGenre) {
                observableMovies.add(movie);
            }
        }

        // Die ListView aktualisieren.
        movieListView.refresh();
    }

    private void ascending(){
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
    }

    private void descending(){
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
    }


}