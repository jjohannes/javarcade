package de.javarca.presentation.components.model;

import java.nio.file.Path;

public record ShellCommand(String cmd, String cmdHidden, boolean moduleSystem, Tool tool, Path workDir, ShellCommand followUp) {
    public enum Tool {
        JAVA(""),
        GRADLE("apps/app-desktop/build/install/app-desktop"),
        MAVEN("apps/app-desktop/target/install/app-desktop"),
        RENOVATE("");

        private final String installDir;

        Tool(String installDir) {
            this.installDir = installDir;
        }

        public String getInstallDir() {
            return installDir;
        }
    }
}
