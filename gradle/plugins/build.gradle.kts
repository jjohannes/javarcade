plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:2.19.0")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.4")
    implementation("org.gradlex:java-module-dependencies:1.9.2")
    implementation("org.gradlex:java-module-packaging:1.1")
}
