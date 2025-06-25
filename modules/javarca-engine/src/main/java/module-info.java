module de.javarca.engine {
  requires transitive de.javarca.model;
  requires org.slf4j;

  exports de.javarca.engine;

  uses de.javarca.model.AssetSet;
  uses de.javarca.model.Stage;
  uses de.javarca.model.ActorSet;
  uses de.javarca.engine.Renderer;
}
