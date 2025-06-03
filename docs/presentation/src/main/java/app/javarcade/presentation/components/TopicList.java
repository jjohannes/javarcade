package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Topic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public record TopicList(List<Topic> topics) {

    public TopicList(HBox box, List<Topic> topics, ImageView slideView) {
        this(topics);
        topics.forEach(this::futureTopicStyle);

        var title = slideButton("slide");
        var cheatSheet = slideButton("slide");
        var end = slideButton("slide");
        title.setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide-title");
        });
        cheatSheet.setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide-sheet");
        });
        end.setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide-end");
        });

        box.getChildren().addAll(title);
        box.getChildren().addAll(topics.stream().map(Topic::text).toList());
        box.getChildren().addAll(cheatSheet, end);
    }

    private void loadOrUnloadSlide(ImageView slideView, String slide) {
        var url = "file:%s/%s.jpg".formatted(ASSET_LOCATION.resolve("slides"), slide);
        if (slideView.getImage() == null || !url.equals(slideView.getImage().getUrl())) {
            slideView.setImage(new Image(url));
            slideView.setVisible(true);
        } else {
            slideView.setImage(null);
            slideView.setVisible(false);
        }
    }

    private static ImageView slideButton(String iconName) {
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitWidth(60);
        iconView.setPickOnBounds(true); // Enable clicks on transparent areas
        return iconView;
    }

    public void focus(Topic topic) {
        topics.stream().dropWhile(t -> t != topic).forEach(this::futureTopicStyle);
        topics.stream().takeWhile(t -> t != topic).forEach(this::doneStyle);
        focusStyle(topic);
    }

    public void markDone(Topic topic) {
        topics.stream().dropWhile(t -> t != topic).forEach(this::futureTopicStyle);
        topics.stream().takeWhile(t -> t != topic).forEach(this::doneStyle);
        doneStyle(topic);
    }

    private void doneStyle(Topic topic) {
        topic.text().setOpacity(1);
        Polygon arrow = (Polygon) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTGREEN);
    }

    private void focusStyle(Topic topic) {
        topic.text().setOpacity(1);
        Polygon arrow = (Polygon) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTBLUE);
    }

    private void futureTopicStyle(Topic topic) {
        topic.text().setOpacity(0.3);
        Polygon arrow = (Polygon) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTGRAY);
    }
}
