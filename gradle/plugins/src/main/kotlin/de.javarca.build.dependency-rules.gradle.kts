import org.gradle.nativeplatform.MachineArchitecture.*
import org.gradle.nativeplatform.OperatingSystemFamily.*

plugins {
  id("org.gradlex.jvm-dependency-conflict-resolution")
  id("org.gradlex.java-module-packaging")
}

jvmDependencyConflicts {
  conflictResolution {
    select("org.gradlex:slf4j-impl", "org.slf4j:slf4j-simple")
  }

  patch {
    listOf("", "-glfw", "-opengl", "-stb").forEach { module ->
      module("org.lwjgl:lwjgl$module") {
        addTargetPlatformVariant("natives",
          "natives-linux", LINUX, X86_64)
        addTargetPlatformVariant("natives",
          "natives-linux-arm64", LINUX, ARM64)
        addTargetPlatformVariant("natives",
          "natives-macos", MACOS, X86_64)
        addTargetPlatformVariant("natives",
          "natives-macos-arm64", MACOS, ARM64)
        addTargetPlatformVariant("natives",
          "natives-windows", WINDOWS, X86_64)
        addTargetPlatformVariant("natives",
          "natives-windows-arm64", WINDOWS, ARM64)
      }
    }
  }

  consistentResolution {
    platform(":versions")
  }
}

version = "1"
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
}
