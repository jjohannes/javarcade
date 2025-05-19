package app.javarcade.presentation.components;

import app.javarcade.presentation.components.model.Module;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static app.javarcade.presentation.data.JavarcadeProject.APP_INSTALL_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.EXTRA_INSTALL_FOLDER;
import static app.javarcade.presentation.data.JavarcadeProject.WORK_FOLDER;
import static app.javarcade.presentation.ui.UI.SPACE;

public record Terminal(List<Text> commands) {

    public Terminal(StackPane box) {
        this(List.of(new Text(), new Text()));

        commands.get(0).setFont(Font.font("Monospaced", FontWeight.BOLD, 36));
        commands.get(1).setFont(Font.font("Monospaced", FontWeight.BOLD, 36));
        TextFlow textFlow1 = new TextFlow(commands.get(0));
        TextFlow textFlow2 = new TextFlow(commands.get(1));

        VBox vBox = new VBox(textFlow1, textFlow2);
        vBox.setSpacing(SPACE * 3);
        vBox.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().add(vBox);
    }

    public void reset() {
        updateVisibility(null);
    }

    public void execute(Text cmd, Set<Module> activeModules, ApplicationScreen applicationScreen) {
        try {
            Path lib = WORK_FOLDER.resolve("lib");
            //noinspection resource
            for (Path file : Files.list(lib).toList()) {
                Files.deleteIfExists(file);
            }
            Files.deleteIfExists(WORK_FOLDER.resolve("out").resolve("screen.png"));
            Files.createDirectories(lib);
            for (Module module : activeModules) {
                if (Files.exists(APP_INSTALL_FOLDER.resolve(module.jarName()))) {
                    Files.copy(APP_INSTALL_FOLDER.resolve(module.jarName()), lib.resolve(module.jarName()));
                } else {
                    Files.copy(EXTRA_INSTALL_FOLDER.resolve(module.jarName()), lib.resolve(module.jarName()));
                }
            }

            updateVisibility(cmd);

            var p = Runtime.getRuntime().exec(
                    cmd.getText().split("\\s+"),
                    new String[]{
                            "PATH=" + System.getenv("PATH"),
                            "PRESENTATION_FOLDER=out"
                    },
                    WORK_FOLDER.toFile());
            p.waitFor();

            String output = new String(p.getInputStream().readAllBytes());
            String error = new String(p.getErrorStream().readAllBytes());
            System.out.println(output);
            System.out.println(error);

            applicationScreen.error().setText(trimError(output, error));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        applicationScreen.reloadScreenshot();
    }

    private void updateVisibility(Text active) {
        commands.forEach(cmd -> cmd.setOpacity(active == cmd ? 1 : 0.3));
    }

    private String trimError(String output, String error) {
        String all = output + error;
        Optional<String> noClassDefFound = Arrays.stream(all.split("\n")).filter(l -> l.contains("NoClassDefFoundError")).findFirst();
        Optional<String> moduleFindException = Arrays.stream(all.split("\n")).filter(l -> l.contains("FindException")).findFirst();

        return noClassDefFound.orElse(moduleFindException.orElse(all.trim()));
    }
}
