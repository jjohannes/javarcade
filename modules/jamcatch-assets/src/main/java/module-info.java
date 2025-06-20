module de.javarca.classic.assets {
  requires transitive de.javarca.base.model;
  requires org.apache.commons.io;

  exports de.javarca.jamcatch.assets;

  provides de.javarca.base.model.AssetSet with
          de.javarca.jamcatch.assets.JamCatchAssets;
}
