package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
                throw new IOException("Unexpected code " + response);
            }
            String jsonData = response.body().string();
            Movie[] moviesArray = gson.fromJson(jsonData, Movie[].class);
            return List.of(moviesArray);
        } catch (IOException e) {
            System.err.println("Error fetching movies: " + e.getMessage());
            return new ArrayList<>(); // Return an empty list on error
        }
    }

    public static List<Movie> fetchAllMovies() {
        return fetchMovies(null, null, null, null);
    }
}
