package app.javarcade.presentation.components.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Set;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public record Module(String jarName, int columnIndex, int rowIndex, Set<String> dependencies, HBox icon) {

    public Module(String jarName, int columnIndex, int rowIndex, Set<String> dependencies) {
        this(jarName, columnIndex, rowIndex, dependencies, moduleCell(jarName));
    }

    public Module(String jarName, int columnIndex, int rowIndex) {
        this(jarName, columnIndex, rowIndex, Set.of(), moduleCell(jarName));
    }

    public Module(String jarName) {
        this(jarName, -1, -1, Set.of(), null);
    }

    private static HBox moduleCell(String jarName) {
        String iconName = jarName.replace(".jar", "");
        if (iconName.contains(".")) { // external with version
            iconName = jarName.substring(0, jarName.indexOf('-'));
        }
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION, iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(100);
        iconView.setFitHeight(100);

        Text text = new Text(jarName.replace("-", "-\n"));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font(24));

        HBox box = new HBox(iconView, text);
        box.setPrefWidth(140);
        box.setPrefHeight(140);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
