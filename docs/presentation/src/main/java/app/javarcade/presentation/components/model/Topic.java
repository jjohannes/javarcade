package app.javarcade.presentation.components.model;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public record Topic(String title, Pane text) {

    public Topic(String title) {
        this(title, topicCell(title));
    }

    private static Pane topicCell(String title) {
        Text text = new Text(title);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(160);
        text.setFont(new Font(22));

        Polygon arrow = new Polygon(
                0.0, 0,
                220.0, 0.0,
                250.0, 30.0,
                220.0, 60.0,
                0.0, 60.0
        );
        arrow.setFill(Color.WHITE);
        arrow.setStroke(Color.BLACK);
        arrow.setStrokeWidth(2);

        StackPane pane = new StackPane(text);
        pane.setAlignment(Pos.CENTER_LEFT);

        return pane;
    }
}
