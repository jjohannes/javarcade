package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.ShellCommand;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.GRADLE;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.MAVEN;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public record ToolsGrid(ImageView jpmsButton,
                        ImageView gradleButton,
                        ImageView mavenButton,
                        ImageView renovateButton) {
    public ToolsGrid(StackPane box) {
        this(logoButton("java"), logoButton("gradle"), logoButton("maven"), logoButton("renovate"));

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(20);

        grid.add(jpmsButton, 0, 0);
        grid.add(renovateButton, 1, 0);
        grid.add(gradleButton, 0, 1);
        grid.add(mavenButton, 1, 1);

        box.getChildren().add(grid);
    }

    private static ImageView logoButton(String iconName) {
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitHeight(80);
        iconView.setPickOnBounds(true); // Enable clicks on transparent areas
        return iconView;
    }

    public void update(ShellCommand.Tool focusedTool, boolean moduleSystem, boolean rogue) {
        jpmsButton().setOpacity(moduleSystem ? 1 : 0.3);

        gradleButton().setOpacity(focusedTool == GRADLE ? 1 : 0.3);
        mavenButton().setOpacity(focusedTool == MAVEN ? 1 : 0.3);
        renovateButton().setOpacity(focusedTool == RENOVATE ? 1 : 0.3);

        mavenButton().setImage(new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"),
                rogue ? "maven-rogue" : "maven")));
    }
}
