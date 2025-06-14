package app.javarcade.presentation.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public interface UI {
    int WIDTH = 1920;
    int HEIGHT = 1080;
    int SPACE = 20;

    int MIDDLE_START = 145;
    int BOTTOM_START = MIDDLE_START + 680;
    int MIDDLE_CONTENT_HEIGHT = 625;

    int APP_SCREEN_WIDTH = 600;
    int TREE_WIDTH = 580;
    int GRAPH_WIDTH = 660;
    int EDITOR_WIDTH = 880;
    int TOOLS_WIDTH = 220;
    int TERMINAL_WIDTH = 1120;

    static <T extends Node> T applyScrollPaneStyle(T pane) {
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

    static <T extends Node> T applyScrollPaneStyleMono(T pane) {
        pane.setStyle("-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-control-inner-background: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-light-text-color: black;" +
                "-fx-font-family: Monospaced;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 20px;"
        );
        return pane;
    }

    static void mainBG(Region main) {
        main.setBackground(layoutImage("bg.jpg"));
    }

    static void topicsBG(Region main) {
        main.setBackground(layoutImage("topics.png"));
    }

    static void terminalBG(Region main) {
        main.setBackground(layoutImage("terminal.png"));
    }

    static void windowsBG(Region main) {
        main.setBackground(layoutImage("windows.png"));
    }

    static void slideBG(Region slide) {
        slide.setBackground(layoutImage("slide.jpg"));
    }

    private static Background layoutImage(String name) {
        Image image = new Image("file:%s/%s".formatted(ASSET_LOCATION.resolve("layout"), name));
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        return new Background(backgroundImage);
    }
}
