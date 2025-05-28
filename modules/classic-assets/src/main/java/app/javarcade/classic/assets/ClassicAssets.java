package app.javarcade.classic.assets;

import static java.util.Objects.requireNonNull;

import app.javarcade.base.model.Asset;
import app.javarcade.base.model.AssetSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ClassicAssets implements AssetSet {

    private final Set<Asset> assets;

    public ClassicAssets() {
        assets = Set.of(
                new Asset('.', readImage("floor")),
                new Asset('x', readImage("wall")),
                new Asset('O', readImage("solid")),
                new Asset('1', readImage("player1")),
                new Asset('2', readImage("player2")),
                new Asset('3', readImage("player3")),
                new Asset('4', readImage("player4")));
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
