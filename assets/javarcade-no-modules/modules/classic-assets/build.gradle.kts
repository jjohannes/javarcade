plugins { id("java-module") }

dependencies {
    api(project(":base-model"))
    implementation("commons-io:commons-io")
}
