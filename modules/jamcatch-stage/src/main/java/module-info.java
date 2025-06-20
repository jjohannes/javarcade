module de.javarca.jamcatch.stage {
  requires transitive de.javarca.base.model;
  requires org.slf4j;

  exports de.javarca.jamcatch.stage;

  provides de.javarca.base.model.Stage with
          de.javarca.jamcatch.stage.JamCatchStage;
}
