plugins {
  id("java")
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
