package de.javarca.jamcatch.actors.test;

import de.javarca.jamcatch.actors.JamCatchActorSet;
import de.javarca.model.Actor;
import de.javarca.model.ActorSet;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JamCatchActorSetTest {

    @Test
    public void testActorSetCreation() {
        // Act
        ActorSet actorSet = new JamCatchActorSet();

        // Assert
        assertNotNull(actorSet);
        Set<Actor> actors = actorSet.items();
        assertNotNull(actors);
        assertFalse(actors.isEmpty(), "Actor set should not be empty");
    }
}