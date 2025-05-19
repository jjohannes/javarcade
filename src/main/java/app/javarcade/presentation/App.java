package app.javarcade.presentation;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.TopicGrid;
import app.javarcade.presentation.data.JavarcadeProject;
import app.javarcade.presentation.state.SlideControl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.HEIGHT;
import static app.javarcade.presentation.ui.UI.SCREEN_DIM;
import static app.javarcade.presentation.ui.UI.SPACE;
import static app.javarcade.presentation.ui.UI.TOPICS_WIDTH;
import static app.javarcade.presentation.ui.UI.TREE_WIDTH;
import static app.javarcade.presentation.ui.UI.WIDTH;

public class App extends Application {

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
        StackPane projectStructure = createBox(topBox, TREE_WIDTH, SCREEN_DIM);
        StackPane editor = createBox(topBox, SCREEN_DIM, SCREEN_DIM);

        StackPane terminalBox = createBox(bottomBox, WIDTH - TOPICS_WIDTH, HEIGHT - SCREEN_DIM - SPACE * 4.0);
        StackPane topicBox = createBox(bottomBox, TOPICS_WIDTH - SPACE * 4, HEIGHT - SCREEN_DIM - SPACE * 4.0);

        topBox.setTranslateX(-SCREEN_DIM - SCREEN_DIM); // FIXME

        new SlideControl(
                new ApplicationScreen(runningApp),
                new Terminal(terminalBox),
                new TopicGrid(topicBox, JavarcadeProject.topics()),
                new ModuleGraph(moduleGraphBox, JavarcadeProject.modules()),
                new ProjectTree(projectStructure, ASSET_LOCATION.resolve("../javarcade"))
        );

        Scene scene = scalableScene(root);

        // scene.setOnKeyPressed(e -> { });
        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.setWidth(WIDTH * 0.5);
        stage.setHeight(HEIGHT * 0.5);
        stage.show();
    }

    private Scene scalableScene(BorderPane root) {
        Group scalableGroup = new Group(root);
        Scene scene = new Scene(scalableGroup, WIDTH, HEIGHT);
        Scale scale = new Scale(1, 1, 0, 0);
        scalableGroup.getTransforms().add(scale);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = newVal.doubleValue() / WIDTH;
            double scaleY = scene.getHeight() / HEIGHT;
            double scaleFactor = Math.min(scaleX, scaleY);
            scale.setX(scaleFactor);
            scale.setY(scaleFactor);
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double scaleX = scene.getWidth() / WIDTH;
            double scaleY = newVal.doubleValue() / HEIGHT;
            double scaleFactor = Math.min(scaleX, scaleY);
            scale.setX(scaleFactor);
            scale.setY(scaleFactor);
        });
        return scene;
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
