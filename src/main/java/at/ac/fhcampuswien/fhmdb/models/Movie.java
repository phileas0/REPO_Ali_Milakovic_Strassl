package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    // TODO add more properties here
    private List<Genre> genres;
    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres(){
        StringBuilder genreList = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            genreList.append(genres.get(i));
            if ((i + 1) < genres.size()) {
                genreList.append(", ");
            }
        }
        return genreList.toString();
    }
    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        movies.add(new Movie ("Poor Things","Porn Movie with Emma Stone and Mark Ruffalo", Arrays.asList(Genre.DRAMA, Genre.ADVENTURE, Genre.FANTASY, Genre.ROMANCE)));
        movies.add(new Movie ("Pulp Fiction","The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", Arrays.asList(Genre.COMEDY, Genre.ADVENTURE, Genre.CRIME, Genre.THRILLER)));
        movies.add(new Movie ("No Country For Old Men","When a man stumbles upon a drug deal gone wrong and a case full of money, he sets off a chain of events that leads to relentless pursuit by a ruthless killer.", Arrays.asList(Genre.THRILLER, Genre.WESTERN, Genre.ACTION, Genre.CRIME)));
        movies.add(new Movie ("Inglourious Basterds","The film tells the fictional alternate history tale of the unwitting convergence of two independent plots to assassinate Nazi Germany's political leadership.", Arrays.asList(Genre.HISTORY, Genre.COMEDY, Genre.WAR, Genre.ACTION)));
        movies.add(new Movie ("Django Unchained","A slave named Django who teams up with a German bounty hunter to rescue his wife from a cruel plantation owner.", Arrays.asList(Genre.WESTERN, Genre.ADVENTURE, Genre.COMEDY, Genre.ACTION)));
        movies.add(new Movie ("Shrek","It's the story of a terrifying green ogre by the name of Shrek, who lives in a swamp.", Arrays.asList(Genre.ANIMATION, Genre.ADVENTURE, Genre.COMEDY, Genre.ROMANCE)));
        movies.add(new Movie ("The SpongeBob SquarePants Movie","SpongeBob takes leave from Bikini Bottom in order to track down, with Patrick, King Neptune's stolen crown.", Arrays.asList(Genre.ANIMATION, Genre.ADVENTURE, Genre.COMEDY, Genre.FAMILY)));
        movies.add(new Movie ("John Wick","A retired assassin who returns back to his old ways after a group of Russian gangsters steal his car and kill his puppy.", Arrays.asList(Genre.DRAMA, Genre.ACTION, Genre.CRIME, Genre.COMEDY)));
        movies.add(new Movie ("Nobody","A mild-mannered family man who returns to his former life of an assassin after he and his family become the target of a vengeful crime lord.", Arrays.asList(Genre.DRAMA, Genre.ACTION, Genre.CRIME, Genre.COMEDY)));
        return movies;
    }
}
