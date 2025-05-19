plugins {
  id("dependency-rules")
  id("repositories")
  id("java-library")
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

testing.suites.named<JvmTestSuite>("test") {
  useJUnitJupiter()
}

dependencies {
  implementation(platform(project(":versions")))
}
