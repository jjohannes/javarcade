plugins { id("com.autonomousapps.dependency-analysis") }

configure<com.autonomousapps.DependencyAnalysisSubExtension> {
  issues { onAny { severity("fail") } }
}
