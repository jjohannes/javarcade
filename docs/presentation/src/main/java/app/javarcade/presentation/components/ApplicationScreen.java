package app.javarcade.presentation.components;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;

public record ApplicationScreen(ImageView screenshot) {

    public ApplicationScreen(StackPane box) {
        this(screenshotView(box));
    }

    private static ImageView screenshotView(StackPane box) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(box.getPrefWidth());
        imageView.setFitHeight(box.getPrefHeight());
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(imageView);
        return imageView;
    }

    public void reset() {
        screenshot.setVisible(false);
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
