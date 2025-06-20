plugins {
  id("dependency-rules")
  id("repositories")
  id("distribution")
  id("application")
  id("java")
}

application {
  mainModule = "de.javarca.base.engine"
  mainClass = "de.javarca.base.engine.Engine"
}

distributions.main {
  contents.from(configurations.runtimeClasspath)
}

tasks.assemble {
  dependsOn(tasks.installDist)
}

dependencies {
  runtimeOnly(project(":base-engine"))
  runtimeOnly(project(":renderer-lwjgl"))
  runtimeOnly(project(":jamcatch-assets"))
  runtimeOnly(project(":jamcatch-inhabitants"))
  runtimeOnly(project(":jamcatch-stage"))
  runtimeOnly("org.slf4j:slf4j-simple")
}
