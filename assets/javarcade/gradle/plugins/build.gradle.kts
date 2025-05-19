plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.3")
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:2.15.0")
}
