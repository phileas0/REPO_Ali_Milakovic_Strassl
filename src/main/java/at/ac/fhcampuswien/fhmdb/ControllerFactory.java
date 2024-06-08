package at.ac.fhcampuswien.fhmdb;

import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private final Map<Class<?>, Object> cache = new HashMap<>();
    private static final Logger logger = Logger.getLogger(ControllerFactory.class.getName());

    @Override
    public Object call(Class<?> type) {
        return cache.computeIfAbsent(type, k -> {
            try {
                logger.log(Level.INFO, "Creating a new instance of " + type.getName());
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Could not create controller: " + type.getName(), e);
                throw new RuntimeException("Could not create controller: " + type.getName(), e);
            }
        });
    }
}