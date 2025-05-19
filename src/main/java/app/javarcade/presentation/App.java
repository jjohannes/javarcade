package app.javarcade.presentation;

import app.javarcade.presentation.data.JavarcadeProject;
import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.TopicGrid;
import app.javarcade.presentation.state.SlideControl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UIComponents.errorTextView;
import static app.javarcade.presentation.ui.UIComponents.screenshotView;

public class App extends Application {

    public static final int RATIO = 2;

    public static final int WIDTH = 1920 / RATIO;
    public static final int HEIGHT = 1080 / RATIO;
    public static final int SCREEN_DIM = 720 / RATIO;
    public static final int WARNING_DIM = 610 / RATIO;
    public static final int TREE_DIM = 640 / RATIO;

    public static final int SPACE = 20 / RATIO;

    @Override
    public void start(Stage stage) {
        HBox topBox = new HBox(SPACE);
        HBox bottomBox = new HBox(SPACE);
        topBox.setPadding(new Insets(SPACE));
        bottomBox.setPadding(new Insets(0, SPACE, SPACE, SPACE));
        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setBottom(bottomBox);

        StackPane runningApp = createBox(topBox, SCREEN_DIM, SCREEN_DIM);
        StackPane moduleGraphBox = createBox(topBox, WIDTH - SCREEN_DIM - SPACE * 6, SCREEN_DIM);
        StackPane projectStructure = createBox(topBox, TREE_DIM, SCREEN_DIM);
        StackPane editor = createBox(topBox, SCREEN_DIM, SCREEN_DIM);

        StackPane terminalBox = createBox(bottomBox, WIDTH - WARNING_DIM, HEIGHT - SCREEN_DIM - SPACE * 4.0);
        StackPane topicBox = createBox(bottomBox, WARNING_DIM - SPACE * 4, HEIGHT - SCREEN_DIM - SPACE * 4.0);

        topBox.setTranslateX(-SCREEN_DIM - SCREEN_DIM); // FIXME

        new SlideControl(
                new ApplicationScreen(screenshotView(runningApp), errorTextView(runningApp)),
                new Terminal(terminalBox),
                new TopicGrid(topicBox, JavarcadeProject.topics()),
                new ModuleGraph(moduleGraphBox, JavarcadeProject.modules()),
                new ProjectTree(projectStructure, ASSET_LOCATION.resolve("../javarcade"))
        );

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        // scene.setOnKeyPressed(e -> { });
        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.show();
    }

    private StackPane createBox(Pane parent, double width, double height) {
        StackPane inner = new StackPane();
        inner.setPadding(new Insets(SPACE * 0.5));
        inner.setPrefWidth(width - SPACE);
        inner.setPrefHeight(height - SPACE);

        Rectangle rectangle = new Rectangle(width, height, Color.TRANSPARENT);
        StackPane stackPane = new StackPane(rectangle, inner);
        stackPane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        parent.getChildren().add(stackPane);

        return inner;
    }
}
