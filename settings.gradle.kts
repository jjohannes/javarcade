pluginManagement { includeBuild("gradle/plugins") }
plugins { id("de.javarca.build") }

File("engine").listFiles()!!.forEach {
  if (File(it, "build.gradle.kts").exists()) {
    include(":${it.name}")
    project(":${it.name}").projectDir = file(it)
  }
}
File("game").listFiles()!!.forEach {
  if (File(it, "build.gradle.kts").exists()) {
    include(":${it.name}")
    project(":${it.name}").projectDir = file(it)
  }
}
File("apps").listFiles()!!.forEach {
  if (File(it, "build.gradle.kts").exists()) {
    include(":${it.name}")
    project(":${it.name}").projectDir = file(it)
  }
}

include(":versions")
project(":versions").projectDir = file("gradle/versions")
