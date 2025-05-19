pluginManagement { includeBuild("gradle/plugins") }

plugins { id("org.gradlex.java-module-dependencies") version "1.8.1" }

javaModules {
    directory("modules") { group = "app.javarcade" }
    versions("gradle/versions")
}
