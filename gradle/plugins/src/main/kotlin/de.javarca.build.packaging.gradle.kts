import org.gradle.nativeplatform.MachineArchitecture.*
import org.gradle.nativeplatform.OperatingSystemFamily.*

plugins {
  id("org.gradlex.java-module-packaging")
}

version = "1.0"

javaModulePackaging {
  target("mac") {
    operatingSystem = MACOS
    architecture = X86_64
    packageTypes = listOf("dmg")
  }
  target("macArm") {
    operatingSystem = MACOS
    architecture = ARM64
    packageTypes = listOf("dmg")
  }
  target("win") {
    operatingSystem = WINDOWS
    architecture = X86_64
    packageTypes = listOf("exe")
  }
  jlinkOptions.add("--bind-services")
}
