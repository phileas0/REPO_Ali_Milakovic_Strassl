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

    public List<Genre> getGenres(){ return genres;}
    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        movies.add(new Movie ("Poor Things","Porn Movie with Emma Stone and Mark Ruffalo", Arrays.asList(Genre.DRAMA, Genre.ADVENTURE, Genre.FANTASY, Genre.ROMANCE)));
        movies.add(new Movie ("Interstellar","\"Interstellar\" erzählt die Geschichte eines Astronautenteams, das durch ein Wurmloch reist, um neue bewohnbare Welten zu finden und die Menschheit zu retten, die auf der sterbenden Erde ums Überleben kämpft.",Arrays.asList(Genre.SCIENCE_FICTION, Genre.DRAMA, Genre.ADVENTURE, Genre.MYSTERY)));
        movies.add(new Movie ("Spiderman: Into the Spider-Verse", "In \"Spider-Man: Into the Spider-Verse\" entdeckt der Teenager Miles Morales seine Kräfte als Spider-Man und trifft auf andere Spider-People aus verschiedenen Dimensionen, um eine Bedrohung zu bekämpfen, die das gesamte Multiversum gefährdet.",Arrays.asList(Genre.ANIMATION, Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Spiderman: Across The Spider-Verse","In \"Spider-Man: Across the Spider-Verse\" verbündet sich Miles Morales mit Spider-People aus anderen Dimensionen, um gegen eine Bedrohung zu kämpfen, die alle Realitäten des Multiversums gefährdet.",Arrays.asList(Genre.ANIMATION, Genre.ACTION, Genre.ADVENTURE, Genre.SCIENCE_FICTION)));
        movies.add(new Movie ("Inception", "\"Inception\" dreht sich um einen Dieb, der die Fähigkeit besitzt, in die Träume anderer Menschen einzudringen, um Ideen zu stehlen oder zu implantieren, und der einen letzten Auftrag annimmt, der ihm seine verlorene Lebensqualität zurückgeben könnte, indem er eine nahezu unmögliche \"Inception\" durchführt.",Arrays.asList(Genre.SCIENCE_FICTION,Genre.ACTION, Genre.ADVENTURE)));
        movies.add(new Movie ("Oppenheimer", "\"Oppenheimer\" erzählt die Geschichte von J. Robert Oppenheimer, dem theoretischen Physiker, der als einer der Väter der Atombombe gilt, und beleuchtet seine Rolle im Manhattan-Projekt sowie die ethischen und moralischen Dilemmata, mit denen er sich im Zuge der Entwicklung der Waffe konfrontiert sieht.", Arrays.asList(Genre.HISTORY, Genre.BIOGRAPHY, Genre.THRILLER)));


        return movies;
    }
}
