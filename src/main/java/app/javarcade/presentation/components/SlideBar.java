package app.javarcade.presentation.components;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.SPACE;

public record SlideBar(ImageView title, ImageView soap, ImageView cheatSheet, ImageView end) {

    public SlideBar(StackPane box) {
        this(logoButton("slide"), logoButton("slide"), logoButton("slide"), logoButton("slide"));

        VBox bar = new VBox(title(), soap(), cheatSheet(), end());
        bar.setSpacing(SPACE * 6);
        bar.setPadding(new Insets(SPACE));

        box.getChildren().add(bar);
    }

    private static ImageView logoButton(String iconName) {
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION, iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitWidth(60);
        iconView.setPickOnBounds(true); // Enable clicks on transparent areas
        return iconView;
    }
}
