package at.ac.fhcampuswien.fhmdb.Interface;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String message);
}
