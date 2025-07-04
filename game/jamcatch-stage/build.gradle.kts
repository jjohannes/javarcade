plugins { id("de.javarca.build.java-module") }

dependencies {
  api(project(":javarca-model"))
  implementation("org.slf4j:slf4j-api")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
}
