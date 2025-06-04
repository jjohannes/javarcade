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
