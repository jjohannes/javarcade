package de.javarca.jamcatch.assets;

import de.javarca.model.Asset;
import de.javarca.model.AssetSet;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class JamCatchAssets implements AssetSet {

    private final Set<Asset> assets;

    public JamCatchAssets() {
        assets = Set.of(
                new Asset('.', readImage("bg")),
                new Asset('p', readImage("catcher")),
                new Asset('x', readImage("wall")),
                new Asset('X', readImage("solid")),
                new Asset('J', readImage("jar_0")),
                new Asset('K', readImage("jar_1")),
                new Asset('L', readImage("jar_2")),
                new Asset('M', readImage("jar_3")),
                new Asset('N', readImage("jar_4")),
                new Asset('O', readImage("jar_5")));
    }

    private byte[] readImage(String name) {
        try (InputStream is = getClass().getResourceAsStream("res/" + name + ".png")) {
            return IOUtils.toByteArray(requireNonNull(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Asset> assets() {
        return assets;
    }
}
