package app.javarcade.presentation.components.model;

import java.nio.file.Path;

public record ShellCommand(String cmd, String cmdHidden, boolean moduleSystem, Tool tool, Path workDir, ShellCommand followUp) {
    public enum Tool {
        JAVA("", ""),
        GRADLE("./gradlew installDist", "apps/app-desktop/build/install/desktop"),
        MAVEN("", ""),
        RENOVATE("", "");

        private final String installCommand;
        private final String installDir;

        Tool(String installCommand, String installDir) {
            this.installCommand = installCommand;
            this.installDir = installDir;
        }

        public String getInstallCommand() {
            return installCommand;
        }

        public String getInstallDir() {
            return installDir;
        }
    }
}
