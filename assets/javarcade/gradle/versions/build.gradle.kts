plugins { id("java-platform") }

dependencies {
    api(platform("org.lwjgl:lwjgl-bom:3.3.6"))
    api(platform("org.slf4j:slf4j-bom:2.0.17"))
}

dependencies.constraints {
    api("commons-io:commons-io:2.16.1") { because("org.apache.commons.io") }
    api("org.apache.commons:commons-csv:1.14.0") { because("org.apache.commons.csv") }
}
