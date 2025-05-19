package app.javarcade.presentation.components;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import app.javarcade.presentation.components.model.Module;

import java.util.Set;

import static app.javarcade.presentation.ui.UIComponents.dependencyGraph;

public record ModuleGraph(Set<Module> modules) {

    public ModuleGraph(StackPane box, Set<Module> modules) {
        this(modules);

        GridPane grid = new GridPane();
        Pane overlay = new Pane();
        grid.setHgap(10);
        grid.setVgap(10);
        overlay.setPickOnBounds(false); // mouse events pass through
        box.getChildren().add(new StackPane(grid, overlay));
        modules().forEach(module -> grid.add(module.icon(), module.columnIndex(), module.rowIndex()));
    }

    public Module get(String jarName) {
        return modules.stream().filter(m -> m.jarName().equals(jarName)).findFirst().orElseThrow();
    }

    public void update(Set<Module> activeModules, boolean graph) {
        modules.forEach(module -> module.icon().setOpacity(activeModules.contains(module) ? 1 : 0.3));
        if (graph) {
            dependencyGraph(this, activeModules);
        }
    }
}
