plugins {
  id("dependency-rules")
  id("repositories")
  id("distribution")
  id("application")
  id("java")
}

application {
  mainModule = "app.javarcade.base.engine"
  mainClass = "app.javarcade.base.engine.Engine"
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
  runtimeOnly(project(":classic-assets"))
  runtimeOnly(project(":classic-items"))
  runtimeOnly(project(":classic-levels"))
  runtimeOnly("org.slf4j:slf4j-simple")
}
