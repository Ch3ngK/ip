package vex;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller for the main window of the Vex application.
 * <p>
 * Handles user interactions from the GUI, manages dialog display,
 * and connects the JavaFX interface to the {@link Vex} logic.
 * </p>
 */
public class MainWindow {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Vex vex;

    private Image userImage;
    private Image vexImage;

    /**
     * Initializes the main window after its FXML elements are loaded.
     * <p>
     * Binds the scroll pane to automatically scroll to the latest dialog,
     * applies the Dota 2 themed background, and loads avatar images
     * for the user and Vex.
     * </p>
     */
    @FXML
    public void initialize() {
        // Auto-scroll to bottom
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Dota 2 background
        rootPane.setStyle(
                "-fx-background-image: url('/images/dota_bg.jpg');"
                        + "-fx-background-size: cover;"
                        + "-fx-background-position: center center;");

        // Load avatars
        userImage = new Image(getClass().getResourceAsStream("/images/dota_user.png"));
        vexImage = new Image(getClass().getResourceAsStream("/images/dota_vex.png"));
    }

    /**
     * Injects the {@link Vex} logic instance into this controller.
     *
     * @param v The Vex instance responsible for processing user commands.
     */
    public void setVex(Vex v) {
        vex = v;
    }

    /**
     * Handles user input submitted through the text field or send button.
     * <p>
     * Displays the user's message, obtains a response from Vex,
     * and shows both as dialog boxes in the chat window.
     * If the user enters the {@code bye} command, a farewell message
     * is shown and the application exits after a short delay.
     * </p>
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response;
        if (input.equalsIgnoreCase("bye")) {
            response = "GG! Hope to see you again on the battlefield!";
        } else {
            response = vex.getResponse(input);
        }

        DialogBox userBox = DialogBox.getUserDialog(input, userImage);
        DialogBox vexBox = DialogBox.getVexDialog("", vexImage);

        dialogContainer.getChildren().addAll(userBox, vexBox);

        // Animate Vex typing
        typeText(vexBox.getDialogLabel(), response);

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.7));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }

    private void typeText(Label label, String text) {
        label.setText("");
        Timeline timeline = new Timeline();
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(25 * i),
                            e -> label.setText(text.substring(0, index + 1))));
        }
        timeline.play();
    }

}
