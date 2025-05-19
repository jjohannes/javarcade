package app.javarcade.presentation.state;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.Editors;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.TopicGrid;
import app.javarcade.presentation.components.model.Module;
import javafx.scene.control.TreeItem;

import java.util.HashSet;
import java.util.Set;

import static app.javarcade.presentation.data.JavarcadeProject.RUN_CLASS_PATH_CMD;
import static app.javarcade.presentation.data.JavarcadeProject.RUN_MODULE_PATH_CMD;
import static app.javarcade.presentation.data.JavarcadeProject.initialState;

public class SlideControl {

    private final Set<Module> activeModules = new HashSet<>();
    private boolean graph = false;
    private boolean gradleNotMaven = true;
    private boolean moduleSystem = true;
    private boolean renovate = false;
    private TreeItem<String> selectedItem = null;

    public SlideControl(ApplicationScreen applicationScreen, ModuleGraph moduleGraph, ProjectTree projectTree, Editors editors, Terminal terminal, TopicGrid topicGrid) {
        terminal.commands().get(0).setText(RUN_MODULE_PATH_CMD);
        terminal.commands().get(1).setText(RUN_CLASS_PATH_CMD);
        activeModules.addAll(initialState(moduleGraph));

        moduleGraph.modules().forEach(module ->
                module.icon().setOnMouseClicked(event -> active(module, moduleGraph, applicationScreen, terminal))
        );
        terminal.commands().forEach(cmd ->
                cmd.setOnMouseClicked(event -> terminal.execute(cmd, activeModules, applicationScreen))
        );
        projectTree.jpmsButton().setOnMouseClicked(event -> {
            moduleSystem = !moduleSystem;
            projectTree.update(gradleNotMaven, moduleSystem, renovate);
            editors.open(selectedItem);
        });
        projectTree.gradleButton().setOnMouseClicked(event -> {
            gradleNotMaven = true;
            projectTree.update(true, moduleSystem, renovate);
            editors.open(selectedItem);
        });
        projectTree.mavenButton().setOnMouseClicked(event -> {
            gradleNotMaven = false;
            projectTree.update(false, moduleSystem, renovate);
            editors.open(selectedItem);
        });
        projectTree.renovateButton().setOnMouseClicked(event -> {
            renovate = !renovate;
            projectTree.update(gradleNotMaven, moduleSystem, renovate);
            editors.open(selectedItem);
        });
        projectTree.tree().setOnMouseClicked(event -> {
            selectedItem = projectTree.tree().getSelectionModel().getSelectedItem();
            editors.open(selectedItem);
        });
        terminal.reset();
        moduleGraph.update(activeModules, graph);
        projectTree.update(gradleNotMaven, moduleSystem, renovate);
    }

    private void active(Module jar, ModuleGraph moduleGraph, ApplicationScreen applicationScreen, Terminal terminal) {
        if (activeModules.contains(jar)) {
            activeModules.remove(jar);
        } else {
            activeModules.add(jar);
        }
        moduleGraph.update(activeModules, graph);
        applicationScreen.reset();
        terminal.reset();
    }
}
