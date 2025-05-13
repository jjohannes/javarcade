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
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;

public class SlideControl {
    public static final int MAX = 2;

    private int current = 1;

    private final ImageView screen;
    private final Text terminalText;
    private final GridPane folderGrid;

    public SlideControl(ImageView screen, Text terminalText, GridPane folderGrid) {
        this.screen = screen;
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
        if (current == MAX) {
            return;
        }
        current++;
        reload();
    }

    private void reload() {
        String slideFolder = String.format("/Users/jendrik/projects/gradle/howto/javarcade-presentation/assets/%03d", current);

        loadImage(slideFolder);

        loadTerminal(slideFolder);

        loadFolder(slideFolder);
    }

    private void loadImage(String slideFolder) {
        Image image = new Image("file:" + slideFolder + "/screen.png");
        screen.setImage(image);
    }

    private void loadTerminal(String slideFolder) {
        try {
            String text = Files.readString(Path.of(slideFolder + "/command.txt"));
            terminalText.setText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFolder(String slideFolder) {
        Image folderIcon = new Image("file:" + slideFolder + "/../main/jar.png");
        List<String> jars = Arrays.stream(requireNonNull(new File(slideFolder + "/lib").listFiles())).map(File::getName).toList();

        folderGrid.getChildren().clear();

        // Add icon and text to the grid
        range(0, jars.size()).forEachOrdered(i -> {
            String jarName = jars.get(i);
            ImageView iconView = new ImageView(folderIcon);
            StackPane iconContainer = new StackPane(iconView);
            iconView.setFitWidth(50);
            iconView.setFitHeight(50);
            Text jarText = new Text(jarName);
            jarText.setWrappingWidth(95);
            int row = i / 5;
            int col = i % 5;
            folderGrid.add(iconContainer, col, row * 2); // Icon in the first row of the cell
            folderGrid.add(jarText, col, row * 2 + 1); // Text in the second row of the cell
        });
    }
}
