package app.javarcade.presentation.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;

public record ApplicationScreen(ImageView screenshot, Text error) {

    public void reset() {
        screenshot.setVisible(false);
        error.setText("");
    }

    public void reloadScreenshot() {
        var imageFile = WORK_FOLDER.resolve("out/screen.png").toFile();
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            screenshot().setImage(image);
            screenshot().setVisible(true);
        } else {
            screenshot().setVisible(false);
        }
    }
}
