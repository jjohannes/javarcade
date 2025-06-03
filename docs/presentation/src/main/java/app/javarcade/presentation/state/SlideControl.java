package app.javarcade.presentation.state;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.Editors;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.SlideBar;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.ToolsGrid;
import app.javarcade.presentation.components.TopicGrid;
import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.ShellCommand;
import javafx.scene.control.TreeItem;

import java.util.HashSet;
import java.util.Set;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.GRADLE;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.JAVA;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.MAVEN;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.initialState;

public class SlideControl {

    private final Set<Module> activeModules = new HashSet<>();
    private boolean graph = false;
    private ShellCommand.Tool focusedTool = JAVA;
    private boolean moduleSystem = true;
    private TreeItem<String> selectedItem = null;

    public SlideControl(SlideBar slideBar, ApplicationScreen applicationScreen, ModuleGraph moduleGraph, ProjectTree projectTree, Editors editors, Terminal terminal, ToolsGrid tools, TopicGrid topics) {
        activeModules.addAll(initialState(moduleGraph));

        moduleGraph.modules().forEach(module ->
                module.icon().setOnMouseClicked(event -> active(module, moduleGraph, applicationScreen, terminal))
        );
        moduleGraph.label().setOnMouseClicked(event -> {
            graph = !graph;
            moduleGraph.update(activeModules, graph);
        });

        terminal.theTerminal().setOnMouseClicked(event ->
                terminal.execute(moduleSystem, focusedTool, activeModules, applicationScreen));

        tools.jpmsButton().setOnMouseClicked(event -> {
            moduleSystem = !moduleSystem;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.gradleButton().setOnMouseClicked(event -> {
            focusedTool = focusedTool == GRADLE ? JAVA : GRADLE;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.mavenButton().setOnMouseClicked(event -> {
            focusedTool = focusedTool == MAVEN ? JAVA : MAVEN;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.renovateButton().setOnMouseClicked(event -> {
            focusedTool = focusedTool == RENOVATE ? JAVA : RENOVATE;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        projectTree.jarTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.jarTree().getSelectionModel().getSelectedItem();
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        projectTree.projectTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.projectTree().getSelectionModel().getSelectedItem();
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        terminal.resetCurrent();
        moduleGraph.update(activeModules, graph);
        projectTree.update(focusedTool, moduleSystem);
        tools.update(focusedTool, moduleSystem);
        terminal.reset(moduleSystem, focusedTool);
    }

    private void active(Module jar, ModuleGraph moduleGraph, ApplicationScreen applicationScreen, Terminal terminal) {
        if (activeModules.contains(jar)) {
            activeModules.remove(jar);
        } else {
            activeModules.add(jar);
        }
        moduleGraph.update(activeModules, graph);
        applicationScreen.reset();
        terminal.resetCurrent();
    }
}
