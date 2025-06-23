import de.javarca.model.ActorSet;
import de.javarca.model.Stage;

module de.javarca.engine {
  requires transitive de.javarca.model;
  requires org.slf4j;

  exports de.javarca.engine;

  uses de.javarca.model.AssetSet;
  uses Stage;
  uses ActorSet;
  uses de.javarca.engine.Renderer;
}
