plugins {
  id("java")
  id("org.gradlex.jvm-dependency-conflict-resolution")
}

jvmDependencyConflicts {
  conflictResolution {
    select("org.gradlex:slf4j-impl", "org.slf4j:slf4j-simple")
  }
  consistentResolution {
    platform(":versions")
  }
}
