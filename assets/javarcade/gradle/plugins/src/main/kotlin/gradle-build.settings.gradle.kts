import org.gradlex.javamodule.dependencies.initialization.JavaModulesExtension

plugins { id("org.gradlex.java-module-dependencies") }

configure<JavaModulesExtension> { versions("gradle/versions") }
