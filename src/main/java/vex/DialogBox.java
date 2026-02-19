package vex;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

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

        // Text settings
        dialog.setText(text);
        dialog.setWrapText(true);
        dialog.setMaxWidth(250);

        // Image
        displayPicture.setImage(img);

        this.setMaxWidth(Region.USE_PREF_SIZE);
        this.setAlignment(Pos.TOP_LEFT); // internal alignment only
    }

    /** User dialog: text left, image right */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);

        db.getStyleClass().add("dialog-user");
        db.dialog.getStyleClass().add("dialog-label");

        db.getChildren().setAll(db.dialog, db.displayPicture);

        return db;
    }

    /** Vex dialog: image left, text right */
    public static DialogBox getVexDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);

        db.getStyleClass().add("dialog-vex");
        db.dialog.getStyleClass().add("dialog-label");

        db.getChildren().setAll(db.displayPicture, db.dialog);

        return db;
    }

    public Label getDialogLabel() {
        return dialog;
    }
}
