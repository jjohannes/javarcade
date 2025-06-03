import org.openjfx.gradle.JavaFXModule
import org.openjfx.gradle.metadatarule.JavaFXComponentMetadataRule

plugins {
    id("org.gradlex.java-module-dependencies") version "1.9"
    id("org.openjfx.javafxplugin") version "0.1.0" apply false
    id("application")
}

repositories { mavenCentral() }

application {
    mainModule = "app.javarcade.presentation"
    mainClass = "app.javarcade.presentation.App"
}

dependencies.constraints {
    implementation("org.openjfx:javafx-graphics:21.0.8-ea+1")
    implementation("org.openjfx:javafx-controls:21.0.7")
    testImplementation("org.assertj:assertj-core:3.22.0")
}

JavaFXModule.values().forEach { javaFXModule ->
    dependencies.components.withModule<JavaFXComponentMetadataRule>("org.openjfx:" + javaFXModule.artifactName)
}

configurations.configureEach {
    if (isCanBeResolved) {
        attributes {
            attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(OperatingSystemFamily.MACOS))
            attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(MachineArchitecture.ARM64))
        }
    }
}
