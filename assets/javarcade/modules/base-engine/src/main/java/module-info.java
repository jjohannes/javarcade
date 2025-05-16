module app.javarcade.base.engine {
    requires app.javarcade.base.model;
    requires org.slf4j;

    exports app.javarcade.base.engine;

    uses app.javarcade.base.model.AssetSet;
    uses app.javarcade.base.model.Level;
    uses app.javarcade.base.model.ItemSet;
    uses app.javarcade.base.engine.Renderer;
}
