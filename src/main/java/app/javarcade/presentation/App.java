package app.javarcade.presentation;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.Editors;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.SlideBar;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.TopicGrid;
import app.javarcade.presentation.data.JavarcadeProject;
import app.javarcade.presentation.state.SlideControl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.GRAPH_WIDTH;
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
        topBox.setPrefWidth(WIDTH);
        ScrollPane topScrollPane = new ScrollPane(topBox);
        topScrollPane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0;");
        topScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        topScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        topScrollPane.setFitToHeight(true);
        topScrollPane.setFitToWidth(false);

        HBox bottomBox = new HBox(SPACE);
        VBox root = new VBox(topScrollPane, bottomBox);
        topBox.setPadding(new Insets(SPACE));
        bottomBox.setPadding(new Insets(0, SPACE, SPACE, SPACE));

        // Boxes in the top row (scrollable)
        StackPane slideBarBox = createBox(topBox, 100, SCREEN_DIM);
        StackPane applicationBox = createBox(topBox, SCREEN_DIM, SCREEN_DIM);
        StackPane moduleGraphBox = createBox(topBox, GRAPH_WIDTH, SCREEN_DIM);
        StackPane projectStructureBox = createBox(topBox, TREE_WIDTH, SCREEN_DIM);
        StackPane editorsBox = createBox(topBox, GRAPH_WIDTH, SCREEN_DIM);

        // Boxes in the bottom row
        StackPane terminalBox = createBox(bottomBox, WIDTH - TOPICS_WIDTH, HEIGHT - SCREEN_DIM - SPACE * 4.0);
        StackPane topicBox = createBox(bottomBox, TOPICS_WIDTH - SPACE * 4, HEIGHT - SCREEN_DIM - SPACE * 4.0);

        new SlideControl(
                new SlideBar(slideBarBox),
                new ApplicationScreen(applicationBox),
                new ModuleGraph(moduleGraphBox, JavarcadeProject.modules()),
                new ProjectTree(projectStructureBox, ASSET_LOCATION.resolve("../javarcade")),
                new Editors(editorsBox, ASSET_LOCATION.resolve("..")),
                new Terminal(terminalBox),
                new TopicGrid(topicBox, JavarcadeProject.topics())
        );

        Scene scene = scalableScene(root);

        // scene.setOnKeyPressed(e -> { });
        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.show();
    }

    private Scene scalableScene(VBox root) {
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
