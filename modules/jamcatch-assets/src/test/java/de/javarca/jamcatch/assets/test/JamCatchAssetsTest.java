package de.javarca.jamcatch.assets.test;

import de.javarca.jamcatch.assets.JamCatchAssets;
import de.javarca.model.Asset;
import de.javarca.model.AssetSet;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JamCatchAssetsTest {

    @Test
    public void testAssetSetCreation() {
        // Act
        AssetSet assetSet = new JamCatchAssets();

        // Assert
        assertNotNull(assetSet);
        Set<Asset> assets = assetSet.assets();
        assertNotNull(assets);
        assertFalse(assets.isEmpty(), "Asset set should not be empty");
    }
}