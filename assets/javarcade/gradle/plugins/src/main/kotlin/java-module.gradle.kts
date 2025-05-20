plugins {
  id("dependency-rules")
  id("repositories")
  id("java-library")
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  implementation(platform(project(":versions")))
}
