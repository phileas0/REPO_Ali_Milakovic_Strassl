package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp(){
        homeController = new HomeController();
    }
    @Test
    void list_Of_Genres_Exists() {
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
    void test_Search_By_Title() {
        // Angenommen, "Inception" ist Teil des Titels eines Films in allMovies
        String query = "inception";
        List<Movie> result = homeController.movieFilter(query.toLowerCase(), null);

        // Stelle sicher, dass alle Filme in 'result' den Suchbegriff im Titel enthalten
        assertTrue(result.stream().allMatch(movie ->
                        movie.getTitle().toLowerCase().contains(query)),
                "Alle Filme sollten 'inception' im Titel enthalten.");
    }

}