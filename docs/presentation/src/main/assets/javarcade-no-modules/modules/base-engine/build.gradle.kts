plugins { id("java-module") }

dependencies {
    api(project(":base-model"))
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.slf4j:slf4j-jdk14")
}
