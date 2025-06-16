package app.javarcade.presentation.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
        slide.setBackground(layoutImage("slide.png"));
    }

    static void slideTitle(StackPane slide) {
        slide.setVisible(true);

        var welcome = new Text("Welcome");
        welcome.setFont(Font.font("monospace", FontWeight.BOLD, 64));
        var tools = new HBox(90, icon("java", false), icon("gradle", false), welcome, icon("maven", true), icon("renovate", true));
        tools.setAlignment(Pos.TOP_CENTER);

        var text = new Text("Unbeschwertes Dependency-Management für Java-Projekte");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(1200);
        text.setFont(Font.font("monospace", 92));

        Text author = new Text("Jendrik Johannes\n\njavarca.de");
        author.setTextAlignment(TextAlignment.LEFT);
        author.setFont(Font.font("monospace", 54));
        Image qr = new Image(("file:%s").formatted(ASSET_LOCATION.resolve("slides/qr.png")), 200, 200, true ,false);
        var bottom = new HBox(60, new ImageView(qr), author);

        var main = new StackPane(tools, text, bottom);
        StackPane.setMargin(text, new Insets(0, 0, 200, 0));
        StackPane.setMargin(bottom, new Insets(650, 0, 0, 100));

        slide.getChildren().clear();
        slide.getChildren().add(main);
    }

    static void slideRecipe(StackPane slide) {
        slide.setVisible(true);

        var title = new Text("Recipe for Carefree Dependency Management");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("monospace", 44));

        var text = new Text("""
                1. Decide for one method and define Dependencies between Modules in dedicated files
                   🧩 Java Module System: <module-folder>/src/main/java/module-info.java
                   🐘 Gradle:             <module-folder>/build.gradle.kts
                   🦉 Maven:              <module-folder>/pom.xml
                
                2. Version management in a dedicated file                     🚨 do not mix with 1 🚨
                   🐘 Gradle:  <bom-folder>/build.gradle.kts
                   🦉 Maven:   <bom-folder>/pom.xml
                3. Configure each build concern in dedicated files            🚨 do not mix with 1 🚨
                   🐘 Gradle:  <plugins-folder>/<build-concern>.gradle.kts (plugin composition)
                   🦉 Maven:   <configs-folder>/<build-concern>/pom.xml    (parent pom hierarchy)
                4. Conflict management in a dedicated file                    🚨 do not mix with 1 🚨
                   🐘 Gradle:  <plugins-folder>/dependency-rules.gradle.kts
                   🦉 Maven:   <configs-folder>/dependency-rules/pom.xml
                5. Configure dependency analysis and make it a regular check
                   🐘 Gradle:  <plugins-folder>/quality-check.gradle.kts
                   🦉 Maven:   <configs-folder>/quality-check/pom.xml
                6. Configure Renovate or Dependabot for automatic update PRs
                   🖌️ Renovate:   renovate.json
                   🤖 Dependabot: .github/dependabot.yml
                """);
        text.setTextAlignment(TextAlignment.LEFT);
        text.setWrappingWidth(1660);
        text.setFont(Font.font("monospace", 32));

        var main = new StackPane(title, text);
        main.setAlignment(Pos.TOP_CENTER);
        StackPane.setMargin(text, new Insets(160, 0, 0, 0));
        StackPane.setMargin(title, new Insets(20, 0, 0, 0));

        slide.getChildren().clear();
        slide.getChildren().add(main);
    }

    static void slideEnd(StackPane slide) {
        slide.setVisible(true);

        var thanks = new Text("Thank You");
        thanks.setFont(Font.font("monospace", FontWeight.BOLD, 64));

        var tools = new HBox(90, icon("java", false), icon("gradle", false), thanks, icon("maven", true), icon("renovate", true));
        tools.setAlignment(Pos.TOP_CENTER);

        Image qr = new Image(("file:%s").formatted(ASSET_LOCATION.resolve("slides/qr.png")), 300, 300, true ,false);

        Text socialLinks = new Text("""
                github.com/jjohannes
                
                mastodon.social/@jendrik
                
                linkedin.com/in/jendrikjohannes
                """);
        socialLinks.setFont(Font.font("monospace", 28));


        var grid = new GridPane(40, 40);
        grid.add(linkBox("Carefree Dependency Management Recipe", "recipe.png", "javarca.de", Color.ROSYBROWN, true), 0, 0);
        grid.add(linkBox("Understanding Gradle Video Series", "youtube.png", "youtube.com/@jjohannes", Color.DARKRED, true), 1, 0);
        grid.add(linkBox("A collection of high quality Gradle plugins", "gradlex.png", "gradle.org", Color.DARKGREEN, false), 0, 1);
        grid.add(linkBox("Gradle Training and Consulting", "onepiece.png", "onepiece.software", Color.DARKORANGE, true), 1, 1);

        grid.add(socialLinks, 2, 0);
        grid.add(new ImageView(qr), 2, 1);

        var main = new StackPane(tools, grid);
        StackPane.setMargin(grid, new Insets(150, 0, 0, 100));

        slide.getChildren().clear();
        slide.getChildren().add(main);
    }

    static void slideRenovate(StackPane slide) {
        slide.setVisible(true);


        var url = "file:%s".formatted(ASSET_LOCATION.resolve("slides/renovate.png"));

        ImageView image = new ImageView(url);
        image.setFitWidth(1300);
        image.setPreserveRatio(true);

        slide.getChildren().clear();
        slide.getChildren().add(image);
    }

    private static Pane linkBox(String label, String imageName, String url, Color color, boolean bold) {
        int width = 500;
        int height = 300;

        Rectangle rectangle = new Rectangle(width, height, Color.TRANSPARENT);

        Text labelText = new Text(label);
        if (bold) {
            labelText.setFont(Font.font(null, FontWeight.BOLD, 24));
        } else {
            labelText.setFont(Font.font(24));
        }
        labelText.setFill(color);

        Text urlText = new Text(url);
        urlText.setFont(Font.font("monospace", FontWeight.BOLD, 24));
        urlText.setFill(Color.BLACK);

        var image = new ImageView(new Image("file:%s/%s".formatted(ASSET_LOCATION.resolve("slides"), imageName)));
        image.setFitWidth(420);
        image.setPreserveRatio(true);
        Rectangle space = new Rectangle(10, 20);
        space.setFill(Color.TRANSPARENT);
        Rectangle space2 = new Rectangle(10, 34);
        space2.setFill(Color.TRANSPARENT);

        var content = new VBox(image, space, labelText, space2, urlText);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);

        StackPane stackPane = new StackPane(rectangle, content);
        stackPane.setStyle("-fx-border-color: darkgray; -fx-border-width: 4; -fx-border-radius: 10; -fx-background-radius: 10;");

        return stackPane;
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

    private static ImageView icon(String name, boolean flip) {
        ImageView icon = new ImageView(new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), name)));
        icon.setFitHeight(80);
        icon.setPreserveRatio(true);
        icon.setScaleX(flip ? -1 : 1);
        return icon;
    }
}
