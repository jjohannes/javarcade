plugins { id("de.javarca.build.java-module") }

dependencies {
  api(project(":javarca-engine"))
  implementation("org.lwjgl:lwjgl-glfw")
  implementation("org.lwjgl:lwjgl-opengl")
  implementation("org.lwjgl:lwjgl-stb")
  implementation("org.lwjgl:lwjgl")
  implementation("org.slf4j:slf4j-api")

  runtimeOnly("org.lwjgl:lwjgl-glfw") {
    capabilities { requireFeature("natives") }
  }
  runtimeOnly("org.lwjgl:lwjgl-opengl") {
    capabilities { requireFeature("natives") }
  }
  runtimeOnly("org.lwjgl:lwjgl-stb") {
    capabilities { requireFeature("natives") }
  }
  runtimeOnly("org.lwjgl:lwjgl") {
    capabilities { requireFeature("natives") }
  }

  testImplementation("org.junit.jupiter:junit-jupiter-api")
}
