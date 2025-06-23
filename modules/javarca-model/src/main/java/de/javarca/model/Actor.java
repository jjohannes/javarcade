package de.javarca.model;

import java.util.Map;
import java.util.Set;

public record Actor(char symbol,
                    Set<ActorPropertyModifier> modifiers,
                    Map<Character, ActorCollision> collisionFunctions) {}
