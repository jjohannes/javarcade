pluginManagement { includeBuild("gradle/plugins") }
plugins { id("de.javarca.build") }

rootProject.name = "javarcade"

javaModules {
  directory("engine") { group = "de.javarca" }
  directory("game") { group = "de.javarca" }
  versions("gradle/versions")

  module("apps/app-jamcatch")
}
