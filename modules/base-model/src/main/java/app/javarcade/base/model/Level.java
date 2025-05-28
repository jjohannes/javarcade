package app.javarcade.base.model;

import java.util.Set;

public interface Level {
    String define();

    default Set<Character> blocking() {
        return Set.of();
    }
    ;

    default Set<Character> destructible() {
        return Set.of();
    }
    ;
}
