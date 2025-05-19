plugins { id("java-module") }

dependencies {
    implementation(project(":base-model"))
    runtimeOnly("org.slf4j:org.slf4j.jdk14")
}
