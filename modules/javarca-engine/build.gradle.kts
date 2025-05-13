plugins { id("de.javarca.build.java-module") }

// org.gradlex.java-module-dependencies

mainModuleInfo {
  runtimeOnly("org.slf4j.jul")
}

tasks.compileJava {
  options.javaModuleMainClass = "de.javarca.engine.Engine"
}
