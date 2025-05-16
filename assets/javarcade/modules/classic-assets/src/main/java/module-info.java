module app.javarcade.classic.assets {
    requires transitive app.javarcade.base.model;
    requires org.apache.commons.io;

    exports app.javarcade.classic.assets;

    provides app.javarcade.base.model.AssetSet with
            app.javarcade.classic.assets.ClassicAssets;
}
