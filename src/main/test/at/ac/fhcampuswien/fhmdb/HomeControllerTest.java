package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
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


}