package app.javarcade.presentation;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.util.Set;

public record ProjectTree(Set<TreeItem<String>> tree,
                          ImageView jpmsButton,
                          ImageView gradleButton,
                          ImageView mavenButton,
                          ImageView renovateButton) {
}
