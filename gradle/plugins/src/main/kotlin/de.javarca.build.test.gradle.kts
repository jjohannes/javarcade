plugins {
  id("java")
  id("org.gradlex.java-module-testing")
}

dependencies {
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations.testImplementation {
  withDependencies {
    removeIf { it.name == "junit-jupiter" }
  }
}
