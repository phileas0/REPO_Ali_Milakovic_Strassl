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

    public String getGenresString(){
        StringBuilder genreList = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            genreList.append(genres.get(i));
            if ((i + 1) < genres.size()) {
                genreList.append(", ");
            }
        }
        return genreList.toString();
    }

    public List<Genre> getGenres(){
        return genres;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        movies.add(new Movie ("Poor Things","Porn Movie with Emma Stone and Mark Ruffalo", Arrays.asList(Genre.DRAMA, Genre.ADVENTURE, Genre.FANTASY, Genre.ROMANCE)));
        movies.add(new Movie ("Forrest Gump", "Forrest Gump is a film about the extraordinary life of a simple man who, through chance encounters and a sincere approach to life, achieves unexpected fame. Along the way, he experiences significant historical events and learns the importance of love, friendship, and self-determination.", Arrays.asList(Genre.WAR, Genre.ROMANCE, Genre.FANTASY, Genre.ADVENTURE, Genre.COMEDY)));
        movies.add(new Movie ("Dune: Part One", "The first of part Dune directed by Dennis Villeneuve, depicts a young nobleman's journey on a desert planet as he becomes entangled in political intrigue and the mystical powers of an ancient prophecy.", Arrays.asList(Genre.ADVENTURE, Genre.FANTASY, Genre.ROMANCE, Genre.WAR, Genre.SCIENCE_FICTION, Genre.DRAMA)));
        movies.add(new Movie ("The Godfather",  "The Godfather Part I follows the rise of Michael Corleone within a powerful Mafia family, navigating a world of crime, loyalty, and betrayal amidst the backdrop of 1940s New York.", Arrays.asList(Genre.CRIME, Genre.DRAMA)));
        movies.add(new Movie ("Midsommar", "Midsommar, directed by Ari Aster, explores a group of friends' journey to a remote Swedish village to partake in a festival, unraveling a disturbing and surreal descent into pagan rituals and psychological horror.", Arrays.asList(Genre.HORROR, Genre.FANTASY, Genre.THRILLER, Genre.MYSTERY, Genre.DOCUMENTARY)));
        movies.add(new Movie ("Interstellar","\"Interstellar\" erzählt die Geschichte eines Astronautenteams, das durch ein Wurmloch reist, um neue bewohnbare Welten zu finden und die Menschheit zu retten, die auf der sterbenden Erde ums Überleben kämpft.",Arrays.asList(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE, Genre.MYSTERY)));
        movies.add(new Movie ("Spiderman: Into the Spider-Verse", "In \"Spider-Man: Into the Spider-Verse\" entdeckt der Teenager Miles Morales seine Kräfte als Spider-Man und trifft auf andere Spider-People aus verschiedenen Dimensionen, um eine Bedrohung zu bekämpfen, die das gesamte Multiversum gefährdet.",Arrays.asList(Genre.ANIMATION, Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Spiderman: Across The Spider-Verse","In \"Spider-Man: Across the Spider-Verse\" verbündet sich Miles Morales mit Spider-People aus anderen Dimensionen, um gegen eine Bedrohung zu kämpfen, die alle Realitäten des Multiversums gefährdet.",Arrays.asList(Genre.ANIMATION, Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Inception", "\"Inception\" dreht sich um einen Dieb, der die Fähigkeit besitzt, in die Träume anderer Menschen einzudringen, um Ideen zu stehlen oder zu implantieren, und der einen letzten Auftrag annimmt, der ihm seine verlorene Lebensqualität zurückgeben könnte, indem er eine nahezu unmögliche \"Inception\" durchführt.",Arrays.asList(Genre.SCIENCE_FICTION,Genre.ACTION, Genre.ADVENTURE)));
        movies.add(new Movie ("Oppenheimer", "\"Oppenheimer\" erzählt die Geschichte von J. Robert Oppenheimer, dem theoretischen Physiker, der als einer der Väter der Atombombe gilt, und beleuchtet seine Rolle im Manhattan-Projekt sowie die ethischen und moralischen Dilemmata, mit denen er sich im Zuge der Entwicklung der Waffe konfrontiert sieht.", Arrays.asList(Genre.HISTORY, Genre.BIOGRAPHY, Genre.THRILLER)));
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
