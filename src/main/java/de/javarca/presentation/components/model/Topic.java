package de.javarca.presentation.components.model;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public record Topic(String title, Text text) {

    public Topic(String title) {
        this(title, topicCell(title));
    }

    private static Text topicCell(String title) {
        Text text = new Text(title);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(160);
        text.setFont(Font.font("Monospaced", FontWeight.BOLD, FontPosture.ITALIC, 21));
        text.setOpacity(0.3);
        return text;
    }
}
