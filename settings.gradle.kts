pluginManagement { includeBuild("gradle/plugins") }

plugins {
    id("org.gradlex.java-module-dependencies") version "1.9"
    id("com.autonomousapps.build-health") version "2.18.0"
}

rootProject.name = "javarcade"

javaModules {
    directory("modules") { group = "app.javarcade" }
    versions("gradle/versions")
}

gradle.lifecycle.beforeProject {
    plugins.withId("java") {
        tasks.named("check") {
            dependsOn("checkAllModuleInfo")
        }
    }
}
