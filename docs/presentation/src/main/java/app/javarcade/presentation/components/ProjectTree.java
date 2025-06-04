package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.ShellCommand;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

public record ProjectTree(TreeView<String> projectTree,
                          TreeView<String> jarTree,
                          Set<TreeItem<String>> items,
                          StackPane container) {
    
    public ProjectTree(StackPane box, Path projectLocation) {
        this(new TreeView<>(), new TreeView<>(), new HashSet<>(), box);

        projectTree().setRoot(buildProjectTree(projectLocation));
        projectTree().setShowRoot(true);
        projectTree().setStyle("-fx-font-size: 24px;");

        jarTree().setRoot(buildJarTree());
        jarTree().setShowRoot(true);
        jarTree().setStyle("-fx-font-size: 24px;");

        box.getChildren().add(jarTree());
    }

    private TreeItem<String> buildJarTree() {
        TreeItem<String> repository = newItem("https://repo1.maven.org/maven2");
        repository.setExpanded(true);
        TreeItem<String> folder = newItem("org/apache/commons/commons-csv/1.14.0");
        repository.getChildren().add(folder);

        TreeItem<String> pom = newItem("commons-csv-1.14.0.pom");
        TreeItem<String> jar = newItem("commons-csv-1.14.0.jar");
        TreeItem<String> javaPackage = newItem("org/apache/commons/csv/**/*.class");
        TreeItem<String> manifest = newItem("META-INF/MANIFEST.MF");
        TreeItem<String> moduleInfo = newItem("module-info.class");

        jar.getChildren().add(javaPackage);
        jar.getChildren().add(manifest);
        jar.getChildren().add(moduleInfo);

        folder.getChildren().add(jar);
        folder.getChildren().add(pom);

        return repository;
    }

    private TreeItem<String> buildProjectTree(Path projectLocation) {
        TreeItem<String> rootItem = newItem(projectLocation.getFileName().toString());
        rootItem.setExpanded(true);

        TreeItem<String> buildToolConfig = newItem("gradle/plugins/src/main/kotlin"); // .mvn/config
        TreeItem<String> versions = newItem("gradle/versions"); // .mvn/versions

        TreeItem<String> repositories = newItem("repositories.gradle.kts"); // /pom.xml
        TreeItem<String> dependencyRules = newItem("dependency-rules.gradle.kts"); // /pom.xml
        TreeItem<String> javaModule = newItem("java-module.gradle.kts"); // /pom.xml
        TreeItem<String> bom = newItem("build.gradle.kts");
        TreeItem<String> renovateJson = newItem("renovate.json");

        TreeItem<String> modules = newItem("modules");

        Path modulesFolder = projectLocation.resolve("modules");
        try(var l = Files.list(modulesFolder)) {
            l.filter(f -> !f.getFileName().toString().startsWith(".")).sorted().forEach(moduleFolder -> {
                TreeItem<String> module = newItem(moduleFolder.getFileName().toString());

                TreeItem<String> moduleInfo = newItem("src/main/java/module-info.java");
                TreeItem<String> buildFile = newItem("build.gradle.kts"); // pom.xml
                module.getChildren().add(moduleInfo);
                module.getChildren().add(buildFile);

                modules.getChildren().add(module);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Image icon = new Image("file:/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/main/base-model.png");
        // ImageView iconView = new ImageView(icon);
        // iconView.setFitHeight(16);
        // iconView.setFitWidth(16);

        rootItem.getChildren().add(buildToolConfig);
        rootItem.getChildren().add(versions);
        rootItem.getChildren().add(modules);
        rootItem.getChildren().add(renovateJson);

        versions.getChildren().add(bom);

        buildToolConfig.getChildren().add(repositories);
        buildToolConfig.getChildren().add(dependencyRules);
        buildToolConfig.getChildren().add(javaModule);
        return rootItem;
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
}
