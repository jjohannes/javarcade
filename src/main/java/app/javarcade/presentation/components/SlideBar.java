package app.javarcade.presentation.components;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.SPACE;
import static app.javarcade.presentation.ui.UI.WIDTH;

public record SlideBar(ImageView title, ImageView soap, ImageView cheatSheet, ImageView reminder, ImageView end) {

    public SlideBar(StackPane box, ImageView slideView) {
        this(logoButton("slide"), logoButton("slide"), logoButton("slide"), logoButton("slide"), logoButton("slide"));

        VBox bar = new VBox(title(), soap(), cheatSheet(), reminder(), end());
        bar.setSpacing(SPACE * 4);
        bar.setPadding(new Insets(SPACE));
        box.getChildren().add(bar);

        title().setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide1");
        });
        soap().setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide2");
        });
        cheatSheet().setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide3");
        });
        reminder().setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide4");
        });
        end().setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide5");
        });

        slideView.setFitWidth(WIDTH - SPACE * 10);
        slideView.setPreserveRatio(true);
    }

    private void loadOrUnloadSlide(ImageView slideView, String slide) {
        var url = "file:%s/%s.jpg".formatted(ASSET_LOCATION.resolve("../slides"), slide);
        if (slideView.getImage() == null || !url.equals(slideView.getImage().getUrl())) {
            slideView.setImage(new Image(url));
            slideView.setVisible(true);
        } else {
            slideView.setImage(null);
            slideView.setVisible(false);
        }
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
