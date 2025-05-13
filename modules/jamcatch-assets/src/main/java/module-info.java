module de.javarca.classic.assets {
  requires transitive de.javarca.model;
  requires org.apache.commons.io;

  exports de.javarca.jamcatch.assets;

  provides de.javarca.model.AssetSet with
          de.javarca.jamcatch.assets.JamCatchAssets;
}
