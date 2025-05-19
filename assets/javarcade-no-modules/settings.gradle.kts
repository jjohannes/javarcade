pluginManagement { includeBuild("../javarcade/gradle/plugins") }

include(":base-engine")
include(":base-model")
include(":classic-assets")
include(":classic-items")
include(":classic-levels")
include(":renderer-lwjgl")
project(":base-engine").projectDir = file("modules/base-engine")
project(":base-model").projectDir = file("modules/base-model")
project(":classic-assets").projectDir = file("modules/classic-assets")
project(":classic-items").projectDir = file("modules/classic-items")
project(":classic-levels").projectDir = file("modules/classic-levels")
project(":renderer-lwjgl").projectDir = file("modules/renderer-lwjgl")

include(":versions")
project(":versions").projectDir = file("../javarcade/gradle/versions")

gradle.lifecycle.beforeProject {
    plugins.withId("java") {
        the<JavaPluginExtension>().modularity.inferModulePath = false
        the<SourceSetContainer>().named("main") {
            java { exclude("module-info.java") }
        }
    }
}
