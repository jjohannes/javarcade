package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Topic;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

public record TopicList(List<Topic> topics) {

    public TopicList(HBox box, List<Topic> topics) {
        this(topics);
        topics.forEach(this::futureTopicStyle);
        box.getChildren().addAll(topics.stream().map(Topic::text).toList());
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
