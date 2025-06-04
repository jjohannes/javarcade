import org.gradle.nativeplatform.MachineArchitecture.*
import org.gradle.nativeplatform.OperatingSystemFamily.*

plugins {
  id("dependency-rules")
  id("repositories")
  id("compile")
  id("test")
  id("quality-check")
  id("java-library")
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
