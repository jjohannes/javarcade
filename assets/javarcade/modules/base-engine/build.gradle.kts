plugins { id("org.example.gradle.component.application") }

application { mainClass = "app.javarcade.base.engine.Engine" }

mainModuleInfo { runtimeOnly("org.slf4j.jul") }
