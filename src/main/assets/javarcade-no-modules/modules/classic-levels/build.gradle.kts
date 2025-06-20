plugins { id("java-module") }

dependencies {
  api(project(":base-model"))
  implementation("org.slf4j:slf4j-api")
}
