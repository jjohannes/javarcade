package de.javarca.presentation.components.model;

import java.nio.file.Path;

public record FileOfBuild(String name, Path containingFolder) {


    @Override
    public String toString() {
        return name;
    }
}
