package de.javarca.model.test;

import de.javarca.model.Actor;
import de.javarca.model.ActorCollision;
import de.javarca.model.ActorPropertyModifier;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActorTest {

    @Test
    public void testActorCreation() {
        // Arrange
        char symbol = 'A';
        Set<ActorPropertyModifier> modifiers = new HashSet<>();
        Map<Character, ActorCollision> collisionFunctions = new HashMap<>();

        // Act
        Actor actor = new Actor(symbol, modifiers, collisionFunctions);

        // Assert
        assertEquals('C', actor.symbol());
        assertEquals(modifiers, actor.modifiers());
        assertEquals(collisionFunctions, actor.collisionFunctions());
    }
}