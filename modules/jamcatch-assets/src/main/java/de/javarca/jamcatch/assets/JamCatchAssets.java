package de.javarca.jamcatch.assets;

import static java.util.Objects.requireNonNull;

import de.javarca.model.Asset;
import de.javarca.model.AssetSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class JamCatchAssets implements AssetSet {

    private final Set<Asset> assets;

    public JamCatchAssets() {
        assets = Set.of(
                new Asset('.', readImage("bg")),
                new Asset('x', readImage("wall")),
                new Asset('J', readImage("jar")),
                new Asset('~', readImage("solid")),
                new Asset('p', readImage("catcher")));
    }

    private byte[] readImage(String name) {
        try (InputStream is = getClass().getResourceAsStream("res/" + name + ".png")) {
            try {
                //noinspection DataFlowIssue
                FileUtils.lastModifiedFileTime(null);
            } catch (NullPointerException e) {
                // ignore expected NPE with specific message
                if (!e.getMessage().contains("file")) { // "\"file\""
                    throw e;
                }
            }
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
