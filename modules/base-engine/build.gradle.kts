plugins { id("java-module") }

// org.gradlex.java-module-dependencies active

mainModuleInfo { runtimeOnly("org.slf4j.jul") }

tasks.compileJava {
  options.javaModuleMainClass = "app.javarcade.base.engine.Engine"
}
