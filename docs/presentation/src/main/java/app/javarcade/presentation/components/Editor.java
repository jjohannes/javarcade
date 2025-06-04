package app.javarcade.presentation.components;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static app.javarcade.presentation.components.SharedComponents.applyScrollPaneStyleMono;
import static app.javarcade.presentation.data.JavarcadeProject.APP_MODULES_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.APP_NO_MODULES_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.APP_ROOT_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.data.JavarcadeProject.NO_MODULE_PROJECT_SUFFIX;

public record Editor(TextArea content, Path projectContainer, Path projectWithoutModulesContainer, ImageView nuke) {

    public Editor(StackPane box, Path projectContainer, Path projectWithoutModulesContainer) {
        this(applyScrollPaneStyleMono(new TextArea()), projectContainer, projectWithoutModulesContainer, nukeButton());

        nuke.setOnMouseClicked(event -> resetAll());

        box.setAlignment(Pos.TOP_RIGHT);
        box.getChildren().addAll(content, nuke);
        resetAll();
    }

    private static ImageView nukeButton() {
        Image image = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), "nuke"));
        ImageView nuke = new ImageView(image);
        nuke.setFitHeight(25);
        nuke.setFitWidth(25);
        nuke.setVisible(false);
        return nuke;
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

        nuke.setVisible(true);
    }

    private void resetAll() {
        nuke.setVisible(false);
        gitCheckout(APP_MODULES_FOLDER);
        gitCheckout(APP_ROOT_FOLDER.resolve("gradle"));
        gitCheckout(APP_ROOT_FOLDER.resolve(".mvn"));
        gitCheckout(APP_NO_MODULES_FOLDER);
        reset();
    }

    private void gitCheckout(Path appModulesFolder) {
        try {
            Runtime.getRuntime().exec(new String[]{"git", "checkout", "*"}, null, appModulesFolder.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
