package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.Interface.Observer;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.Interface.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.sorting.AscendingState;
import at.ac.fhcampuswien.fhmdb.sorting.DescendingState;
import at.ac.fhcampuswien.fhmdb.sorting.MovieSortManager;
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
import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;

public class HomeController implements Initializable, Observer{
    @FXML
    public JFXButton watchListButton;

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

    public List<Movie> allMovies = Movie.initializeMovies();

    final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private ConnectionSource connectionSource;
    private MovieSortManager sortManager = new MovieSortManager();

    MovieRepository movieRepository;

    private WatchlistRepository watchlistRepository;

    public enum WindowState {
        HOME, WATCHLIST
    }

    private WindowState windowState = WindowState.HOME;

    public HomeController() {
        System.out.println("HomeController instance created: " + this);
    }

    @Override
    public void update(String message) {
        Platform.runLater(() -> {
            showWatchlistAlert(message);
        });
    }

    public void switchToWatchlist() {
        if (windowState != WindowState.WATCHLIST) {
            windowState = WindowState.WATCHLIST;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist.fxml"));
                Scene scene = new Scene(loader.load(), 890, 620);
                Stage stage = (Stage) movieListView.getScene().getWindow();
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                stage.setScene(scene);
                WatchListController watchListController = loader.getController();
                watchListController.loadWatchlist(); // Ensure the watchlist is loaded
            } catch (IOException e) {
                e.printStackTrace(); // Consider a more user-friendly error handling
            }
        }
    }


    ClickEventHandler<Movie> addToWatchlistHandler = movie -> {
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
        watchListButton.setOnAction(actionEvent -> switchToWatchlist());
        cacheMoviesAtStartup();

        movieListView.setCellFactory(lv -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler, false));  // false for home screen
        movieRepository = MovieRepository.getInstance();
        watchlistRepository = WatchlistRepository.getInstance();

        try {
            // Beispiel für eine H2 In-Memory Datenbank
            String databaseUrl = "jdbc:h2:mem:fhmdb"; // Ersetze dies mit deiner tatsächlichen Datenbank-URL
            connectionSource = new JdbcConnectionSource(databaseUrl);

            // Initialisiere das Repository mit der Datenbankverbindung
            movieListView.setCellFactory(lv -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler, false));  // false for home screen
        } catch (SQLException e) {
            e.printStackTrace();  // Füge eine angemessene Fehlerbehandlung hinzu
        }


        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(lv -> new MovieCell(watchlistRepository, addToWatchlistHandler, removeFromWatchlistHandler, false));  // false for home screen

        searchBtn.setOnAction(actionEvent -> applyFilterAndDisplayResults());
        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                searchBtn.fire();
            }
        });

        /*
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
         */

        loadMovies();
    }


    private void loadMovies() {
        Platform.runLater(() -> {
            try {
                observableMovies.setAll(MovieAPI.fetchAllMovies()); // Holt alle Filme ohne Filter
                prepareAndPopulateFilters();
            } catch (Exception e) {
                // Lade Filme aus der Datenbank, wenn die API nicht erreichbar ist
                try {
                    List<MovieEntity> moviesFromDB = MovieRepository.findAll();
                    List<Movie> cachedMovies = moviesFromDB.stream().map(MovieEntity::convertToMovie).collect(Collectors.toList());
                    observableMovies.setAll(cachedMovies);
                    prepareAndPopulateFilters();
                } catch (SQLException sqlException) {
                    // Zeige Fehlermeldung im UI
                    sqlException.printStackTrace();
                }
            }
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
            sortManager.sortMovies(observableMovies);
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


    /*
    public void ascending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie1.getTitle().compareToIgnoreCase(movie2.getTitle()));
    }

    public void descending() {
        FXCollections.sort(observableMovies, (movie1, movie2) -> movie2.getTitle().compareToIgnoreCase(movie1.getTitle()));
    }
     */

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


    private void cacheMoviesAtStartup() {
        try {
            List<Movie> moviesFromAPI = MovieAPI.fetchAllMovies();
            MovieRepository movieRepository = MovieRepository.getInstance();
            for (Movie movie : moviesFromAPI) {
                MovieEntity movieEntity = MovieEntity.convertMovieToMovieEntity(movie);
                movieRepository.createOrUpdate(movieEntity);
            }
        } catch (SQLException e) {
            // Handle Exception, vielleicht eine Fehlermeldung im UI anzeigen
            e.printStackTrace();
        }
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWatchlistAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Watchlist Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Show the alert and wait for the user to close it
    }

    public void onSortAscendingClicked() {
        sortManager.setSortState(new AscendingState());
        sortManager.sortMovies(observableMovies);
    }

    public void onSortDescendingClicked() {
        sortManager.setSortState(new DescendingState());
        sortManager.sortMovies(observableMovies);
    }
}