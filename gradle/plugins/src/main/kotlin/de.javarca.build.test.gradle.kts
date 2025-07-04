plugins {
  id("java")
  id("org.gradlex.java-module-testing")
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
