package de.javarca.engine.test;

import de.javarca.engine.GameState;
import de.javarca.engine.Spot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameStateTest {

    @Test
    public void testGameStateInitialization() {
        // Act
        GameState gameState = new GameState();

        // Assert
        assertNotNull(gameState);
        List<Spot> spots = gameState.getSpots();
        assertNotNull(spots);
        assertFalse(spots.isEmpty(), "Spots list should not be empty");
        assertNotNull(gameState.getAll(), "Actor states should not be null");
        assertNotNull(gameState.getImages(), "Images map should not be null");
    }

    @Test
    public void testDirectionFlags() {
        // Arrange
        GameState gameState = new GameState();
        
        // Act & Assert - Initial state
        assertFalse(gameState.isUp(), "Up flag should be false initially");
        assertFalse(gameState.isDown(), "Down flag should be false initially");
        assertFalse(gameState.isLeft(), "Left flag should be false initially");
        assertFalse(gameState.isRight(), "Right flag should be false initially");
        
        // Act & Assert - Setting flags
        gameState.setUp(true);
        gameState.setDown(true);
        gameState.setLeft(true);
        gameState.setRight(true);
        
        // Assert - After setting
        assert(gameState.isUp());
        assert(gameState.isDown());
        assert(gameState.isLeft());
        assert(gameState.isRight());
    }
}