plugins { id("de.javarca.build.java-module") }

dependencies {
  api(project(":javarca-model"))
  implementation("commons-io:commons-io")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
}
