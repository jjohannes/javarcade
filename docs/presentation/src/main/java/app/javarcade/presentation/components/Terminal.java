package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.ShellCommand;
import app.javarcade.presentation.data.JavarcadeProject;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.GRADLE;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.JAVA;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.MAVEN;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.APP_MODULES_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.APP_NO_MODULES_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.APP_ROOT_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.data.JavarcadeProject.EXTRA_INSTALL_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;
import static app.javarcade.presentation.ui.UI.applyScrollPaneStyleMono;

public record Terminal(TextFlow theTerminal, ImageView nuke, ImageView renovatePR, ScrollPane container) {

    public Terminal(StackPane box) {
        this(new TextFlow(), nukeButton(), browserView(), applyScrollPaneStyleMono(new ScrollPane()));

        container.setContent(theTerminal);
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        nuke.setOnMouseClicked(event -> {
            nuke.setVisible(false);
            resetCurrent();
            cleanCaches();
        });

        box.setAlignment(Pos.TOP_RIGHT);
        box.getChildren().addAll(container, nuke);

        cleanCaches();
    }

    private static ImageView nukeButton() {
        Image image = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("icons"), "nuke"));
        ImageView nuke = new ImageView(image);
        nuke.setFitHeight(25);
        nuke.setFitWidth(25);
        nuke.setVisible(false);
        return nuke;
    }

    private static ImageView browserView() {
        Image image = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("slides"), "renovate"));
        ImageView iconView = new ImageView(image);
        iconView.setPreserveRatio(true);
        return iconView;
    }

    public void reset(boolean moduleSystem, ShellCommand.Tool focusedTool, boolean rogue) {
        Optional<ShellCommand> command = findCommand(moduleSystem, focusedTool);
        theTerminal.getChildren().clear();
        Text text = new Text(rogue ? command.orElseThrow().cmd().replace(" clean", "") : command.orElseThrow().cmd());
        text.setFill(Color.WHITE);
        theTerminal.getChildren().add(text);
        theTerminal.setOpacity(0.7);
        container.setContent(theTerminal);
    }

    public void resetCurrent() {
        if (!theTerminal.getChildren().isEmpty()) {
            String old = ((Text) theTerminal.getChildren().getFirst()).getText();
            theTerminal.getChildren().clear();
            Text text = new Text(old.split("\n")[0]);
            text.setFill(Color.WHITE);
            theTerminal.getChildren().add(text);
        }
        theTerminal.setOpacity(0.7);
    }

    public void execute(boolean moduleSystem, ShellCommand.Tool focusedTool, Set<Module> activeModules,
                        ApplicationScreen applicationScreen, Consumer<Path> updateCommand) {
        if (focusedTool == RENOVATE) {
            container.setContent(renovatePR);
            return;
        }

        if (theTerminal.getOpacity() == 1.0) {
            return; // already executed
        }

        Optional<ShellCommand> cmd = findCommand(moduleSystem, focusedTool);
        if (cmd.isEmpty()) {
            return;
        }

        try {
            Files.deleteIfExists(WORK_FOLDER.resolve("out").resolve("screen.png"));
            applicationScreen.reloadScreenshot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        theTerminal().setOpacity(1.0);
        Text commandText = (Text) theTerminal.getChildren().getFirst();
        commandText.setFill(Color.GREEN);

        new Thread(() -> {
            var succeeded = runExternalCommand(cmd.get(), activeModules, commandText.getText());
            if (succeeded) {
                Platform.runLater(() -> {
                    updateCommand.accept(cmd.get().workDir());
                    runExternalCommand(cmd.get().followUp(), activeModules, null);
                });
            }
            Platform.runLater(applicationScreen::reloadScreenshot);
        }).start();
    }

    private boolean runExternalCommand(ShellCommand cmd, Set<Module> activeModules, String override) {
        if (cmd == null) {
            return false;
        }

        try {
            if (cmd.tool() == JAVA) {
                Path lib = WORK_FOLDER.resolve("lib");
                Files.createDirectories(lib);
                //noinspection resource
                for (Path file : Files.list(lib).toList()) {
                    Files.deleteIfExists(file);
                }
                Files.createDirectories(lib);
                for (Module module : activeModules) {
                    try (Stream<Path> result = Files.find(APP_MODULES_FOLDER, 4, (p, a) -> module.jarName().equals(p.getFileName().toString()))) {
                        Optional<Path> jarPath = result.findFirst();
                        if (jarPath.isPresent()) {
                            Files.copy(jarPath.get(), lib.resolve(module.jarName()));
                        } else {
                            Files.copy(EXTRA_INSTALL_FOLDER.resolve(module.jarName()), lib.resolve(module.jarName()));
                        }
                    }
                }
            }

            Process p = run((override == null ? cmd.cmd() : override) + cmd.cmdHidden(), cmd.workDir());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    updateTerminal(line, Color.WHITE);
                    if (line.equals("Calculating task graph as no cached configuration is available for tasks: build")) {
                        printGradleDownloadLog();
                    }
                }
            }

            p.waitFor();

            String error = new String(p.getErrorStream().readAllBytes());
            System.out.println(error);

            updateTerminal(trimError(error), Color.INDIANRED);

            if (cmd.tool() == GRADLE || cmd.tool() == MAVEN) {
                nuke.setVisible(true);
            }
            return error.isEmpty();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void printGradleDownloadLog() {
        try {
            Thread.sleep(1000);
            for (String line : Files.readAllLines(ASSET_LOCATION.resolve("gradle-download.log"))) {
                updateTerminal(line, Color.WHITE);
                Thread.sleep(5);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTerminal(String nextLine, Color color) {
        Platform.runLater(() -> {
            Text text = new Text("\n" + nextLine);
            text.setFill(color);
            if (text.getText().contains("Download")) {
                text.setFill(Color.DARKSLATEGRAY);
            }
            if (text.getText().contains("SUCCESS")) {
                text.setFill(Color.DARKGREEN);
            }
            if (text.getText().contains("FAIL")) {
                text.setFill(Color.INDIANRED);
            }
            if (text.getText().contains("INFO")) {
                text.setFill(Color.WHITE);
            }
            theTerminal.getChildren().add(text);
            container.setVvalue(1.0);
        });
    }

    private String trimError(String error) {
        Optional<String> noClassDefFound = Arrays.stream(error.split("\n")).filter(l -> l.contains("NoClassDefFoundError")).findFirst();
        Optional<String> moduleFindException = Arrays.stream(error.split("\n")).filter(l -> l.contains("FindException")).findFirst();

        return noClassDefFound.orElse(moduleFindException.orElse(error.trim()));
    }

    private Optional<ShellCommand> findCommand(boolean moduleSystem, ShellCommand.Tool focusedTool) {
        return JavarcadeProject.shellCommands().stream().filter(c ->
                c.moduleSystem() == moduleSystem && c.tool() == focusedTool).findFirst();
    }

    private void cleanCaches() {
        try {
            run("find . -type d -name build -exec rm -rf {} \\;", APP_MODULES_FOLDER).waitFor();
            run("find . -type d -name build -exec rm -rf {} \\;", APP_NO_MODULES_FOLDER).waitFor();
            run("find . -type d -name target -exec rm -rf {} \\;", APP_MODULES_FOLDER).waitFor();
            run("find . -type d -name target -exec rm -rf {} \\;", APP_NO_MODULES_FOLDER).waitFor();
            run("rm -rf gradle/plugins/build", APP_ROOT_FOLDER).waitFor();
            run("rm -rf .gradle", APP_ROOT_FOLDER).waitFor();
            run("rm -rf .gradle", APP_NO_MODULES_FOLDER).waitFor();
            run("rm -rf home/.gradle/caches/build-cache-1", WORK_FOLDER).waitFor();
            run("rm -rf home/.gradle/caches/modules-2", WORK_FOLDER).waitFor();
            run("rm -rf home/.m2/repo", WORK_FOLDER).waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private Process run(String cmd, Path workDir) {
        try {
            return Runtime.getRuntime().exec(
                    cmd.split("\\s+"),
                    new String[]{
                            "PATH=" + System.getenv("PATH"),
                            "GRADLE_USER_HOME=" + WORK_FOLDER.resolve("home/.gradle"),
                            "MAVEN_USER_HOME=" + WORK_FOLDER.resolve("home/.m2"),
                            "PRESENTATION_FOLDER=out",
                    },
                    workDir.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
