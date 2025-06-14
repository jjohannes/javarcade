package app.javarcade.presentation.state;

import app.javarcade.presentation.components.ApplicationScreen;
import app.javarcade.presentation.components.Editor;
import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.ProjectTree;
import app.javarcade.presentation.components.Terminal;
import app.javarcade.presentation.components.ToolsGrid;
import app.javarcade.presentation.components.TopicList;
import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.ShellCommand;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.GRADLE;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.JAVA;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.MAVEN;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.initialState;
import static java.util.Objects.requireNonNull;

public class SlideControl {

    private final Set<Module> activeModules = new HashSet<>();
    private boolean graph = false;
    private ShellCommand.Tool focusedTool = JAVA;
    private boolean moduleSystem = false;
    private TreeItem<String> selectedItem = null;
    private boolean rogue = false;

    public SlideControl(ApplicationScreen applicationScreen, ModuleGraph moduleGraph, ProjectTree projectTree, Editor editor, Terminal terminal, ToolsGrid tools, TopicList topics) {
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
            if (focusedTool == GRADLE || focusedTool == MAVEN) {
                activeModules.clear();
                moduleGraph.update(activeModules, graph);
                terminal.execute(moduleSystem, focusedTool, activeModules, applicationScreen, workDir -> {
                    updateActiveModulesFromInstallDir(moduleGraph, workDir);
                    moduleGraph.update(activeModules, graph);
                });
            } else {
                terminal.execute(moduleSystem, focusedTool, activeModules, applicationScreen, workDir -> {});
            }
        });

        tools.jpmsButton().setOnMouseClicked(event -> {
            moduleSystem = !moduleSystem;
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem, rogue);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        tools.gradleButton().setOnMouseClicked(event -> {
            focusedTool = refocus(GRADLE);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem, rogue);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        tools.mavenButton().setOnMouseClicked(event -> {
            focusedTool = refocus(MAVEN);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem, rogue);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        tools.renovateButton().setOnMouseClicked(event -> {
            focusedTool = refocus(RENOVATE);
            projectTree.update(focusedTool, moduleSystem);
            tools.update(focusedTool, moduleSystem, rogue);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        projectTree.jarTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.jarTree().getSelectionModel().getSelectedItem();
            selectTopic(topics, null);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        projectTree.projectTree().setOnMouseClicked(event -> {
            selectedItem = projectTree.projectTree().getSelectionModel().getSelectedItem();
            selectTopic(topics, null);
            editor.open(selectedItem, moduleSystem);
            terminal.reset(moduleSystem, focusedTool, rogue);
        });
        editor.content().focusedProperty().addListener((observable, old, focus) -> {
            if (focus) {
                terminal.reset(moduleSystem, focusedTool, rogue);
            } else {
                editor.save(selectedItem, moduleSystem);
            }
        });

        terminal.resetCurrent();
        moduleGraph.update(activeModules, graph);
        projectTree.update(focusedTool, moduleSystem);
        tools.update(focusedTool, moduleSystem, rogue);
        terminal.reset(moduleSystem, focusedTool, rogue);
    }

    private void updateActiveModulesFromInstallDir(ModuleGraph moduleGraph, Object workDir) {

    }

    private void updateActiveModulesFromInstallDir(ModuleGraph moduleGraph, Path workDir) {
        if (workDir == null) {
            return;
        }
        var installDir = workDir.resolve(focusedTool.getInstallDir());
        if (!installDir.toFile().isDirectory()) {
            return;
        }
        Arrays.stream(requireNonNull(installDir.toFile().listFiles())).map(File::getName).forEach(jar -> {
            activeModules.add(moduleGraph.modules().stream().filter(m -> m.jarName().equals(jar))
                    .findFirst().orElse(new Module(jar)));
        });
    }

    public void toggleRogueMode(ToolsGrid tools, Terminal terminal) {
        rogue = !rogue;
        tools.update(focusedTool, moduleSystem, rogue);
        terminal.reset(moduleSystem, focusedTool, rogue);
    }

    private void selectTopic(TopicList topics, ShellCommand.Tool toolInTerminal) {
        if (toolInTerminal == GRADLE || toolInTerminal == MAVEN) {
            topics.focus(topics.topics().get(2));
            return;
        }
        if (toolInTerminal == RENOVATE) {
            topics.focus(topics.topics().get(5));
            return;
        }
        if (selectedItem == null) {
            return;
        }
        if (selectedItem.getParent() != null && selectedItem.getParent().getValue().endsWith("versions")) {
            topics.focus(topics.topics().get(1));
            return;
        }
        if (selectedItem.getValue().startsWith("repositories")) {
            topics.focus(topics.topics().get(2));
            return;
        }
        if (selectedItem.getValue().startsWith("dependency-rules")) {
            topics.focus(topics.topics().get(3));
            return;
        }
        if (selectedItem.getValue().startsWith("quality-check")) {
            topics.focus(topics.topics().get(4));
            return;
        }
        if (selectedItem.getValue().startsWith("java-module")) {
            topics.focus(topics.topics().get(4));
            return;
        }
        if (selectedItem.getValue().startsWith("compile") || selectedItem.getValue().startsWith("test")) {
            topics.reset();
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
