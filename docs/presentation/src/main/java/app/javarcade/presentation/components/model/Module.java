package app.javarcade.presentation.components.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Set;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;

public record Module(String jarName, int columnIndex, int rowIndex, Set<String> dependencies, Pane icon) {

    public Module(String jarName, int columnIndex, int rowIndex, Set<String> dependencies) {
        this(jarName, columnIndex, rowIndex, dependencies, moduleCell(jarName));
    }

    public Module(String jarName, int columnIndex, int rowIndex) {
        this(jarName, columnIndex, rowIndex, Set.of(), moduleCell(jarName));
    }

    public Module(String jarName) {
        this(jarName, -1, -1, Set.of(), null);
    }

    private static StackPane moduleCell(String jarName) {
        String iconName = jarName.replace(".jar", "");
        if (iconName.contains(".")) { // external with version
            iconName = jarName.substring(0, jarName.indexOf('-'));
        }
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), "jar"));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth(160);
        iconView.setFitHeight(100);

        Text text = new Text(jarName.replace("-", "-\n").replace("-\n3.3.6-\n", "-3.3.6\n-").toUpperCase());
        text.setFont(Font.font("Monospaced", FontWeight.NORMAL, 14));
        text.setTextAlignment(TextAlignment.LEFT);
        text.setFont(new Font(14));
        text.setWrappingWidth(84);

        StackPane textWrapper = new StackPane(text);
        textWrapper.setPadding(new Insets(0, 0 , 22, 60));
        StackPane box = new StackPane(iconView, textWrapper);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }
}
