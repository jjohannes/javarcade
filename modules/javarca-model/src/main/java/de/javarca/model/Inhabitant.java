package de.javarca.model;

import java.util.Map;
import java.util.Set;

public record Inhabitant(char symbol,
                         Set<InhabitantPropertyModifier> modifiers,
                         Map<Character, InhabitantCollision> collisionFunctions) {}
