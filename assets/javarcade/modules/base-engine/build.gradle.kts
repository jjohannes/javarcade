plugins {
    id("java-module")
    id("application")
}

// org.gradlex.java-module-dependencies activated

application { mainClass = "app.javarcade.base.engine.Engine" }

mainModuleInfo { runtimeOnly("org.slf4j.jul") }
