package app.javarcade.presentation.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static app.javarcade.presentation.data.JavarcadeProject.NO_MODULE_PROJECT_SUFFIX;

public record Editor(TextArea content, Path projectContainer, Path projectWithoutModulesContainer) {

    public Editor(StackPane box, Path projectContainer, Path projectWithoutModulesContainer) {
        this(new TextArea(), projectContainer, projectWithoutModulesContainer);

        content().setFont(Font.font("Monospaced", FontWeight.BOLD, 24));
        ScrollPane scrollPane = new ScrollPane(content());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        box.getChildren().add(scrollPane);
    }

    public void open(TreeItem<String> item, boolean modules) {
        if (item == null) {
            reset();
            return;
        }

        Path location = location(item, modules);

        if (Files.isDirectory(location) || !Files.exists(location)) {
            reset();
            return;
        }

        content().setText(readFile(location));
    }

    public void save(TreeItem<String> item, boolean modules) {
        if (item == null) {
            return;
        }

        Path location = location(item, modules);

        if (Files.isDirectory(location) || !Files.exists(location)) {
            return;
        }

        writeFile(location);
    }

    private Path location(TreeItem<String> item, boolean modules) {
        Path pathInProject = parentPath(item, Path.of(item.getValue()), modules);
        return projectWithoutModulesContainer.resolve(pathInProject).toFile().exists()
                ? projectWithoutModulesContainer.resolve(pathInProject)
                : projectContainer.resolve(pathInProject);
    }

    private Path parentPath(TreeItem<String> item, Path path, boolean modules) {
        TreeItem<String> parent = item.getParent();
        if (parent == null) {
            return path;
        }
        String myName = path.subpath(0, 1).toString();
        String parentName;
        if (!modules && myName.equals("modules")) {
            parentName = parent.getValue() + NO_MODULE_PROJECT_SUFFIX;
        } else {
            parentName = parent.getValue().replace("https://", "");
        }

        return parentPath(parent, Path.of(parentName).resolve(path), modules);
    }

    private void reset() {
        content().setText("");
    }

    private String readFile(Path location) {
        try {
            var module = location.getParent().getFileName().toString();
            return Files.readString(location)
                    .replace("<relativePath>../../../../../../../../.mvn/config/java-module</relativePath>", "<relativePath>../../.mvn/config/java-module</relativePath>")
                    .replace("<build><sourceDirectory>../../../../../../../../modules/%s/src/main/java</sourceDirectory></build></project>".formatted(module), "</project>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(Path location) {
        var newContent = content().getText();
        if (location.toString().contains("/javarcade-no-modules/")) {
            if (location.getParent().getParent().getFileName().toString().equals("modules")) {
                var module = location.getParent().getFileName().toString();
                newContent = newContent
                        .replace("<relativePath>../../.mvn/config/java-module</relativePath>", "<relativePath>../../../../../../../../.mvn/config/java-module</relativePath>")
                        .replace("</project>", "<build><sourceDirectory>../../../../../../../../modules/%s/src/main/java</sourceDirectory></build></project>".formatted(module));
            }
        }

        try {
            Files.writeString(location, newContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
