package app.javarcade.classic.levels;

import app.javarcade.base.model.Level;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JamCatchStage implements Level {
    private static final Logger LOG = LoggerFactory.getLogger(JamCatchStage.class);

    public JamCatchStage() {}

    @Override
    public String define() {
        LOG.debug("Constructing a classic level");
        return """
                x . . . . ☼ . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . ☼ . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . x . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . x . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                x . . . p . . . . . . . . . . x
                x . . . . . . . . . . . . . . x
                """;
    }
}
