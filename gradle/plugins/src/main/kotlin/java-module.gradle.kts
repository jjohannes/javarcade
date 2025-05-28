import org.gradle.nativeplatform.MachineArchitecture.*
import org.gradle.nativeplatform.OperatingSystemFamily.*

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

sourceSets.configureEach {
  configurations[runtimeClasspathConfigurationName].attributes {
    attribute(OPERATING_SYSTEM_ATTRIBUTE, objects.named<OperatingSystemFamily>(MACOS))
    attribute(ARCHITECTURE_ATTRIBUTE, objects.named<MachineArchitecture>(ARM64))
  }
}

dependencies {
  implementation(platform(project(":versions")))
}
