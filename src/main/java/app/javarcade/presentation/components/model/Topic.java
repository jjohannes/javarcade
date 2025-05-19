package app.javarcade.presentation.components.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static app.javarcade.presentation.ui.UI.SPACE;

public record Topic(String title, int columnIndex, int rowIndex, Pane text) {

    public Topic(String title, int columnIndex, int rowIndex) {
        this(title, columnIndex, rowIndex, topicCell(title));
    }

    private static Pane topicCell(String title) {
        Text text = new Text(title);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(150);
        text.setFont(new Font(24));

        StackPane pane = new StackPane(text);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(SPACE * 0.25));
        pane.setMinHeight(124);

        pane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;");

        return pane;
    }
}
