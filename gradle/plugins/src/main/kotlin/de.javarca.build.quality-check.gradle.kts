import com.autonomousapps.DependencyAnalysisSubExtension
import org.gradlex.javamodule.dependencies.tasks.ModuleDirectivesScopeCheck

plugins {
  id("base")
  id("com.autonomousapps.dependency-analysis")
}

configure<DependencyAnalysisSubExtension> {
  issues { onAny { severity("fail") } }
}
tasks.check {
  dependsOn(tasks.withType<ModuleDirectivesScopeCheck>())
}
