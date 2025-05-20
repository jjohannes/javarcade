pluginManagement { includeBuild("gradle/plugins") }

plugins {
    id("org.gradlex.java-module-dependencies") version "1.8.1"
    id("com.autonomousapps.build-health") version "2.15.0"
}

javaModules {
    directory("modules") { group = "app.javarcade" }
    versions("gradle/versions")
}
