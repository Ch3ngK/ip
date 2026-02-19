package vex;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

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

    /** Keeps scroll at bottom when content grows; removed during scroll animation. */
    private InvalidationListener scrollToBottomListener;

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
        scrollToBottomListener = o -> scrollPane.setVvalue(1.0);
        dialogContainer.heightProperty().addListener(scrollToBottomListener);

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
     * Displays the greeting message when the GUI starts.
     *
     * @param v The Vex instance responsible for processing user commands.
     */
    public void setVex(Vex v) {
        vex = v;
        String greeting = vex.getGreeting();
        DialogBox vexBox = DialogBox.getVexDialog("", vexImage);

        addDialogsWithScroll(List.of(vexBox), List.of(false));
        typeText(vexBox.getDialogLabel(), greeting);
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
            response = vex.getByeMessage();
        } else {
            response = vex.getResponse(input);
        }

        DialogBox userBox = DialogBox.getUserDialog(input, userImage);
        DialogBox vexBox = DialogBox.getVexDialog("", vexImage);

        addDialogsWithScroll(List.of(userBox, vexBox), List.of(true, false));
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

    /**
     * Adds dialog boxes to the chat, runs entrance animations, and smooth-scrolls to bottom.
     *
     * @param boxes     Dialog boxes to add (in order)
     * @param fromRight For each box: true = slide from right (user), false = slide from left (Vex)
     */
    private void addDialogsWithScroll(List<DialogBox> boxes, List<Boolean> fromRight) {
        dialogContainer.heightProperty().removeListener(scrollToBottomListener);
        dialogContainer.getChildren().addAll(boxes);
        scrollPane.setVvalue(0);

        for (int i = 0; i < boxes.size(); i++) {
            playEntranceAnimation(boxes.get(i), fromRight.get(i));
        }
        Timeline scrollDown = new Timeline(
                new KeyFrame(Duration.millis(350), new KeyValue(scrollPane.vvalueProperty(), 1.0)));
        scrollDown.setOnFinished(e -> dialogContainer.heightProperty().addListener(scrollToBottomListener));
        scrollDown.play();
    }

    /**
     * Plays a fade-in + slide entrance animation on a dialog box.
     *
     * @param box       The dialog box to animate
     * @param fromRight true for user (slides from right), false for Vex (slides from left)
     */
    private void playEntranceAnimation(DialogBox box, boolean fromRight) {
        double startX = fromRight ? 40 : -40;
        box.setOpacity(0);
        box.setTranslateX(startX);

        FadeTransition fade = new FadeTransition(Duration.millis(280), box);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(280), box);
        slide.setFromX(startX);
        slide.setToX(0);

        new ParallelTransition(fade, slide).play();
    }

}
