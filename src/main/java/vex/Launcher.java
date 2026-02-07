package vex;

import javafx.application.Application;

/**
 * The {@code Launcher} class serves as the entry point of the Vex JavaFX
 * application.
 * <p>
 * This class is required to work around JavaFX packaging limitations when using
 * Gradle and modular Java versions (Java 11+).
 * </p>
 * <p>
 * It delegates control to the {@link Main} class, which extends
 * {@link javafx.application.Application} and initializes the GUI.
 * </p>
 */
public class Launcher {

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
