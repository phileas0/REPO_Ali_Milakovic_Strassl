package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.Interface.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.WatchListController;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    public ListView watchlistListView;
    @FXML
    private ComboBox<String> viewSelector;
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    private JFXListView<Movie> movieListView;
    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML JFXComboBox ratingComboBox;

    @FXML
    public JFXButton sortBtn;
    public JFXButton watchlistBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private ConnectionSource connectionSource;

    private MovieRepository movieRepository;

    private WatchlistRepository watchlistRepository;

    public enum WindowState {
        HOME, WATCHLIST
    }

    private WindowState windowState = WindowState.HOME;

    public HomeController() {
    }

    public void switchToWatchlist() {
        if (windowState != WindowState.WATCHLIST) {
            windowState = WindowState.WATCHLIST;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) movieListView.getScene().getWindow();
                stage.setScene(scene);
                WatchListController watchListController = loader.getController();
                watchListController.loadWatchlist(); // Ensure the watchlist is loaded
            } catch (IOException e) {
                e.printStackTrace(); // Consider a more user-friendly error handling
            }
        }
    }


    private ClickEventHandler<Movie> addToWatchlistHandler = movie -> {
        try {
            MovieEntity movieEntity = movieRepository.findByApiId(movie.getId());
            if (movieEntity == null) {
                movieEntity = MovieEntity.convertMovieToMovieEntity(movie);
                movieRepository.createOrUpdate(movieEntity);  // Make sure to implement this method in MovieRepository
            }
            WatchlistMovieEntity watchlistMovie = new WatchlistMovieEntity();
            watchlistMovie.setMovie(movieEntity);
            watchlistMovie.setApiId(movie.getId());
            watchlistRepository.addToWatchlist(watchlistMovie);
        } catch (SQLException e) {
            e.printStackTrace();  // Proper error handling should be added
        }
    };

    private ClickEventHandler<Movie> removeFromWatchlistHandler = movie -> {
        if (movie != null) {
            try {
                String apiId = movie.getId();  // Annehmen, dass movie.getId() die API ID zurückgibt
                watchlistRepository.removeFromWatchlist(apiId);
            } catch (SQLException e) {
                e.printStackTrace();  // Fehlerbehandlung
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        windowState = WindowState.HOME;

        watchlistBtn.setOnAction(actionEvent -> switchToWatchlist());

        movieListView.setCellFactory(lv -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler));
        movieRepository = new MovieRepository(); // No parameters
        watchlistRepository = new WatchlistRepository(); // No parameters

        try {
            // Beispiel für eine H2 In-Memory Datenbank
            String databaseUrl = "jdbc:h2:mem:fhmdb"; // Ersetze dies mit deiner tatsächlichen Datenbank-URL
            connectionSource = new JdbcConnectionSource(databaseUrl);

            // Initialisiere das Repository mit der Datenbankverbindung
            watchlistRepository = new WatchlistRepository();
            movieListView.setCellFactory(lv -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler));
        } catch (SQLException e) {
            e.printStackTrace();  // Füge eine angemessene Fehlerbehandlung hinzu
        }


        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler));

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


    public void loadMovies() {
        Platform.runLater(() -> {
            observableMovies.setAll(MovieAPI.fetchAllMovies()); // Holt alle Filme ohne Filter
            prepareAndPopulateFilters();
        });

    }

    public void prepareAndPopulateFilters() {
        List<Movie> allMovies = MovieAPI.fetchAllMovies(); // Assuming this method returns all movies

        // Extract unique genres
        Set<String> uniqueGenres = new HashSet<>();
        for (Movie movie : allMovies) {
            uniqueGenres.addAll(movie.getGenres());
        }
        List<String> sortedGenres = new ArrayList<>(uniqueGenres);
        Collections.sort(sortedGenres); // Sort, assuming Genre enum has a natural order or implement Comparator

        // Extract unique release years
        Set<Integer> uniqueYears = new HashSet<>();
        for (Movie movie : allMovies) {
            uniqueYears.add(movie.getReleaseYear());
        }
        List<Integer> sortedYears = new ArrayList<>(uniqueYears);
        Collections.sort(sortedYears); // Sort years

        // Assuming ratings are integers for simplicity; adjust logic as needed for your data
        int minRating = (int) allMovies.stream().mapToDouble(Movie::getNumericRating).min().orElse(1);
        int maxRating = (int) allMovies.stream().mapToDouble(Movie::getNumericRating).max().orElse(10);

        // Populate ComboBoxes
        Platform.runLater(() -> {
            genreComboBox.getItems().clear();
            genreComboBox.getItems().add("ALL GENRES");
            genreComboBox.getItems().addAll(sortedGenres);
            genreComboBox.getSelectionModel().selectFirst(); // Select "ALL GENRES" by default

            releaseYearComboBox.getItems().clear();
            releaseYearComboBox.getItems().addAll("ALL YEARS");
            sortedYears.forEach(year -> releaseYearComboBox.getItems().add(year.toString()));
            releaseYearComboBox.getSelectionModel().selectFirst(); // Select "ALL YEARS" by default

            ratingComboBox.getItems().clear();
            ratingComboBox.getItems().addAll("ALL RATINGS");
            for (double rating = minRating; rating <= maxRating; rating++) {
                ratingComboBox.getItems().add(String.format(Locale.ROOT, "%.1f", rating)); // Assuming ratings are decimal numbers
            }
            ratingComboBox.getSelectionModel().selectFirst(); // Select "ALL RATINGS" by default
        });
    }

    public void applyFilterAndDisplayResults() {
        String query = searchField.getText().trim().toLowerCase();
        String genre = genreComboBox.getSelectionModel().getSelectedItem().toString();
        // Avoid sending "ALL MOVIES" to the server
        if ("ALL GENRES".equals(genre)) {
            genre = null;
        }

        String yearFilter = null;
        Object yearSelection = releaseYearComboBox.getSelectionModel().getSelectedItem();
        if (yearSelection instanceof Integer) {
            yearFilter = String.valueOf(yearSelection);
        } else if (yearSelection instanceof String && !((String) yearSelection).equals("ALL YEARS")) {
            yearFilter = (String) yearSelection;
        }
        if ("ALL YEARS".equals(yearFilter)) {
            yearFilter = null;
        }

        String ratingFilter = null;
        Object ratingSelection = ratingComboBox.getSelectionModel().getSelectedItem();
        if (ratingSelection instanceof Number) {
            ratingFilter = String.format(Locale.ROOT, "%.1f", ((Number) ratingSelection).doubleValue());
        } else if (ratingSelection instanceof String && !((String) ratingSelection).equals("ALL RATINGS")) {
            ratingFilter = (String) ratingSelection;
        }
        if ("ALL RATINGS".equals(ratingFilter)) {
            ratingFilter = null;
        }

        // Proceed with fetching movies using the adjusted parameters
        List<Movie> filteredMovies = MovieAPI.fetchMovies(query, genre, yearFilter, ratingFilter);
        Platform.runLater(() -> {
            observableMovies.setAll(filteredMovies);
            movieListView.refresh();
        });
    }

    public List<Movie> movieFilter(String query, String selectedGenre) {
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

    public void handleViewSelection() {
        String selectedView = viewSelector.getSelectionModel().getSelectedItem();
        if ("Watchlist".equals(selectedView)) {
            switchToWatchlist();
        } else if ("Alle Filme".equals(selectedView)) {
            loadMovies();  // Methode, um alle Filme zu laden
        }
    }
}