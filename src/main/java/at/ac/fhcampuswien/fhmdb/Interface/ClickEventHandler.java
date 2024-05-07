package at.ac.fhcampuswien.fhmdb.Interface;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}
