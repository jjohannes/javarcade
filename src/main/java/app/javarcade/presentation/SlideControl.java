package app.javarcade.presentation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;

public class SlideControl {
    private int current = 1;

    private final ImageView screen;
    private final Text screenError;
    private final Text terminalText;
    private final GridPane folderGrid;

    public SlideControl(ImageView screen, Text screenError, Text terminalText, GridPane folderGrid) {
        this.screen = screen;
        this.screenError = screenError;
        this.terminalText = terminalText;
        this.folderGrid = folderGrid;
        reload();
    }

    void prev() {
        if (current == 1) {
            return;
        }
        current--;
        reload();
    }

    void next() {
        if (!new File(slideFolder(current + 1)).exists()) {
            return;
        }
        current++;
        reload();
    }

    private void reload() {
        String slideFolder = slideFolder(current);

        System.out.println("===== SWITCHED TO " + current + " =====");

        try {
            var p = Runtime.getRuntime().exec(
                    new String[] { "./prepare.sh" },
                    new String[] { "PATH=" + System.getenv("PATH")},
                    new File(slideFolder));
            p.waitFor();

            String output = new String(p.getInputStream().readAllBytes());
            String error = new String(p.getErrorStream().readAllBytes());
            System.out.println(output);
            System.out.println(error);

            screenError.setText(trimError(output, error));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        loadImage(slideFolder);

        loadTerminal(slideFolder);

        loadFolder(slideFolder);
    }

    private String trimError(String output, String error) {
        String all = output + error;
        Optional<String> noClassDefFound = Arrays.stream(all.split("\n")).filter(l -> l.contains("NoClassDefFoundError")).findFirst();
        Optional<String> moduleFindException = Arrays.stream(all.split("\n")).filter(l -> l.contains("FindException")).findFirst();

        return noClassDefFound.orElse(moduleFindException.orElse(all));
    }

    private String slideFolder(int no) {
        return String.format("/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/%03d", no);
    }

    private void loadImage(String slideFolder) {
        var imaageFile = new File(slideFolder + "/out/screen.png");
        if (imaageFile.exists()) {
            Image image = new Image(imaageFile.toURI().toString());
            screen.setImage(image);
            screen.setVisible(true);
            screenError.setVisible(false);
        } else {
            screen.setVisible(false);
            screenError.setVisible(true);
        }
    }

    private void loadTerminal(String slideFolder) {
        try {
            List<String> commands = Files.readAllLines(Path.of(slideFolder + "/prepare.sh"));
            List<String> visible = commands.subList(commands.lastIndexOf("") + 1, commands.size());
            terminalText.setText(String.join("\n", visible));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFolder(String slideFolder) {
        Image folderIcon = new Image("file:" + slideFolder + "/../main/jar.png");
        List<String> jars = Arrays.stream(requireNonNull(new File(slideFolder + "/lib").listFiles())).map(File::getName).toList();

        var lwjglCount = jars.stream().filter(j -> j.startsWith("lwjgl-")).count();
        var filtered = Stream.concat(jars.stream().filter(j -> !j.startsWith("lwjgl-")), Stream.of("LWJGL (" + lwjglCount + " JARs)"))
                .map(s -> s.replace("commons-", "")).toList();

        folderGrid.getChildren().clear();

        // Add icon and text to the grid
        range(0, filtered.size()).forEachOrdered(i -> {
            String jarName = filtered.get(i);
            ImageView iconView = new ImageView(folderIcon);
            StackPane iconContainer = new StackPane(iconView);
            iconView.setFitWidth(50);
            iconView.setFitHeight(50);
            Text jarText = new Text(jarName);
            jarText.setWrappingWidth(120);
            int row = i / 4;
            int col = i % 4;
            folderGrid.add(iconContainer, col, row * 2); // Icon in the first row of the cell
            folderGrid.add(jarText, col, row * 2 + 1); // Text in the second row of the cell
        });
    }
}
