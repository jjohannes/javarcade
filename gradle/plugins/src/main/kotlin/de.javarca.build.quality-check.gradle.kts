import com.autonomousapps.DependencyAnalysisSubExtension

plugins {
  id("base")
  id("com.autonomousapps.dependency-analysis")
}

configure<DependencyAnalysisSubExtension> {
  issues { onAny { severity("fail") } }
}
tasks.check {
  dependsOn(tasks.named { it == "projectHealth" })
}
