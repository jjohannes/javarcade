module de.javarca.classic.items {
  requires transitive de.javarca.model;
  requires org.apache.commons.csv;

  exports de.javarca.jamcatch.actors;

  provides de.javarca.model.ActorSet with
          de.javarca.jamcatch.actors.JamCatchActorSet;
}
