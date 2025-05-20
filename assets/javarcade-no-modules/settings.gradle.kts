pluginManagement { includeBuild("../javarcade/gradle/plugins") }

plugins {
    id("com.autonomousapps.build-health") version "2.15.0"
}

File("modules").listFiles().forEach {
    if (File(it, "build.gradle.kts").exists()) {
        include(":${it.name}")
        project(":${it.name}").projectDir = file(it)
    }
}

include(":versions")
project(":versions").projectDir = file("../javarcade/gradle/versions")

// Ignore module-info.java files
gradle.lifecycle.beforeProject {
    plugins.withId("java") {
        the<JavaPluginExtension>().modularity.inferModulePath = false
        the<SourceSetContainer>().named("main") {
            java {
                setSrcDirs(java.sourceDirectories.map {
                    it.absolutePath.replace("/javarcade-no-modules/", "/javarcade/")
                })
                exclude("module-info.java")
            }
        }
    }
}
