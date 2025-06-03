package app.javarcade.presentation.components.model;

import java.nio.file.Path;

public record ShellCommand(String cmd, boolean moduleSystem, Tool tool, Path workDir) {
    public enum Tool {
        JAVA,
        GRADLE,
        MAVEN,
        RENOVATE
    }
}
