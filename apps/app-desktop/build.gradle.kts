plugins {
  id("dependency-rules")
  id("repositories")
  id("distribution")
  id("java")
}

distributions.main {
  contents.from(configurations.runtimeClasspath)
}

dependencies {
  runtimeOnly(project(":base-engine"))
  runtimeOnly(project(":renderer-lwjgl"))
  runtimeOnly(project(":classic-assets"))
  runtimeOnly(project(":classic-items"))
  runtimeOnly(project(":classic-levels"))
  runtimeOnly("org.slf4j:slf4j-simple")
}
