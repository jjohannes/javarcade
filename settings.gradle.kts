pluginManagement { includeBuild("gradle/plugins") }

plugins {
    id("org.gradlex.java-module-dependencies") version "1.9"
    id("com.autonomousapps.build-health") version "2.18.0"
}

rootProject.name = "javarcade"

javaModules {
    directory("modules") { group = "de.javarca" }
    versions("gradle/versions")
    directory("apps") {
        module("app-desktop")
        group = "de.javarca"
    }
}

gradle.lifecycle.beforeProject {
    plugins.withId("java-library") {
        tasks.named("check") {
            dependsOn("checkAllModuleInfo")
        }
    }
}
