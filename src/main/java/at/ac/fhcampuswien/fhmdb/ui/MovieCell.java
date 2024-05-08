package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.Interface.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
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

    private ClickEventHandler<Movie> onAddToWatchlist;
    private ClickEventHandler<Movie> onRemoveFromWatchlist;
    private boolean isWatchlistView;

    public MovieCell(WatchlistRepository watchlistRepo, ClickEventHandler<Movie> onAddToWatchlist, ClickEventHandler<Movie> onRemoveFromWatchlist, boolean isWatchlistView) {
        super();
        this.watchlistRepo = watchlistRepo;
        this.onAddToWatchlist = onAddToWatchlist;
        this.onRemoveFromWatchlist = onRemoveFromWatchlist;
        this.isWatchlistView = isWatchlistView;
        initializeCellComponents();
    }

    private void initializeCellComponents() {
        if (!isWatchlistView) {
            layout.getChildren().add(addToWatchlistButton);
        } else {
            layout.getChildren().add(removeFromWatchlistButton);
        }
        setupButtonActions();
    }

    private void setupButtonActions() {
        addToWatchlistButton.setOnAction(event -> {
            Movie item = getItem();
            if (item != null && onAddToWatchlist != null) {
                onAddToWatchlist.onClick(item);
            }
        });
        removeFromWatchlistButton.setOnAction(event -> {
            Movie item = getItem();
            if (item != null && onRemoveFromWatchlist != null) {
                onRemoveFromWatchlist.onClick(item);
                getListView().getItems().remove(item); // Remove the item from the ListView
                getListView().refresh();
            }
        });
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
            getStyleClass().remove("movie-cell");
        } else {
            // Debugging output to verify movie details
            System.out.println("Updating cell with: " + movie.getTitle());

            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            genre.setText(movie.getGenresString());
            rating.setText(movie.getRating());

            // Additional UI setups
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-white");
            rating.getStyleClass().add("text-cyan");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // Layout properties
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