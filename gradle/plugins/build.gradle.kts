plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:3.4.1")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.4")
    implementation("org.gradlex:java-module-dependencies:1.11")
    implementation("org.gradlex:java-module-packaging:1.1")
    implementation("org.gradlex:java-module-testing:1.8")
}
