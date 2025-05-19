package app.javarcade.presentation.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public record Editors(Text top, Path projectContainer) {
    public Editors(StackPane box, Path projectLocation) {
        this(new Text(), projectLocation);

        top().setFont(Font.font("Monospaced", FontWeight.BOLD, 24));
        TextFlow textFlow1 = new TextFlow(top());
        ScrollPane scrollPane = new ScrollPane(textFlow1);
        scrollPane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        box.getChildren().add(scrollPane);
    }

    public void open(TreeItem<String> item) {
        if (item == null) {
            reset();
            return;
        }

        Path location = projectContainer.resolve(parentPath(item, Path.of(item.getValue())));
        if (Files.isDirectory(location) || !Files.exists(location)) {
            reset();
            return;
        }

        top().setText(readFile(location));
    }

    private Path parentPath(TreeItem<String> item, Path path) {
        TreeItem<String> parent = item.getParent();
        if (parent == null) {
            return path;
        }
        return parentPath(parent, Path.of(parent.getValue()).resolve(path));
    }

    private void reset() {
        top().setText("");
    }

    private String readFile(Path location) {
        try {
            return Files.readAllLines(location).stream().dropWhile(l -> l.startsWith("import ") || l.isBlank()).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
