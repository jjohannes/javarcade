package app.javarcade.presentation;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static app.javarcade.presentation.App.DEPENDENCY_GRAPH;

public class SlideControl {
    private static final Path WORK_FOLDER =
            Path.of("/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/work");

    private static final Path APP_INSTALL_FOLDER =
            Path.of("/Users/jendrik/projects/gradle/howto/javarcade/apps/app-retro/build/install/app-retro/lib");

    private static final Path EXTRA_INSTALL_FOLDER =
            Path.of("/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/main/jars");

    private static final String RUN_MODULE_PATH_CMD =
            "java --module-path lib   --module app.javarcade.base.engine";
    private static final String RUN_CLASS_PATH_CMD =
            "java --class-path  lib/*   app.javarcade.base.engine.Engine";

    private final ImageView screen;
    private final Text screenError;
    private final List<Text> terminal;
    private final Map<String, HBox> jars;
    private final ProjectTree projectTree;
    private final Set<String> activeJars = new HashSet<>();

    private boolean graph = false;
    private boolean gradleNotMaven = true;
    private boolean moduleSystem = true;
    private boolean renovate = false;

    public SlideControl(ImageView screen, Text screenError, List<Text> terminal, Map<String, HBox> jars, ProjectTree projectTree) {
        this.screen = screen;
        this.screenError = screenError;
        this.terminal = terminal;
        this.jars = jars;
        this.projectTree = projectTree;

        terminal.get(0).setText(RUN_MODULE_PATH_CMD);
        terminal.get(1).setText(RUN_CLASS_PATH_CMD);

        terminal.forEach(cmd -> cmd.setOnMouseClicked(event -> execute(cmd)));
        jars.forEach((jar, cell) -> cell.setOnMouseClicked(event -> active(jar)));

        activeJars.add("lwjgl-glfw-3.3.6.jar");
        activeJars.add("lwjgl-glfw-3.3.6-natives-macos-arm64.jar");
        activeJars.add("lwjgl-opengl-3.3.6.jar");
        activeJars.add("lwjgl-opengl-3.3.6-natives-macos-arm64.jar");
        activeJars.add("lwjgl-stb-3.3.6.jar");
        activeJars.add("lwjgl-stb-3.3.6-natives-macos-arm64.jar");

        // TODO
        activeJars.add("base-model.jar");
        activeJars.add("base-engine.jar");
        activeJars.add("renderer-lwjgl.jar");
        activeJars.add("slf4j-api-2.0.17.jar");
        activeJars.add("slf4j-simple-2.0.17.jar");
        activeJars.add("lwjgl-3.3.6.jar");
        activeJars.add("lwjgl-3.3.6-natives-macos-arm64.jar");

        projectTree.jpmsButton().setOnMouseClicked(event -> {
            moduleSystem = !moduleSystem;
            updateTree();
        });
        projectTree.gradleButton().setOnMouseClicked(event -> {
            gradleNotMaven = true;
            updateTree();
        });
        projectTree.mavenButton().setOnMouseClicked(event -> {
            gradleNotMaven = false;
            updateTree();
        });
        projectTree.renovateButton().setOnMouseClicked(event -> {
            renovate = !renovate;
            updateTree();
        });

        updateTerminal(null);
        updateGrid();
        updateTree();
    }

    private void updateTerminal(Text active) {
        terminal.forEach(cmd -> cmd.setOpacity(active == cmd ? 1 : 0.3));
    }

    private void updateGrid() {
        jars.forEach((jar, cell) -> cell.setOpacity(activeJars.contains(jar) ? 1 : 0.3));
        if (graph) {
            drawGraph();
        }
    }

    private void active(String jar) {
        if (activeJars.contains(jar)) {
            activeJars.remove(jar);
        } else {
            activeJars.add(jar);
        }
        updateGrid();
        screen.setVisible(false);
        screenError.setText("");
        updateTerminal(null);
    }

    private void execute(Text cmd) {
        try {
            Path lib = WORK_FOLDER.resolve("lib");
            //noinspection resource
            for (Path file : Files.list(lib).toList()) {
                Files.deleteIfExists(file);
            }
            Files.deleteIfExists(WORK_FOLDER.resolve("out").resolve("screen.png"));
            Files.createDirectories(lib);
            for (String jar : activeJars) {
                if (Files.exists(APP_INSTALL_FOLDER.resolve(jar))) {
                    Files.copy(APP_INSTALL_FOLDER.resolve(jar), lib.resolve(jar));
                } else {
                    Files.copy(EXTRA_INSTALL_FOLDER.resolve(jar), lib.resolve(jar));
                }
            }

            updateTerminal(cmd);

            var p = Runtime.getRuntime().exec(
                    cmd.getText().split("\\s+"),
                    new String[] {
                            "PATH=" + System.getenv("PATH"),
                            "PRESENTATION_FOLDER=out"
                    },
                    WORK_FOLDER.toFile());
            p.waitFor();

            String output = new String(p.getInputStream().readAllBytes());
            String error = new String(p.getErrorStream().readAllBytes());
            System.out.println(output);
            System.out.println(error);

            screenError.setText(trimError(output, error));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        loadImage(WORK_FOLDER);
    }

    private String trimError(String output, String error) {
        String all = output + error;
        Optional<String> noClassDefFound = Arrays.stream(all.split("\n")).filter(l -> l.contains("NoClassDefFoundError")).findFirst();
        Optional<String> moduleFindException = Arrays.stream(all.split("\n")).filter(l -> l.contains("FindException")).findFirst();

        return noClassDefFound.orElse(moduleFindException.orElse(all.trim()));
    }

    private void loadImage(Path workFolder) {
        var imageFile = workFolder.resolve("out/screen.png").toFile();
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            screen.setImage(image);
            screen.setVisible(true);
        } else {
            screen.setVisible(false);
        }
    }

