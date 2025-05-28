package app.javarcade.presentation.components;

import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.ui.UI.SPACE;

public record ProjectTree(TreeView<String> projectTree,
                          TreeView<String> jarTree,
                          Set<TreeItem<String>> items,
                          ImageView jarButton,
                          ImageView jpmsButton,
                          ImageView gradleButton,
                          ImageView mavenButton,
                          ImageView renovateButton) {
    
    public ProjectTree(StackPane box, Path projectLocation) {
        this(new TreeView<>(), new TreeView<>(), new HashSet<>(), logoButton("commons"), logoButton("jpms"), logoButton("gradle"), logoButton("maven"), logoButton("renovate"));

        projectTree().setRoot(buildProjectTree(projectLocation));
        projectTree().setShowRoot(true);
        projectTree().setStyle("-fx-font-size: 24px;");
        projectTree().setVisible(false);

        jarTree().setRoot(buildJarTree());
        jarTree().setShowRoot(true);
        jarTree().setStyle("-fx-font-size: 24px;");

        updateButtonVisibility();
        jarButton().setOnMouseClicked(event -> {
            jarTree().setVisible(!jarTree().isVisible());
            projectTree().setVisible(!projectTree().isVisible());
            updateButtonVisibility();
        });

        HBox menuBar = new HBox(jarButton(), jpmsButton(), gradleButton(), mavenButton(), renovateButton());
        menuBar.setSpacing(SPACE * 3);
        menuBar.setPadding(new Insets(SPACE));
        VBox.setVgrow(projectTree(), Priority.ALWAYS);
        VBox container = new VBox(menuBar, new StackPane(jarTree(), projectTree()));

        box.getChildren().add(container);
    }

    private void updateButtonVisibility() {
        jarButton().setOpacity(jarTree().isVisible() ? 1 : 0.3);

        jpmsButton().setVisible(projectTree().isVisible());
        gradleButton().setVisible(projectTree().isVisible());
        mavenButton().setVisible(projectTree().isVisible());
        renovateButton().setVisible(projectTree().isVisible());
    }

    private TreeItem<String> buildJarTree() {
        TreeItem<String> repository = newItem("https://repo1.maven.org/maven2");
        repository.setExpanded(true);
        TreeItem<String> folder = newItem("org/apache/commons/commons-csv/1.14.0");
        folder.setExpanded(true);
        repository.getChildren().add(folder);

        TreeItem<String> pom = newItem("commons-csv-1.14.0.pom");
        TreeItem<String> jar = newItem("commons-csv-1.14.0.jar");
        TreeItem<String> javaPackage = newItem("org/apache/commons/csv/*.class");
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

    private static ImageView logoButton(String iconName) {
        Image icon = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), iconName));
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitHeight(60);
        iconView.setPickOnBounds(true); // Enable clicks on transparent areas
        return iconView;
    }

    private TreeItem<String> newItem(String name) {
        TreeItem<String> item = new TreeItem<>(name);
        items().add(item);
        return item;
    }

    public void update(boolean gradleNotMaven, boolean moduleSystem, boolean renovate) {
        if (gradleNotMaven) {
            gradleButton().setOpacity(1);
            mavenButton().setOpacity(0.3);
        } else {
            gradleButton().setOpacity(0.3);
            mavenButton().setOpacity(1);
        }
        jpmsButton().setOpacity(moduleSystem ? 1 : 0.3);
        renovateButton().setOpacity(renovate ? 1 : 0.3);
        items().forEach(item -> item.setValue(updateTreeItemValue(item.getValue(), gradleNotMaven, moduleSystem, renovate)));
    }

    private String updateTreeItemValue(String value, boolean gradleNotMaven, boolean moduleSystem, boolean renovate) {
        if (gradleNotMaven) {
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
        } else {
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
            return moduleSystem ? "src/main/java/module-info.java" : "src/main/java/...";
        }

        if (value.equals("renovate.json") || value.isEmpty()) {
            return renovate ? "renovate.json" : "";
        }

        return value;
    }
}
