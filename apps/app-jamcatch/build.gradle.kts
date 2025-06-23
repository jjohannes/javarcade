plugins {
  id("de.javarca.build.dependency-rules")
  id("de.javarca.build.repositories")
  id("application")
}

application {
  mainModule = "de.javarca.engine"
  mainClass = "de.javarca.engine.Engine"
}

tasks.jar {
  enabled = false
}

tasks.assemble {
  dependsOn(tasks.installDist)
}

dependencies {
  runtimeOnly(project(":javarca-engine"))
  runtimeOnly(project(":renderer-lwjgl"))
  runtimeOnly(project(":jamcatch-assets"))
  runtimeOnly(project(":jamcatch-actors"))
  runtimeOnly(project(":jamcatch-stage"))
  runtimeOnly("org.slf4j:slf4j-simple")
}
