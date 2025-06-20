package de.javarca.presentation;

import de.javarca.presentation.components.ApplicationScreen;
import de.javarca.presentation.components.Editor;
import de.javarca.presentation.components.ModuleGraph;
import de.javarca.presentation.components.ProjectTree;
import de.javarca.presentation.components.Terminal;
import de.javarca.presentation.components.ToolsGrid;
import de.javarca.presentation.components.TopicList;
import de.javarca.presentation.data.JavarcadeProject;
import de.javarca.presentation.state.SlideControl;
import de.javarca.presentation.ui.UI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import static de.javarca.presentation.data.JavarcadeProject.APP_ROOT_FOLDER;
import static de.javarca.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static de.javarca.presentation.ui.UI.BOTTOM_START;
import static de.javarca.presentation.ui.UI.EDITOR_WIDTH;
import static de.javarca.presentation.ui.UI.GRAPH_WIDTH;
import static de.javarca.presentation.ui.UI.HEIGHT;
import static de.javarca.presentation.ui.UI.MIDDLE_CONTENT_HEIGHT;
import static de.javarca.presentation.ui.UI.MIDDLE_START;
import static de.javarca.presentation.ui.UI.APP_SCREEN_WIDTH;
import static de.javarca.presentation.ui.UI.SPACE;
import static de.javarca.presentation.ui.UI.TERMINAL_WIDTH;
import static de.javarca.presentation.ui.UI.TOOLS_WIDTH;
import static de.javarca.presentation.ui.UI.TREE_WIDTH;
import static de.javarca.presentation.ui.UI.WIDTH;
import static de.javarca.presentation.ui.UI.applyScrollPaneStyle;

public class App extends Application {

    private StackPane applicationBox = null;

    @Override
    public void start(Stage stage) {
        var topicsBox = new StackPane();
        topicsBox.setAlignment(Pos.TOP_LEFT);

        var middleBox = new HBox(SPACE * 2);
        middleBox.setPrefWidth(WIDTH);
        ScrollPane middleScrollPane = applyScrollPaneStyle(new ScrollPane(middleBox));
        middleScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        middleScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        middleScrollPane.setFitToHeight(true);
        middleScrollPane.setFitToWidth(false);

        var bottomBox = new HBox(SPACE * 4);

        // Boxes in the top row (scrollable)
        applicationBox = createBox(middleBox, APP_SCREEN_WIDTH, MIDDLE_CONTENT_HEIGHT, "APP SCREEN");
        StackPane moduleGraphBox = createBox(middleBox, GRAPH_WIDTH, MIDDLE_CONTENT_HEIGHT, "LIB");
        StackPane projectStructureBox = createBox(middleBox, TREE_WIDTH, MIDDLE_CONTENT_HEIGHT, "REPOSITORY");
        StackPane editorsBox = createBox(middleBox, EDITOR_WIDTH, MIDDLE_CONTENT_HEIGHT, "EDITOR");

        // Boxes in the bottom row
        StackPane toolsBox = createBox(bottomBox, TOOLS_WIDTH, 0, null);
        StackPane terminalBox = createBox(bottomBox, TERMINAL_WIDTH, 0, null);

        StackPane slideView = new StackPane();
        slideView.setPadding(new Insets(80));
        slideView.setPrefWidth(WIDTH);
        slideView.setVisible(false);

        ToolsGrid toolsGrid = new ToolsGrid(toolsBox);
        Terminal terminal = new Terminal(terminalBox, slideView);

        SlideControl slideControl = new SlideControl(stage,
                new ApplicationScreen(applicationBox),
                new ModuleGraph(moduleGraphBox, JavarcadeProject.modules()),
                new ProjectTree(projectStructureBox, APP_ROOT_FOLDER),
                new Editor(editorsBox, APP_ROOT_FOLDER.getParent(), ASSET_LOCATION),
                terminal,
                toolsGrid,
                new TopicList(topicsBox, JavarcadeProject.topics(), slideView)
        );

        var root = new StackPane(topicsBox, middleScrollPane, bottomBox);
        root.setPrefHeight(HEIGHT);

        StackPane.setMargin(middleScrollPane, new Insets(MIDDLE_START, 0, 0, 0));
        StackPane.setMargin(bottomBox, new Insets(BOTTOM_START, 0, 0, 0));

        middleBox.setPadding(new Insets(5, SPACE * 3.5, 0, SPACE * 3.5));
        bottomBox.setPadding(new Insets(SPACE, SPACE * 10, 2 * SPACE, SPACE * 10));

        UI.mainBG(root);
        UI.topicsBG(topicsBox);
        UI.windowsBG(middleBox);
        UI.terminalBG(bottomBox);
        UI.slideBG(slideView);

        Scene scene = scalableScene(root, slideView);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.X) {
                if (slideView.isVisible()) {
                    slideView.setVisible(false);
                } else {
                    slideControl.toggleRogueMode(toolsGrid, terminal);
                }
            }
        });
        slideView.setOnMouseClicked(e -> slideView.setVisible(false));
        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private Scene scalableScene(Pane root, StackPane slideView) {
        GaussianBlur blur = new GaussianBlur(20);
        slideView.visibleProperty().addListener((obs, wasVisible, isNowVisible) -> {
            root.setEffect(isNowVisible ? blur : null);
        });
        StackPane pane = new StackPane(root, slideView);
        Group scalableGroup = new Group(pane);
        Scene scene = new Scene(scalableGroup, WIDTH, HEIGHT, Color.BLACK);
        Scale scale = new Scale(1, 1, 0, 0);
        scalableGroup.getTransforms().add(scale);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateScaleAndPosition(scene, scale, scalableGroup));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateScaleAndPosition(scene, scale, scalableGroup));

        return scene;
    }

    private void updateScaleAndPosition(Scene scene, Scale scale, Group scalableGroup) {
        double scaleX = scene.getWidth() / WIDTH;
        double scaleY = scene.getHeight() / HEIGHT;
        double scaleFactor = Math.min(scaleX, scaleY);

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        // Center the content
        double offsetX = (scene.getWidth() - WIDTH * scaleFactor) / 2;
        double offsetY = (scene.getHeight() - HEIGHT * scaleFactor) / 2;
        scalableGroup.setTranslateX(offsetX);
        scalableGroup.setTranslateY(offsetY);

        if (applicationBox != null) {
            applicationBox.requestFocus(); // start on left
            applicationBox = null;
        }
    }

    private StackPane createBox(Pane parent, double width, double height, String label) {
        StackPane inner = new StackPane();
        inner.setPrefWidth(width);
        if (height > 0) {
            inner.setPrefHeight(height);
        }

        Rectangle rectangle = new Rectangle(width, height, Color.TRANSPARENT);
        StackPane stackPane = new StackPane(rectangle, inner);
        // stackPane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        if (label == null) {
            parent.getChildren().add(stackPane);
        } else {
            Text labelText = new Text(label);
            labelText.setFont(Font.font("Monospaced", FontWeight.BOLD, 24));
            labelText.setFill(Color.DARKKHAKI);
            VBox boxWithLabel = new VBox(SPACE * 0.5, labelText, stackPane);
            boxWithLabel.setAlignment(Pos.TOP_CENTER);
            parent.getChildren().add(boxWithLabel);
        }

        return inner;
    }
}
