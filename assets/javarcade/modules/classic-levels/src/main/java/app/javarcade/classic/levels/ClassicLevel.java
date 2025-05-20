package app.javarcade.classic.levels;

import app.javarcade.base.model.Level;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassicLevel implements Level {
    private static final Logger LOG = LoggerFactory.getLogger(ClassicLevel.class);

    public ClassicLevel() {}

    @Override
    public String define() {
        LOG.debug("Constructing a classic level");
        return """
                1 . . x x x x x x x x . . 3
                . O x O x O x x O x O x O .
                . x . O O O O O O O O . x .
                x O . x x x x x x x x . O .
                x x x x x x O O x x x x x x
                x O . O x . . . . x O . O x
                x x x x x O x x O x x x x x
                x x x x x O x x O x x x x x
                x O . O x . . . . x O . O x
                x x x x x x O O x x x . x x
                x O . x x x x x x x x . O x
                . x . O O O O O O O O . x .
                . O x O x O x x O x O x O .
                4 . . x x x x x x x x . . 2
                """;
    }

    @Override
    public Set<Character> blocking() {
        return Set.of('x');
    }

    @Override
    public Set<Character> destructible() {
        return Set.of('O');
    }
}
