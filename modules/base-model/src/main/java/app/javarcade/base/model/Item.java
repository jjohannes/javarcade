package app.javarcade.base.model;

import java.util.Set;

public record Item(char symbol, Set<PlayerPropertyModifier> modifiers) {}
