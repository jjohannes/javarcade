package app.javarcade.presentation.components;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;

public record ApplicationScreen(ImageView screenshot, Text error) {

    public ApplicationScreen(StackPane box) {
        this(screenshotView(box), errorTextView(box));
    }

    private static ImageView screenshotView(StackPane box) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(box.getPrefWidth());
        imageView.setFitHeight(box.getPrefHeight());
        box.getChildren().add(imageView);
        return imageView;
    }

    private static Text errorTextView(StackPane box) {
        Text text = new Text();
        text.setFont(Font.font("Monospaced", FontWeight.BOLD, 32));
        text.setFill(Color.WHITE);
        Rectangle background = new Rectangle();
        background.setFill(Color.web("#FF7F7FBB"));
        background.widthProperty().bind(text.layoutBoundsProperty().map(Bounds::getWidth));
        background.heightProperty().bind(text.layoutBoundsProperty().map(Bounds::getHeight));

        TextFlow textFlow = new TextFlow(text);
        box.setAlignment(Pos.TOP_LEFT);
        box.getChildren().add(background);
        box.getChildren().add(textFlow);

        return text;
    }

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
