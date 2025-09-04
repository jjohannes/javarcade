plugins {
  id("de.javarca.build.dependency-rules")
  id("de.javarca.build.repositories")
  id("application")
  id("com.gradleup.shadow") version "9.1.0"
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

tasks.shadowJar {
  configurations = listOf(
    project.configurations.macRuntimeClasspath.get(),
    project.configurations.macArmRuntimeClasspath.get(),
    project.configurations.winRuntimeClasspath.get()
  )
}