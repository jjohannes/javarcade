plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:2.18.0")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.4")
}
