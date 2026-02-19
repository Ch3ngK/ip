package vex;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for the Vex GUI.
 */
public class Main extends Application {

    private static final String DATA_PATH = "data/tasks.txt";

    @Override
    public void start(Stage stage) {
        try {
            Vex vex = new Vex(DATA_PATH);

            FXMLLoader fxmlLoader = new FXMLLoader(
                    Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();

            Scene scene = new Scene(ap);
            scene.getStylesheets().add(
                    Main.class.getResource("/styles.css").toExternalForm());

            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setVex(vex);
            stage.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration error");
            alert.setHeaderText("Invalid data path");
            alert.setContentText("Cannot use data path: " + DATA_PATH + ". " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Startup error");
            alert.setHeaderText("Could not load the application");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
