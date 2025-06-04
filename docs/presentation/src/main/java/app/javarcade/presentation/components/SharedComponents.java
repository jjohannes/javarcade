package app.javarcade.presentation.components;

import javafx.scene.Node;

public class SharedComponents {

    public static <T extends Node> T applyScrollPaneStyle(T pane) {
        pane.setStyle("-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-control-inner-background: transparent;" +
                "-fx-viewport-background: transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-light-text-color: black;" +
                "-fx-font-size: 24px;"
        );
        return pane;
    }

    public static <T extends Node> T applyScrollPaneStyleMono(T pane) {
        pane.setStyle("-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-control-inner-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-light-text-color: black;" +
                "-fx-font-family: Monospaced;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 24px;"
        );
        return pane;
    }
}
