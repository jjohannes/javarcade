plugins { id("java-module") }

dependencies {
    api(project(":base-engine"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.slf4j:slf4j-api")
}
