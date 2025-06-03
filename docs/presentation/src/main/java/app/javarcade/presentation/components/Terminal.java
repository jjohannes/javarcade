package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.ShellCommand;
import app.javarcade.presentation.data.JavarcadeProject;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static app.javarcade.presentation.components.model.ShellCommand.Tool.JAVA;
import static app.javarcade.presentation.components.model.ShellCommand.Tool.RENOVATE;
import static app.javarcade.presentation.data.JavarcadeProject.APP_MODULES_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.ASSET_LOCATION;
import static app.javarcade.presentation.data.JavarcadeProject.EXTRA_INSTALL_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;

public record Terminal(Text theTerminal, ImageView renovatePR, ScrollPane container) {

    public Terminal(StackPane box) {
        this(new Text(), browserView(), new ScrollPane());

        theTerminal.setFont(Font.font("Monospaced", FontWeight.BOLD, 24));

        container.setContent(theTerminal);

        container.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0;");
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        box.getChildren().add(container);
    }

    private static ImageView browserView() {
        Image image = new Image(("file:%s/%s.png").formatted(ASSET_LOCATION.resolve("slides"), "renovate"));
        ImageView iconView = new ImageView(image);
        iconView.setPreserveRatio(true);
        return iconView;
    }

    public void reset(boolean moduleSystem, ShellCommand.Tool focusedTool) {
        if (focusedTool == RENOVATE) {
            container.setContent(renovatePR);
        } else  {
            Optional<ShellCommand> command = findCommand(moduleSystem, focusedTool);
            theTerminal.setText(command.orElseThrow().cmd());
            theTerminal.setOpacity(0.3);
            container.setContent(theTerminal);
        }
    }

    public void resetCurrent() {
        theTerminal.setText(theTerminal.getText().split("\n")[0]);
        theTerminal.setOpacity(0.3);
    }

    public void execute(boolean moduleSystem, ShellCommand.Tool focusedTool, Set<Module> activeModules, ApplicationScreen applicationScreen) {
        if (theTerminal.getOpacity() == 1.0) {
            return; // already executed
        }

        Optional<ShellCommand> cmd = findCommand(moduleSystem, focusedTool);
        if (cmd.isEmpty()) {
            return;
        }

        try {
            if (focusedTool == JAVA) {
                Path lib = WORK_FOLDER.resolve("lib");
                Files.createDirectories(lib);
                //noinspection resource
                for (Path file : Files.list(lib).toList()) {
                    Files.deleteIfExists(file);
                }
                Files.deleteIfExists(WORK_FOLDER.resolve("out").resolve("screen.png"));
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        theTerminal().setOpacity(1.0);

        new Thread(() -> {
            String consoleOut = runExternalCommand(cmd.get(), applicationScreen);
            Platform.runLater(() -> {
                theTerminal.setText(theTerminal.getText() + "\n" + consoleOut);
                applicationScreen.reloadScreenshot();
            });
        }).start();
    }

    private String runExternalCommand(ShellCommand cmd, ApplicationScreen applicationScreen) {
        try {
            Process p = Runtime.getRuntime().exec(
                    cmd.cmd().split("\\s+"),
                    new String[]{
                            "PATH=" + System.getenv("PATH"),
                            "HOME=" + System.getenv("HOME"),
                            "PRESENTATION_FOLDER=out",
                    },
                    cmd.workDir().toFile());
            p.waitFor();

            String output = new String(p.getInputStream().readAllBytes());
            String error = new String(p.getErrorStream().readAllBytes());
            System.out.println(output);
            System.out.println(error);
            return trimError(output, error);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String trimError(String output, String error) {
        String all = output + error;
        Optional<String> noClassDefFound = Arrays.stream(all.split("\n")).filter(l -> l.contains("NoClassDefFoundError")).findFirst();
        Optional<String> moduleFindException = Arrays.stream(all.split("\n")).filter(l -> l.contains("FindException")).findFirst();

        return noClassDefFound.orElse(moduleFindException.orElse(all.trim()));
    }

    private Optional<ShellCommand> findCommand(boolean moduleSystem, ShellCommand.Tool focusedTool) {
        return JavarcadeProject.shellCommands().stream().filter(c ->
                c.moduleSystem() == moduleSystem && c.tool() == focusedTool).findFirst();
    }
}
