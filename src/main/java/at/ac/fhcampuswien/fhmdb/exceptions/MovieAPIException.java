package at.ac.fhcampuswien.fhmdb.exceptions;

public class MovieAPIException extends Exception {
    public MovieAPIException() {
        super();
    }

    public MovieAPIException(String message) {
        super(message);
    }

    public MovieAPIException(Throwable cause) {
        super(cause);
    }

    public MovieAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}