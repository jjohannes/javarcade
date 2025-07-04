package de.javarca.renderer.lwjgl.test;

import de.javarca.engine.Renderer;
import de.javarca.renderer.lwjgl.LWJGLRenderer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LWJGLRendererTest {

    @Test
    public void testRendererCreation() {
        // Act
        Renderer renderer = new LWJGLRenderer();

        // Assert
        assertNotNull(renderer, "Renderer should not be null");
    }
}