package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.ui.UI;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Set;

import static app.javarcade.presentation.ui.UI.SPACE;

public record ModuleGraph(Set<Module> modules, Text label) {

    public ModuleGraph(StackPane box, Set<Module> modules) {
        this(modules, (Text) box.getParent().getParent().getChildrenUnmodifiable().getFirst());

        GridPane grid = new GridPane();
        Pane overlay = new Pane();
        grid.setPadding(new Insets(SPACE * 0.8, 0, 0, SPACE * 3));
        grid.setHgap(10);
        grid.setVgap(25);
        overlay.setPickOnBounds(false); // mouse events pass through
        StackPane mainPane = new StackPane(grid, overlay);
        mainPane.setAlignment(Pos.TOP_LEFT);
        box.getChildren().add(mainPane);
        modules().forEach(module -> grid.add(module.icon(), module.columnIndex(), module.rowIndex()));
    }

    public Module get(String jarName) {
        return modules.stream().filter(m -> m.jarName().equals(jarName)).findFirst().orElseThrow();
    }

    public void update(Set<Module> activeModules, boolean graph) {
        modules.forEach(module -> module.icon().setOpacity(activeModules.contains(module) ? 1 : 0.3));

        Module anyModule = modules().stream().findFirst().orElseThrow();
        StackPane parent = (StackPane) anyModule.icon().getParent().getParent();
        Pane graphOverlay = (Pane) parent.getChildren().get(1);
        graphOverlay.getChildren().clear();

        if (graph) {
            dependencyGraph(activeModules, graphOverlay);
        }
    }

    private void dependencyGraph(Set<Module> activeModules, Pane overlay) {
        modules().stream().filter(activeModules::contains).forEach(from -> {
            Pane source = from.icon();
            Bounds srcBounds = source.localToScene(source.getBoundsInLocal());
            Point2D srcCenter = new Point2D(srcBounds.getMinX() + srcBounds.getWidth() / 2, srcBounds.getMinY() + srcBounds.getHeight() / 2);
            from.dependencies().stream().map(this::get).filter(activeModules::contains).forEach(to -> {
                Pane target = to.icon();

                Bounds tgtBounds = target.localToScene(target.getBoundsInLocal());
                Point2D tgtCenter = new Point2D(tgtBounds.getMinX() + tgtBounds.getWidth() / 2, tgtBounds.getMinY() + tgtBounds.getHeight() / 2);
                Point2D srcBorder = borderIntersection(srcBounds, tgtCenter);
                Point2D tgtBorder = borderIntersection(tgtBounds, srcCenter);
                Point2D srcOverlay = overlay.sceneToLocal(srcBorder);
                Point2D tgtOverlay = overlay.sceneToLocal(tgtBorder);
                Line line = new Line(srcOverlay.getX(), srcOverlay.getY(), tgtOverlay.getX(), tgtOverlay.getY());
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                overlay.getChildren().add(line);
                overlay.getChildren().add(arrowHead(tgtOverlay, srcOverlay));
            });
        });
    }

    private Point2D borderIntersection(Bounds bounds, Point2D to) {
        double cx = bounds.getMinX() + bounds.getWidth() / 2;
        double cy = bounds.getMinY() + bounds.getHeight() / 2;
        double dx = to.getX() - cx;
        double dy = to.getY() - cy;
        double scale = 0.5 / Math.max(Math.abs(dx) / bounds.getWidth(), Math.abs(dy) / bounds.getHeight());
        scale = Math.min(scale, 1.0); // Clamp to not overshoot
        return new Point2D(cx + dx * scale, cy + dy * scale);
    }

    private Polygon arrowHead(Point2D tgtOverlay, Point2D srcOverlay) {
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
        return arrowHead;
    }
}
