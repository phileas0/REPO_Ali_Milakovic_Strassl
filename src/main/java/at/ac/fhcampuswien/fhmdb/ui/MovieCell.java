package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;

import static at.ac.fhcampuswien.fhmdb.database.MovieEntity.convertMovieToMovieEntity;

public class MovieCell extends ListCell<Movie> {

    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final VBox layout = new VBox(title, detail, genre);
    private final VBox secondLayout = new VBox();
    private WatchlistRepository watchlistRepo; // Repository-Instanz
    private Button addToWatchlistButton = new Button("Add to Watchlist");
    private Button removeFromWatchlistButton = new Button("Remove");

    public MovieCell(WatchlistRepository watchlistRepo) {
        super();
        initializeCellComponents();
        this.watchlistRepo = watchlistRepo;
        layout.getChildren().addAll(addToWatchlistButton, removeFromWatchlistButton);
        addToWatchlistButton.setOnAction(event -> addToWatchlist(getItem()));
        removeFromWatchlistButton.setOnAction(event -> removeFromWatchlist(getItem()));
    }

    private void initializeCellComponents() {
        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setOnAction(event -> addToWatchlist(getItem()));
        // similar setup for other components
    }

    private void addToWatchlist(Movie movie) {
        try {
            WatchlistMovieEntity watchlistMovie = new WatchlistMovieEntity();
            // Stelle sicher, dass du die `MovieEntity` von `Movie` umwandelst
            watchlistMovie.setMovie(convertMovieToMovieEntity(movie));
            watchlistRepo.addToWatchlist(watchlistMovie);
        } catch (SQLException e) {
            e.printStackTrace(); // Fehlerbehandlung
        }
    }

    private void removeFromWatchlist(Movie movie) {
        try {
            // Hier musst du die entsprechende Logik implementieren, um eine WatchlistMovieEntity zu löschen
            watchlistRepo.removeFromWatchlist(movie.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            // Fehlerbehandlung
        }
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
            getStyleClass().remove("movie-cell");
        } else {

            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText(movie.getGenresString());
            //releaseYear.setText(movie.getStringReleaseYear());
            rating.setText(movie.getRating());


            // color scheme
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-white");
            //releaseYear.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-cyan");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            if (!layout.getChildren().contains(rating)) {
                layout.getChildren().addAll(rating);
            }
            setGraphic(layout);
        }
    }
}

