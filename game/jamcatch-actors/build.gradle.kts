plugins { id("de.javarca.build.java-module") }

dependencies {
  api(project(":javarca-model"))
  implementation("org.apache.commons:commons-csv")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
}

