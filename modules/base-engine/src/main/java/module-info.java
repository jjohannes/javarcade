import de.javarca.base.model.Stage;

module de.javarca.base.engine {
  requires transitive de.javarca.base.model;
  requires org.slf4j;

  exports de.javarca.base.engine;

  uses de.javarca.base.model.AssetSet;
  uses Stage;
  uses de.javarca.base.model.InhabitantSet;
  uses de.javarca.base.engine.Renderer;
}
