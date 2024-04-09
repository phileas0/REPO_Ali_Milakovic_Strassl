package at.ac.fhcampuswien.fhmdb;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp(){
        homeController = new HomeController();
    }

    @Test
    void test_Filter_By_Title_Returns_True_If_Movie_Title_Contains_Query() {
        // Angenommen, "Inception" ist Teil des Titels eines Films in allMovies
        String query = "inception";
        List<Movie> result = homeController.movieFilter(query.toLowerCase(), null);

        // Sicherstellen, dass alle Filme in 'result' den Suchbegriff im Titel enthalten
        assertTrue(result.stream().allMatch(movie ->
                        movie.getTitle().toLowerCase().contains(query)),
                "Alle Filme sollten 'inception' im Titel enthalten.");
    }

    @Test
    void test_Filter_By_Genre_Returns_True_If_Selected_Genre_Is_Displayed() {
        String selectedGenre = "Science Fiction";
        List<Movie> result = homeController.movieFilter("", selectedGenre);

        assertTrue(result.stream().allMatch(movie ->
                        movie.getGenres().contains(selectedGenre)),
                "Alle Filme sollten dem Genre SCIENCE_FICTION zugeordnet sein.");
    }

    @Test
    void test_Filter_By_Title_And_Genre() {
        String query = "spider";
        String selectedGenre = "Animation";
        List<Movie> result = homeController.movieFilter(query.toLowerCase(), selectedGenre);

        assertTrue(result.stream().allMatch(movie ->
                        movie.getTitle().toLowerCase().contains(query) &&
                                movie.getGenres().contains(selectedGenre)),
                "Alle Filme sollten 'spider' im Titel und dem Genre ANIMATION zugeordnet sein.");
    }

    @Test
    void test_Sort_Ascending() {
        homeController.ascending();
        List<Movie> movies = homeController.observableMovies;

        assertTrue(isSortedAscending(movies), "Die Filme sollten aufsteigend sortiert sein.");
    }

    @Test
    void test_Sort_Descending() {
        homeController.descending();
        List<Movie> movies = homeController.observableMovies;

        assertTrue(isSortedDescending(movies), "Die Filme sollten absteigend sortiert sein.");
    }

    @Test
    void test_Filter_With_No_Criteria_Returns_All_Movies() {
        // Suchbegriff auf einen leeren String und das Genre auf null setzen
        // um den Fall "ALL MOVIES" zu simulieren
        String query = "";
        String selectedGenre = ""; // "ALL MOVIES" impliziert, dass kein Genre ausgewählt ist

        // Filterfunktion mit den leeren Werten ausfüllen
        List<Movie> result = homeController.movieFilter(query, selectedGenre);

        // Überprüfen, ob die Liste der gefilterten Filme der Gesamtliste entspricht
        assertEquals(homeController.allMovies.size(), result.size(),
                "Die Filterfunktion sollte alle Filme zurückgeben, wenn keine Filterkriterien ausgewählt sind.");
    }

    // Hilfsmethoden zum Überprüfen der Sortierung
    boolean isSortedAscending(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getTitle().compareToIgnoreCase(movies.get(i + 1).getTitle()) > 0) {
                return false;
            }
        }
        return true;
    }

    boolean isSortedDescending(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getTitle().compareToIgnoreCase(movies.get(i + 1).getTitle()) < 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    void test_Get_Most_Popular_Actor() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("1", "Title", "Description", null, 2000, 8.0, null, List.of("Actor1", "Actor2"), null, null, 120));
        movies.add(new Movie("2", "Title", "Description", null, 2000, 8.0, null, List.of("Actor1", "Actor3"), null, null, 120));
        movies.add(new Movie("3", "Title", "Description", null, 2000, 8.0, null, List.of("Actor1", "Actor4"), null, null, 120));

        String mostPopularActor = homeController.getMostPopularActor(movies);

        assertEquals("Actor1", mostPopularActor);
    }
    @Test
    void test_Get_Longest_Movie_Title() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("1", "Title", "Description", null, 2000, 8.0, null, null, null, null, 120));
        movies.add(new Movie("2", "Long", "Description", null, 2000, 8.0, null, null, null, null, 120));
        movies.add(new Movie("3", "The Longest Title Ever", "Description", null, 2000, 8.0, null, null, null, null, 120));

        int longestTitleLength = homeController.getLongestMovieTitle(movies);

        assertEquals(22, longestTitleLength);
    }

    @Test
    void test_Count_Movies_From_Director() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("1", "Title", "Description", null, 2000, 8.0, null, null, null, List.of("Director1"), 120));
        movies.add(new Movie("2", "Title", "Description", null, 2000, 8.0, null, null, null, List.of("Director2"), 120));
        movies.add(new Movie("3", "Title", "Description", null, 2000, 8.0, null, null, null, List.of("Director1"), 120));

        long movieCount = homeController.countMoviesFrom(movies, "Director1");

        assertEquals(2, movieCount);
    }
    @Test
    void test_Get_Movies_Between_Years() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("1", "Title", "Description", null, 2000, 8.0, null, null, null, null, 120));
        movies.add(new Movie("2", "Title", "Description", null, 2005, 8.0, null, null, null, null, 120));
        movies.add(new Movie("3", "Title", "Description", null, 2010, 8.0, null, null, null, null, 120));

        List<Movie> filteredMovies = homeController.getMoviesBetweenYears(movies, 2001, 2009);

        assertEquals(1, filteredMovies.size());
        assertEquals(2005, filteredMovies.get(0).getReleaseYear());
    }

}