pluginManagement { includeBuild("gradle/plugins") }
plugins { id("de.javarca.build") }

rootProject.name = "javarcade"

javaModules {
  directory("modules") { group = "de.javarca" }
  versions("gradle/versions")

  module("apps/app-jamcatch")
}

gradle.lifecycle.beforeProject {
  plugins.withId("java-library") {
    tasks.named("check") {
      dependsOn("checkAllModuleInfo")
    }
  }
}
