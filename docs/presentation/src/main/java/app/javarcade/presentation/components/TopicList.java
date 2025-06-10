package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Topic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public record TopicList(List<Topic> topics) {

    public TopicList(HBox box, List<Topic> topics, ImageView slideView) {
        this(topics);
        topics.forEach(this::futureTopicStyle);

        var title = slideButton(140);
        var cheatSheet = slideButton(140);
        var end = slideButton(140);
        title.setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide-title");
        });
        cheatSheet.setOnMouseClicked(mouseEvent -> {
            markDone(topics.getLast());
            loadOrUnloadSlide(slideView, "slide-sheet");
        });
        end.setOnMouseClicked(mouseEvent -> {
            markDone(topics.getLast());
            loadOrUnloadSlide(slideView, "slide-end");
        });

        box.getChildren().addAll(title);
        box.getChildren().addAll(topics.stream().map(Topic::text).toList());
        box.getChildren().addAll(cheatSheet);
        box.getChildren().addAll(end);
    }

    private void loadOrUnloadSlide(ImageView slideView, String slide) {
        var url = "file:%s/%s.jpg".formatted(ASSET_LOCATION.resolve("slides"), slide);
        if (slideView.getImage() == null || !url.equals(slideView.getImage().getUrl())) {
            slideView.setImage(new Image(url));
        } else {
            slideView.setImage(null);
        }
    }

    private static Rectangle slideButton(int width) {
        Rectangle icon = new Rectangle(width, 80);
        icon.setFill(Color.TRANSPARENT);
        icon.setPickOnBounds(true); // Enable clicks on transparent areas
        return icon;
    }

    public void reset() {
        topics.forEach(this::futureTopicStyle);
    }

    public void focus(Topic topic) {
        topics.stream().dropWhile(t -> t != topic).forEach(this::futureTopicStyle);
        topics.stream().takeWhile(t -> t != topic).forEach(this::doneStyle);
        focusStyle(topic);
    }

    public void markDone(Topic topic) {
        topics.stream().dropWhile(t -> t != topic).forEach(this::futureTopicStyle);
        topics.stream().takeWhile(t -> t != topic).forEach(this::doneStyle);
        if (topic != null) doneStyle(topic);
    }

    private void doneStyle(Topic topic) {
        topic.text().setOpacity(1);
        Shape arrow = (Shape) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTGREEN);
    }

    private void focusStyle(Topic topic) {
        topic.text().setOpacity(1);
        Shape arrow = (Shape) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTBLUE);
    }

    private void futureTopicStyle(Topic topic) {
        topic.text().setOpacity(0.3);
        Shape arrow = (Shape) topic.text().getChildren().getFirst();
        arrow.setFill(Color.LIGHTGRAY);
    }
}
