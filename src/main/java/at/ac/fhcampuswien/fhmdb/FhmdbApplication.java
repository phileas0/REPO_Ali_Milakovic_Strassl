package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FhmdbApplication extends Application {
    private static DatabaseManager databaseManager;

    @Override
    public void start(Stage stage) throws IOException {
        initializeDatabase();
        WatchlistRepository watchlistRepo = WatchlistRepository.getInstance(); // Observer
        HomeController homeController = new HomeController();
        watchlistRepo.attach(homeController);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 890, 620);
        stage.setScene(scene);
        stage.show();
    }

    private void initializeDatabase() {
        databaseManager = DatabaseManager.getDatabaseManager();
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
        super.stop();
    }
}