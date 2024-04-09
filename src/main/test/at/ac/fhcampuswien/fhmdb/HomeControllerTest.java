package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp(){
        homeController = new HomeController();
    }

    @Test
    void check_If_List_Of_Genres_Exists() {
        //Given
        Genre[] expectedGenres = {
                Genre.ACTION,
                Genre.ADVENTURE,
                Genre.ANIMATION,
                Genre.BIOGRAPHY,
                Genre.COMEDY,
                Genre.CRIME,
                Genre.DRAMA,
                Genre.DOCUMENTARY,
                Genre.FAMILY,
                Genre.FANTASY,
                Genre.HISTORY,
                Genre.HORROR,
                Genre.MUSICAL,
                Genre.MYSTERY,
                Genre.ROMANCE,
                Genre.SCIENCE_FICTION,
                Genre.SPORT,
                Genre.THRILLER,
                Genre.WAR,
                Genre.WESTERN
        };


        //When
        Genre[] actualGenres = Genre.values();


        //Then
        for (Genre expectedGenre : expectedGenres) {
            boolean exists = false;
            for (Genre actualGenre : actualGenres) {
                if (expectedGenre.equals(actualGenre)) {
                    exists = true;
                    break;
                }
            }
            assertTrue(exists, "Das Genre " + expectedGenre + " sollte in der Genre enum existieren.");
        }
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
    void test_Filter_By_Description_Returns_True_If_Description_Contains_Query() {
        String query = "Marine dispatched";
        Genre selectedGenre = null;
        List<Movie> result = homeController.movieFilter(query.toLowerCase(), selectedGenre);

        assertTrue(result.stream().anyMatch(movie ->
                        movie.getDescription().toLowerCase().contains(query)),
                "Mindestens ein Film sollte 'Marine dispatched' in der Beschreibung enthalten.");
    }

    @Test
    void test_Filter_By_Genre_Returns_True_If_Selected_Genre_Is_Displayed() {
        Genre selectedGenre = Genre.SCIENCE_FICTION;
        List<Movie> result = homeController.movieFilter("", selectedGenre);

        assertTrue(result.stream().allMatch(movie ->
                        movie.getGenres().contains(selectedGenre)),
                "Alle Filme sollten dem Genre SCIENCE_FICTION zugeordnet sein.");
    }

    @Test
    void test_Filter_By_Title_And_Genre() {
        String query = "spider";
        Genre selectedGenre = Genre.ANIMATION;
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
        Genre selectedGenre = null; // "ALL MOVIES" impliziert, dass kein Genre ausgewählt ist

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

}