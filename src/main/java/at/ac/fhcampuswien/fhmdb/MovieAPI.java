package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.HomeController.showAlert;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static String buildURL(String query, String genre, String releaseYear, String ratingFrom) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        if (query != null && !query.isEmpty()) urlBuilder.addQueryParameter("query", query);

        // Only add the genre parameter if it is not "ALL MOVIES"
        if (genre != null && !genre.isEmpty() && !genre.equals("ALL MOVIES")) {
            urlBuilder.addQueryParameter("genre", genre);
        }

        // Only add the releaseYear parameter if it is not "ALL YEARS"
        if (releaseYear != null && !releaseYear.isEmpty() && !releaseYear.equals("ALL YEARS")) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }

        // Only add the ratingFrom parameter if it is not "ALL RATINGS"
        if (ratingFrom != null && !ratingFrom.isEmpty() && !ratingFrom.equals("ALL RATINGS")) {
            urlBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }

        return urlBuilder.build().toString();
    }

    public static List<Movie> fetchMovies(String query, String genre, String releaseYear, String ratingFrom) {
        Request request = new Request.Builder()
                .url(buildURL(query, genre, releaseYear, ratingFrom))
                .addHeader("User-Agent", "http.agent")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                try {
                    throw new MovieAPIException("Failed to fetch movies: " + response);
                } catch (MovieAPIException e) {
                    throw new RuntimeException(e);
                }
            }
            String jsonData = response.body().string();
            Movie[] moviesArray = gson.fromJson(jsonData, Movie[].class);
            return List.of(moviesArray);
        } catch (IOException e) {
            try {
                throw new MovieAPIException("Network error while fetching movies: " + e.getMessage(), e);
            } catch (MovieAPIException ex) {
                Platform.runLater(() -> showAlert("Network Error", "Unable to fetch movies due to a network error. Using cached data."));
                return functionWhenNoInternet();
            }
        }
    }




    private static List<Movie> functionWhenNoInternet() {
        try {
            // Hier initialisieren wir das Repository, um aus der Datenbank zu lesen
            MovieRepository movieRepository = MovieRepository.getInstance();
            List<MovieEntity> movieEntities = movieRepository.findAll();
            return movieEntities.stream().map(MovieEntity::convertToMovie).collect(Collectors.toList());
        } catch (SQLException e) {
            System.err.println("Error accessing local database: " + e.getMessage());
            return new ArrayList<>(); // Return an empty list on database access error
        }
    }

    public static List<Movie> fetchAllMovies() {
        return fetchMovies(null, null, null, null);
    }
}