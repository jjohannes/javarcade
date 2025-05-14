package app.javarcade.presentation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class App extends Application {


    public static final int RATIO = 2;

    public static final int WIDTH = 1920 / RATIO;
    public static final int HEIGHT = 1080 / RATIO;
    public static final int SCREEN_DIM = 720 / RATIO;

    public static final int SPACE = 40 / RATIO;

    private SlideControl slideControl;

    @Override
    public void start(Stage stage) {
        HBox topBox = new HBox(SPACE);
        HBox bottomBox = new HBox(SPACE);
        topBox.setPadding(new Insets(SPACE));
        bottomBox.setPadding(new Insets(0, SPACE, SPACE, SPACE));
        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setBottom(bottomBox);

        StackPane topLeftBox = createBox(topBox, SCREEN_DIM, SCREEN_DIM);
        StackPane topRightBox = createBox(topBox, WIDTH - SCREEN_DIM - SPACE*3, SCREEN_DIM);
        StackPane terminalBox = createBox(bottomBox, WIDTH - SPACE*2.0, HEIGHT - SCREEN_DIM - SPACE*3.0);

        slideControl = new SlideControl(imageView(topLeftBox), errorTextView(topLeftBox), textView(terminalBox), gridPane(topRightBox));

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> slideControl.prev();
                case RIGHT -> slideControl.next();
            }
        });

        stage.setTitle("Java Modularity");
        stage.setScene(scene);
        stage.show();
    }

    private StackPane createBox(HBox parent, double width, double height) {
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
        text.setFill(Color.DARKRED);

        TextFlow textFlow = new TextFlow(text);
        textFlow.setLineSpacing(10); // Adjust the value for desired spacing
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(textFlow);

        return text;
    }

    private Text textView(StackPane box) {
        Text terminalText = new Text();
        terminalText.setFont(Font.font("Monospaced", FontWeight.BOLD, 42 / RATIO));

        TextFlow textFlow = new TextFlow(terminalText);
        textFlow.setLineSpacing(10); // Adjust the value for desired spacing
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(textFlow);

        return terminalText;
    }

    private GridPane gridPane(StackPane box) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        box.getChildren().add(grid);
        return grid;
    }
}
