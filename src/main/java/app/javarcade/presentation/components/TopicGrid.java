package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Topic;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Set;

import static app.javarcade.presentation.App.RATIO;

public record TopicGrid(Set<Topic> topics) {
    public TopicGrid(StackPane box, Set<Topic> topics) {
        this(topics);

        GridPane grid = new GridPane();
        grid.setHgap(10 / RATIO);
        grid.setVgap(10 / RATIO);

        topics.forEach(topic -> grid.add(topic.text(), topic.columnIndex(), topic.rowIndex()));

        box.getChildren().add(grid);
    }
}
