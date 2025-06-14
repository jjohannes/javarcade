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
        Image bgImage = layoutImage("bg.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        main.setBackground(new Background(backgroundImage));
    }

    static void topicsBG(Region main) {
        Image bgImage = layoutImage("topics.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        main.setBackground(new Background(backgroundImage));
    }

    static void terminalBG(Region main) {
        Image bgImage = layoutImage("terminal.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        main.setBackground(new Background(backgroundImage));
    }

    static void windowsBG(Region main) {
        Image bgImage = layoutImage("windows.png");
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        main.setBackground(new Background(backgroundImage));
    }

    private static Image layoutImage(String name) {
        return new Image("file:%s/%s".formatted(ASSET_LOCATION.resolve("layout"), name));
    }
}