    private void drawGraph() {
        StackPane parent = (StackPane) jars.values().stream().findFirst().get().getParent().getParent();
        Pane overlay = (Pane) parent.getChildren().get(1);
        overlay.getChildren().clear();

        DEPENDENCY_GRAPH.keySet().stream().filter(activeJars::contains).forEach(from -> {
            HBox source = jars.get(from);
            Bounds srcBounds = source.localToScene(source.getBoundsInLocal());
            Point2D srcCenter = new Point2D(srcBounds.getMinX() + srcBounds.getWidth() / 2, srcBounds.getMinY() + srcBounds.getHeight() / 2);
            DEPENDENCY_GRAPH.get(from).stream().filter(activeJars::contains).forEach(to -> {
                HBox target = jars.get(to);

                Bounds tgtBounds = target.localToScene(target.getBoundsInLocal());
                Point2D tgtCenter = new Point2D(tgtBounds.getMinX() + tgtBounds.getWidth() / 2, tgtBounds.getMinY() + tgtBounds.getHeight() / 2);

                Point2D tgtBorder = getBorderIntersection(tgtBounds, tgtCenter, srcCenter);

                Point2D srcBorder = getBorderIntersection(srcBounds, srcCenter, tgtCenter);
                Point2D srcOverlay = overlay.sceneToLocal(srcBorder);
                Point2D tgtOverlay = overlay.sceneToLocal(tgtBorder);
                // Draw line
                Line line = new Line(srcOverlay.getX(), srcOverlay.getY(), tgtOverlay.getX(), tgtOverlay.getY());
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                overlay.getChildren().add(line);
                // Draw arrowhead
                double angle = Math.atan2(tgtOverlay.getY() - srcOverlay.getY(), tgtOverlay.getX() - srcOverlay.getX());
                double arrowLength = 12, arrowAngle = Math.PI / 8;
                double x1 = tgtOverlay.getX() - arrowLength * Math.cos(angle - arrowAngle);
                double y1 = tgtOverlay.getY() - arrowLength * Math.sin(angle - arrowAngle);
                double x2 = tgtOverlay.getX() - arrowLength * Math.cos(angle + arrowAngle);
                double y2 = tgtOverlay.getY() - arrowLength * Math.sin(angle + arrowAngle);
                Polygon arrowHead = new Polygon(
                        tgtOverlay.getX(), tgtOverlay.getY(),
                        x1, y1,
                        x2, y2
                );
                arrowHead.setFill(Color.BLACK);
                overlay.getChildren().add(arrowHead);
            });
        });
    }

    private Point2D getBorderIntersection(Bounds bounds, Point2D from, Point2D to) {
        double cx = bounds.getMinX() + bounds.getWidth() / 2;
        double cy = bounds.getMinY() + bounds.getHeight() / 2;
        double dx = to.getX() - cx;
        double dy = to.getY() - cy;
        double scale = 0.5 / Math.max(Math.abs(dx) / bounds.getWidth(), Math.abs(dy) / bounds.getHeight());
        scale = Math.min(scale, 1.0); // Clamp to not overshoot
        return new Point2D(cx + dx * scale, cy + dy * scale);
    }

    private void updateTree() {
        if (gradleNotMaven) {
            projectTree.gradleButton().setOpacity(1);
            projectTree.mavenButton().setOpacity(0.3);
        } else {
            projectTree.gradleButton().setOpacity(0.3);
            projectTree.mavenButton().setOpacity(1);
        }
        projectTree.jpmsButton().setOpacity(moduleSystem ? 1 : 0.3);
        projectTree.renovateButton().setOpacity(renovate ? 1 : 0.3);
        projectTree.tree().forEach(this::updateTreeItem);
    }

    private void updateTreeItem(TreeItem<String> item) {
        item.setValue(updateTreeItemValue(item.getValue()));
    }

    private String updateTreeItemValue(String value) {
        if (gradleNotMaven) {
            if (value.equals("pom.xml")) {
                return "build.gradle.kts";
            }
            if (value.endsWith("/pom.xml")) {
                return value.replace("/pom.xml", ".gradle.kts");
            }
            if (value.equals(".mvn/config")) {
                return "gradle/plugins";
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
            if (value.equals("gradle/plugins")) {
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
