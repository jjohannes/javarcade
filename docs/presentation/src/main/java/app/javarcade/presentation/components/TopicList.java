package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Topic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.SPACE;

public record TopicList(List<Topic> topics, List<ImageView> topicFocusBg) {

    public TopicList(Pane box, List<Topic> topics, ImageView slideView) {
        this(topics, new ArrayList<>());
        topics.forEach(this::futureTopicStyle);

        var title = slideButton(140);
        var cheatSheet = slideButton(140);
        var end = slideButton(140);
        title.setOnMouseClicked(mouseEvent -> {
            loadOrUnloadSlide(slideView, "slide-title.jpg");
        });
        cheatSheet.setOnMouseClicked(mouseEvent -> {
            focus(topics.getLast());
            loadOrUnloadSlide(slideView, "slide-sheet.jpg");
        });
        end.setOnMouseClicked(mouseEvent -> {
            focus(topics.getLast());
            loadOrUnloadSlide(slideView, "slide-end.jpg");
        });

        HBox bar = new HBox(SPACE * 0.5);
        bar.setAlignment(Pos.TOP_CENTER);
        bar.setPadding(new Insets(SPACE * 2.5, 0, 0, 0));

        bar.getChildren().addAll(title);
        bar.getChildren().addAll(topics.stream().map(Topic::text).toList());
        bar.getChildren().addAll(cheatSheet);
        bar.getChildren().addAll(end);

        for (int i = 0; i <= topics.size(); i++) {
            var image = new ImageView(new Image("file:%s/topics%s.png".formatted(ASSET_LOCATION.resolve("layout"), i)));
            topicFocusBg.add(image);
            image.setVisible(false);
            box.getChildren().add(image);
        }

        box.getChildren().add(bar);
    }

    public static void loadOrUnloadSlide(ImageView slideView, String slide) {
        var url = "file:%s/%s".formatted(ASSET_LOCATION.resolve("slides"), slide);
        if (slideView.getParent().isVisible()) {
            slideView.getParent().setVisible(false);
        } else {
            slideView.setImage(new Image(url));
            slideView.getParent().setVisible(true);
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
        int topicIndex = topic == null ? 0 : topics.indexOf(topic);
        for (int i = 0; i <= topicIndex; i++) {
            topics.get(i).text().setOpacity(1.0);
            topicFocusBg.get(i).setVisible(true);
        }
        for (int i = topicIndex + 1; i < topics.size(); i++) {
            topics.get(i).text().setOpacity(0.3);
            topicFocusBg.get(i).setVisible(false);
        }
    }

    private void futureTopicStyle(Topic topic) {
        topic.text().setOpacity(0.3);
    }
}
