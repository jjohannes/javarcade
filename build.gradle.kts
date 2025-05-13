plugins {
    id("org.gradlex.java-module-dependencies") version "1.9"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("application")
}

repositories { mavenCentral() }

application { mainClass = "app.javarcade.presentation.App" }

javafx { modules("javafx.graphics") }

dependencies.constraints {
    implementation("org.openjfx:javafx-graphics:21.0.7")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

// For sources download
configurations.configureEach {
    if (isCanBeResolved) {
        attributes {
            attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily.MACOS))
            attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(MachineArchitecture.ARM64))
        }
    }
}
