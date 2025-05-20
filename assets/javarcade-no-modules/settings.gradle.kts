pluginManagement { includeBuild("../javarcade/gradle/plugins") }

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
            java { exclude("module-info.java") }
        }
    }
}
