import org.openjfx.gradle.JavaFXModule
import org.openjfx.gradle.metadatarule.JavaFXComponentMetadataRule

plugins {
    id("org.gradlex.java-module-dependencies") version "1.9"
    id("org.openjfx.javafxplugin") version "0.1.0" apply false
    id("application")
}

repositories { mavenCentral() }

application {
    mainModule = "de.javarca.presentation"
    mainClass = "de.javarca.presentation.App"
}

val javaFX = "21.0.7"

dependencies.constraints {
    implementation("org.openjfx:javafx-graphics:$javaFX")
    implementation("org.openjfx:javafx-controls:$javaFX")
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
