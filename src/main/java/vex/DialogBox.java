package vex;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Dialog box consisting of text and an avatar image.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/DialogBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /** User dialog: text left, image right */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);

        db.getStyleClass().add("dialog-user");
        db.dialog.getStyleClass().add("dialog-label");

        db.getChildren().setAll(db.dialog, db.displayPicture);
        db.setAlignment(Pos.TOP_RIGHT);

        return db;
    }

    /** Vex dialog: image left, text right */
    public static DialogBox getVexDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);

        db.getStyleClass().add("dialog-vex");
        db.dialog.getStyleClass().add("dialog-label");

        db.getChildren().setAll(db.displayPicture, db.dialog);
        db.setAlignment(Pos.TOP_LEFT);

        return db;
    }

    public Label getDialogLabel() {
        return dialog;
    }

}
