module de.javarca.classic.items {
  requires transitive de.javarca.model;
  requires org.apache.commons.csv;
  requires org.slf4j;

  exports de.javarca.jamcatch.inhabitants;

  provides de.javarca.model.InhabitantSet with
          de.javarca.jamcatch.inhabitants.JamCatchInhabitantSet;
}
