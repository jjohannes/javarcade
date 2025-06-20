plugins { id("java-module") }

// org.gradlex.java-module-dependencies active

mainModuleInfo { runtimeOnly("org.slf4j.jul") }

tasks.compileJava {
  options.javaModuleMainClass = "de.javarca.base.engine.Engine"
}
