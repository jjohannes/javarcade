plugins {
  id("java-platform")
}

javaPlatform { allowDependencies() }

dependencies {
  api(platform("org.lwjgl:lwjgl-bom:3.4.3"))
  api(platform("org.slf4j:slf4j-bom:2.0.20"))
  api(platform("org.junit:junit-bom:6.1.3"))
}

dependencies.constraints {
  // org.apache.commons.io
  api("commons-io:commons-io:2.16.1")
  // org.apache.commons.csv
  api("org.apache.commons:commons-csv:1.14.1")
}
