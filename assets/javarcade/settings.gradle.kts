pluginManagement { includeBuild("gradle/plugins") }

plugins { id("gradle-build") }

javaModules {
    directory("modules") { group = "app.javarcade" }
}
