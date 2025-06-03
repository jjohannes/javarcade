package app.javarcade.presentation.state;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.Editors;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.ToolsGrid;
import app.javarcade.presentation.components.TopicList;
import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.ShellCommand;
import javafx.scene.control.TreeItem;

import java.util.HashSet;
import java.util.List;
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
    private boolean moduleSystem = false;
    private TreeItem<String> selectedItem = null;

    public SlideControl(ApplicationScreen applicationScreen, ModuleGraph moduleGraph, ProjectTree projectTree, Editors editors, Terminal terminal, ToolsGrid tools, TopicList topics) {
        activeModules.addAll(initialState(moduleGraph));

        moduleGraph.modules().forEach(module ->
                module.icon().setOnMouseClicked(event -> active(module, moduleGraph, applicationScreen, terminal))
        );
        moduleGraph.label().setOnMouseClicked(event -> {
            graph = !graph;
            moduleGraph.update(activeModules, graph);
        });

        terminal.theTerminal().setOnMouseClicked(event -> {
            selectTopic(topics, focusedTool);
            terminal.execute(moduleSystem, focusedTool, activeModules, applicationScreen);
        });

        tools.jpmsButton().setOnMouseClicked(event -> {
            moduleSystem = !moduleSystem;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.gradleButton().setOnMouseClicked(event -> {
            focusedTool = refocus(GRADLE);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.mavenButton().setOnMouseClicked(event -> {
            focusedTool = refocus(MAVEN);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        tools.renovateButton().setOnMouseClicked(event -> {
            focusedTool = refocus(RENOVATE);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        projectTree.jarTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.jarTree().getSelectionModel().getSelectedItem();
            selectTopic(topics, null);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        projectTree.projectTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.projectTree().getSelectionModel().getSelectedItem();
            selectTopic(topics, null);
            editors.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool);
        });
        terminal.resetCurrent();
        moduleGraph.update(activeModules, graph);
        projectTree.update(focusedTool, moduleSystem);
        tools.update(focusedTool, moduleSystem);
        terminal.reset(moduleSystem, focusedTool);
    }

    private void selectTopic(TopicList topics, ShellCommand.Tool toolInTerminal) {
        if (toolInTerminal == GRADLE || toolInTerminal == MAVEN) {
            topics.focus(topics.topics().get(2));
            return;
        }
        if (selectedItem == null) {
            return;
        }
        if (selectedItem.getParent().getValue().endsWith("versions")) {
            topics.focus(topics.topics().get(1));
            return;
        }
        if (selectedItem.getValue().startsWith("dependency-rules")) {
            topics.focus(topics.topics().get(3));
            return;
        }
        if (selectedItem.getValue().startsWith("quality-checks")) {
            topics.focus(topics.topics().get(4));
            return;
        }
        if (selectedItem.getValue().equals("renovate.json")) {
            topics.focus(topics.topics().get(5));
            return;
        }
        if (List.of("module-info.class", "commons-csv-1.14.0.pom", "module-info.java", "build.gradle.kts", "pom.xml").contains(selectedItem.getValue())) {
            topics.focus(topics.topics().getFirst());
        }
    }

    private ShellCommand.Tool refocus(ShellCommand.Tool requested) {
        if (focusedTool == requested) {
            selectedItem = null;
            return JAVA;
        }
        return requested;
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
