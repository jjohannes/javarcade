plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:3.5.1")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.5")
    implementation("org.gradlex:java-module-dependencies:1.12")
    implementation("org.gradlex:java-module-packaging:1.2")
    implementation("org.gradlex:java-module-testing:1.8")
}
