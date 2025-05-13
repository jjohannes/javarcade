module de.javarca.jamcatch.stage {
  requires transitive de.javarca.model;
  requires org.slf4j;

  exports de.javarca.jamcatch.stage;

  provides de.javarca.model.Stage with
          de.javarca.jamcatch.stage.JamCatchStage;
}
