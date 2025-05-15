package app.javarcade.presentation;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {


    public static final int RATIO = 2;

    public static final int WIDTH = 1920 / RATIO;
    public static final int HEIGHT = 1080 / RATIO;
    public static final int SCREEN_DIM = 720 / RATIO;
    public static final int WARNING_DIM = 610 / RATIO;

    public static final int SPACE = 20 / RATIO;

    static final Map<String, List<String>> DEPENDENCY_GRAPH = Map.ofEntries(
            Map.entry("base-model.jar", List.of()),
            Map.entry("base-engine.jar", List.of("base-model.jar", "slf4j-api-2.0.17.jar", "slf4j-simple-2.0.17.jar")),
            Map.entry("classic-assets.jar", List.of("base-model.jar", "commons-io-2.18.0.jar")),
            Map.entry("classic-levels.jar", List.of("base-model.jar")),
            Map.entry("classic-items.jar", List.of("base-model.jar", "commons-csv-1.14.0.jar")),
            Map.entry("renderer-lwjgl.jar", List.of("base-engine.jar", "slf4j-api-2.0.17.jar", "slf4j-jdk14-2.0.17.jar", "lwjgl-3.3.6.jar", "lwjgl-3.3.6-natives-macos-arm64.jar", "lwjgl-3.3.6-natives-windows-x86.jar")),
            Map.entry("slf4j-api-2.0.17.jar", List.of("")),
            Map.entry("slf4j-simple-2.0.17.jar", List.of("slf4j-api-2.0.17")),
            Map.entry("slf4j-jdk14-2.0.17.jar", List.of("slf4j-api-2.0.17")),
            Map.entry("commons-io-2.18.0.jar", List.of("")),
            Map.entry("commons-csv-1.14.0.jar", List.of("commons-io-2.18.0.jar", "commons-codec-1.18.0.jar")),
            Map.entry("commons-codec-1.18.0.jar", List.of("")),
            Map.entry("commons-io-2.16.1.jar", List.of("")),
            Map.entry("lwjgl-3.3.6.jar", List.of("")),
            Map.entry("lwjgl-3.3.6-natives-macos-arm64.jar", List.of("")),
            Map.entry("lwjgl-3.3.6-natives-windows-x86.jar", List.of(""))
    );

    private SlideControl slideControl;
    private SlideShow slideShow;

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
        StackPane dependencyGraph = createBox(topBox, WIDTH - SCREEN_DIM - SPACE * 6, SCREEN_DIM);
        StackPane projectStructure = createBox(topBox, SCREEN_DIM, SCREEN_DIM);

        StackPane terminalBox = createBox(bottomBox, WIDTH - WARNING_DIM, HEIGHT - SCREEN_DIM - SPACE * 4.0);
        StackPane warningBox = createBox(bottomBox, WARNING_DIM - SPACE * 4, HEIGHT - SCREEN_DIM - SPACE * 4.0);

        warningGrid(warningBox);

        // topBox.setTranslateX(-SCREEN_DIM - SPACE);
        showProjectStructure(projectStructure);

        slideControl = new SlideControl(imageView(runningApp), errorTextView(runningApp), terminalView(terminalBox), gridPane(dependencyGraph));
        slideShow = new SlideShow();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> slideShow.prev();
                case RIGHT -> slideShow.next();
            }
        });

        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.show();
    }

    private void warningGrid(StackPane warningBox) {
        GridPane grid = new GridPane();
        grid.setHgap(10 / RATIO);
        grid.setVgap(10 / RATIO);

        grid.add(warningText("Dependency Definition"), 0, 0);
        grid.add(warningText("Module Version Management"), 1, 0);
        grid.add(warningText("Retrieving JARs"), 2, 0);

        grid.add(warningText("Version Conflict Management"), 0, 1);
        grid.add(warningText("Variant Conflict Management"), 1, 1);
        grid.add(warningText("Version Update Management"), 2, 1);

        warningBox.getChildren().add(grid);
    }

    private Node warningText(String title) {
        Text text = new Text(title);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(150 / RATIO); // Enable wrapping

        StackPane pane = new StackPane(text);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(SPACE / 4));
        pane.setMinHeight(124 / RATIO);

        pane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4;");

        return pane;
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


    private ImageView imageView(StackPane box) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(box.getPrefWidth());
        imageView.setFitHeight(box.getPrefHeight());
        box.getChildren().add(imageView);
        return imageView;
    }

    private Text errorTextView(StackPane box) {
        Text text = new Text();
        text.setFont(Font.font("Monospaced", FontWeight.BOLD, 32 / RATIO));
        text.setFill(Color.WHITE);
        Rectangle background = new Rectangle();
        background.setFill(Color.web("#FF7F7FBB"));
        background.widthProperty().bind(text.layoutBoundsProperty().map(Bounds::getWidth));
        background.heightProperty().bind(text.layoutBoundsProperty().map(Bounds::getHeight));

        TextFlow textFlow = new TextFlow(text);
        box.setAlignment(Pos.TOP_LEFT);
        box.getChildren().add(background);
        box.getChildren().add(textFlow);

        return text;
    }

    private List<Text> terminalView(StackPane box) {
        Text terminalCmd1 = new Text();
        terminalCmd1.setFont(Font.font("Monospaced", FontWeight.BOLD, 36 / RATIO));
        Text terminalCmd2 = new Text();
        terminalCmd2.setFont(Font.font("Monospaced", FontWeight.BOLD, 36 / RATIO));

        TextFlow textFlow1 = new TextFlow(terminalCmd1);
        TextFlow textFlow2 = new TextFlow(terminalCmd2);

        VBox vBox = new VBox(textFlow1, textFlow2);
        vBox.setSpacing(SPACE / 2);
        vBox.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().add(vBox);

        return List.of(terminalCmd1, terminalCmd2);
    }

    private Map<String, HBox> gridPane(StackPane box) {
        GridPane grid = new GridPane();
        Pane overlay = new Pane();
        grid.setHgap(10);
        grid.setVgap(10);
        overlay.setPickOnBounds(false); // Let mouse events pass through
        box.getChildren().add(new StackPane(grid, overlay));

        Map<String, HBox> jarCells = new HashMap<>();

        grid.add(jarCell("base-model.jar", jarCells), 1, 0);
        grid.add(jarCell("base-engine.jar", jarCells), 3, 0);

        grid.add(jarCell("classic-assets.jar", jarCells), 0, 1);
        grid.add(jarCell("classic-levels.jar", jarCells), 1, 1);
        grid.add(jarCell("classic-items.jar", jarCells), 2, 1);
        grid.add(jarCell("renderer-lwjgl.jar", jarCells), 3, 1);

        grid.add(jarCell("slf4j-api-2.0.17.jar", jarCells), 0, 2);
        grid.add(jarCell("slf4j-simple-2.0.17.jar", jarCells), 1, 2);
        grid.add(jarCell("slf4j-jdk14-2.0.17.jar", jarCells), 2, 2);

        grid.add(jarCell("commons-io-2.18.0.jar", jarCells), 0, 3);
        grid.add(jarCell("commons-csv-1.14.0.jar", jarCells), 1, 3);
        grid.add(jarCell("commons-codec-1.18.0.jar", jarCells), 2, 3);
        grid.add(jarCell("commons-io-2.16.1.jar", jarCells), 3, 3);

        grid.add(jarCell("lwjgl-3.3.6.jar", jarCells), 3, 4);
        grid.add(jarCell("lwjgl-3.3.6-natives-macos-arm64.jar", jarCells), 2, 4);
        grid.add(jarCell("lwjgl-3.3.6-natives-windows-x86.jar", jarCells), 1, 4);

        return jarCells;
    }

    private Node jarCell(String jarName, Map<String, HBox> jarCells) {
        String iconName = jarName.replace(".jar", "");
        if (iconName.contains(".")) { // external with version
            iconName = jarName.substring(0, jarName.indexOf('-'));
        }
        Image icon = new Image("file:/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/main/%s.png".formatted(iconName));

        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(50);
        iconView.setFitHeight(50);
        Text text = new Text(jarName.replace("-", "-\n"));
        text.setTextAlignment(TextAlignment.CENTER);
        HBox box = new HBox(iconView, text);
        box.setPrefWidth(140 / RATIO);
        box.setPrefHeight(140 / RATIO);
        box.setAlignment(Pos.CENTER);

        jarCells.put(jarName, box);

        return box;
    }

    private void showProjectStructure(StackPane projectStructure) {
        TreeItem<String> rootItem = new TreeItem<>("javarcade");
        TreeItem<String> buildTool = new TreeItem<>("gradle");
        TreeItem<String> modules = new TreeItem<>("modules");
        rootItem.setExpanded(true);

        TreeItem<String> model = new TreeItem<>("base-model");
        TreeItem<String> engine = new TreeItem<>("base-engine");
        TreeItem<String> renderer = new TreeItem<>("renderer-lwjgl");
        TreeItem<String> assets = new TreeItem<>("classic-assets");
        TreeItem<String> levels = new TreeItem<>("classic-levels");
        TreeItem<String> items = new TreeItem<>("classic-items");

        // Build the tree structure
        rootItem.getChildren().add(buildTool);
        rootItem.getChildren().add(modules);

        modules.getChildren().add(model);
        modules.getChildren().add(engine);
        modules.getChildren().add(renderer);
        modules.getChildren().add(assets);
        modules.getChildren().add(levels);
        modules.getChildren().add(items);

        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(true);

        projectStructure.getChildren().add(treeView);
    }
}
