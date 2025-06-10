package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.ShellCommand;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.GRADLE;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.JAVA;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.MAVEN;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.applyScrollPaneStyle;

public record ProjectTree(TreeView<String> projectTree,
                          TreeView<String> jarTree,
                          Set<TreeItem<String>> items,
                          StackPane container) {
    
    public ProjectTree(StackPane box, Path projectLocation) {
        this(applyScrollPaneStyle(new TreeView<>()), applyScrollPaneStyle(new TreeView<>()), new HashSet<>(), box);

        projectTree().setRoot(buildProjectTree(projectLocation));
        projectTree().setShowRoot(true);
        projectTree().heightProperty().addListener((observable, oldValue, newValue) -> {
            projectTree().lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0));
        });

        jarTree().setRoot(buildJarTree());
        jarTree().setShowRoot(true);
        jarTree().heightProperty().addListener((observable, oldValue, newValue) -> {
            projectTree().lookupAll(".scroll-bar").forEach(bar -> bar.setOpacity(0));
        });
    }

    private TreeItem<String> buildJarTree() {
        TreeItem<String> repository = newItem("https://repo1.maven.org/maven2", "folder");
        TreeItem<String> folder = newItem("org/apache/commons/commons-csv/1.14.0", "folder");
        repository.getChildren().add(folder);

        TreeItem<String> pom = newItem("commons-csv-1.14.0.pom", "text");
        TreeItem<String> jar = newItem("commons-csv-1.14.0.jar", "jar");
        TreeItem<String> javaPackage = newItem("org/apache/commons/csv/**/*.class", "binary");
        TreeItem<String> manifest = newItem("META-INF/MANIFEST.MF", "text");
        TreeItem<String> moduleInfo = newItem("module-info.class", "binary");

        jar.getChildren().add(javaPackage);
        jar.getChildren().add(manifest);
        jar.getChildren().add(moduleInfo);

        folder.getChildren().add(jar);
        folder.getChildren().add(pom);

        return repository;
    }

    private TreeItem<String> buildProjectTree(Path projectLocation) {
        TreeItem<String> rootItem = newItem(projectLocation.getFileName().toString(), "folder");

        TreeItem<String> buildToolConfig = newItem("gradle/plugins/src/main/kotlin", "folder"); // .mvn/config
        TreeItem<String> versions = newItem("gradle/versions", "folder"); // .mvn/versions

        TreeItem<String> repositories = newItem("repositories.gradle.kts"); // /pom.xml
        TreeItem<String> dependencyRules = newItem("dependency-rules.gradle.kts"); // /pom.xml
        TreeItem<String> compile = newItem("compile.gradle.kts"); // /pom.xml
        TreeItem<String> test = newItem("test.gradle.kts"); // /pom.xml
        TreeItem<String> qCheck = newItem("quality-check.gradle.kts"); // /pom.xml
        TreeItem<String> javaModule = newItem("java-module.gradle.kts"); // /pom.xml
        TreeItem<String> bom = newItem("build.gradle.kts");
        TreeItem<String> renovateJson = newItem("renovate.json");

        TreeItem<String> modules = newItem("modules", "folder");

        Path modulesFolder = projectLocation.resolve("modules");
        try(var l = Files.list(modulesFolder)) {
            l.filter(f -> !f.getFileName().toString().startsWith(".")).sorted().forEach(moduleFolder -> {
                TreeItem<String> module = newItem(moduleFolder.getFileName().toString(), "folder");

                TreeItem<String> moduleInfo = newItem("src/main/java/module-info.java", "java");
                TreeItem<String> buildFile = newItem("build.gradle.kts"); // pom.xml
                module.getChildren().add(moduleInfo);
                module.getChildren().add(buildFile);

                modules.getChildren().add(module);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        rootItem.getChildren().add(buildToolConfig);
        rootItem.getChildren().add(versions);
        rootItem.getChildren().add(modules);
        rootItem.getChildren().add(renovateJson);

        versions.getChildren().add(bom);

        buildToolConfig.getChildren().add(repositories);
        buildToolConfig.getChildren().add(dependencyRules);
        buildToolConfig.getChildren().add(qCheck);
        buildToolConfig.getChildren().add(compile);
        buildToolConfig.getChildren().add(test);
        buildToolConfig.getChildren().add(javaModule);
        return rootItem;
    }

    private TreeItem<String> newItem(String name, String icon) {
        TreeItem<String> item = new TreeItem<>(name);
        item.setGraphic(icon(icon));
        items().add(item);
        return item;
    }

    private TreeItem<String> newItem(String name) {
        TreeItem<String> item = new TreeItem<>(name);
        items().add(item);
        return item;
    }

    public void update(ShellCommand.Tool focusedTool, boolean moduleSystem) {
        container.getChildren().clear();
        container.getChildren().add(focusedTool == JAVA ? jarTree() : projectTree());

        items().forEach(item -> item.setValue(updateTreeItemValue(item.getValue(), focusedTool, moduleSystem)));
        items().forEach(item -> item.setGraphic(updateTreeItemIcon(item.getGraphic(), item.getValue())));
    }

    private Node updateTreeItemIcon(Node graphic, String updatedValue) {
        if (updatedValue.endsWith(".gradle.kts")) {
            return icon("gradle");
        }
        if (updatedValue.endsWith("pom.xml")) {
            return icon("maven");
        }
        if (updatedValue.equals("renovate.json")) {
            return icon("renovate");
        }
        if (updatedValue.isEmpty()) {
            return null;
        }
        return graphic;
    }

    private String updateTreeItemValue(String value, ShellCommand.Tool focusedTool, boolean moduleSystem) {
        if (focusedTool == GRADLE) {
            if (value.equals("pom.xml")) {
                return "build.gradle.kts";
            }
            if (value.endsWith("/pom.xml")) {
                return value.replace("/pom.xml", ".gradle.kts");
            }
            if (value.equals(".mvn/config")) {
                return "gradle/plugins/src/main/kotlin";
            }
            if (value.equals(".mvn/versions")) {
                return "gradle/versions";
            }
        }
        if (focusedTool == MAVEN) {
            if (value.equals("build.gradle.kts")) {
                return "pom.xml";
            }
            if (value.endsWith(".gradle.kts")) {
                return value.replace(".gradle.kts", "/pom.xml");
            }
            if (value.equals("gradle/plugins/src/main/kotlin")) {
                return ".mvn/config";
            }
            if (value.equals("gradle/versions")) {
                return ".mvn/versions";
            }
        }

        if (value.startsWith("src/main/java")) {
            return moduleSystem ? "src/main/java/module-info.java" : "src/main/java/**/*.java";
        }

        if (value.equals("renovate.json") || value.isEmpty()) {
            return focusedTool == RENOVATE ? "renovate.json" : "";
        }

        return value;
    }

    private ImageView icon(String iconName) {
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("treeicons"), iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitHeight(24);
        return iconView;
    }
}
