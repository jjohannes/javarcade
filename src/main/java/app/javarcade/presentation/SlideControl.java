package app.javarcade.presentation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SlideControl {
    private static final Path WORK_FOLDER =
            Path.of("/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/work");

    private static final Path APP_INSTALL_FOLDER =
            Path.of("/Users/jendrik/projects/gradle/howto/javarcade/apps/app-retro/build/install/app-retro/lib");

    private static final String RUN_MODULE_PATH_CMD =
            "java --module-path lib --module app.javarcade.base.engine";
    private static final String RUN_CLASS_PATH_CMD =
            "java --class-path  lib/*        app.javarcade.base.engine.Engine";

    private final ImageView screen;
    private final Text screenError;
    private final List<Text> terminal;
    private final Map<String, HBox> jars;
    private final Set<String> activeJars = new HashSet<>();

    public SlideControl(ImageView screen, Text screenError, List<Text> terminal, Map<String, HBox> jars) {
        this.screen = screen;
        this.screenError = screenError;
        this.terminal = terminal;
        this.jars = jars;

        terminal.get(0).setText(RUN_MODULE_PATH_CMD);
        terminal.get(1).setText(RUN_CLASS_PATH_CMD);

        terminal.forEach(cmd -> cmd.setOnMouseClicked(event ->
                execute(((Text)event.getTarget()).getText())));

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
    }

    private void execute(String cmd) {

        try {
            Path lib = WORK_FOLDER.resolve("lib");
            //noinspection resource
            for (Path file : Files.list(lib).toList()) {
                Files.deleteIfExists(file);
            }
            Files.createDirectories(lib);
            for (String jar : activeJars) {
                Files.copy(APP_INSTALL_FOLDER.resolve(jar), lib.resolve(jar));
            }

            var p = Runtime.getRuntime().exec(
                    cmd.split("\\s+"),
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

        return noClassDefFound.orElse(moduleFindException.orElse(all));
    }

    private void loadImage(Path workFolder) {
        var imageFile = workFolder.resolve("out/screen.png").toFile();
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            screen.setImage(image);
            screen.setVisible(true);
            screenError.setVisible(false);
        } else {
            screen.setVisible(false);
            screenError.setVisible(true);
        }
    }
}
