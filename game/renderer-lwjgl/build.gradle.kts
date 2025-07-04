plugins { id("de.javarca.build.java-module") }

dependencies {
  api(project(":javarca-engine"))
  implementation("org.lwjgl:lwjgl-glfw")
  implementation("org.lwjgl:lwjgl-opengl")
  implementation("org.lwjgl:lwjgl-stb")
  implementation("org.lwjgl:lwjgl")
  implementation("org.slf4j:slf4j-api")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
}
