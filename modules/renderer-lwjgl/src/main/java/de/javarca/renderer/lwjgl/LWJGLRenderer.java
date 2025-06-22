package de.javarca.renderer.lwjgl;

import de.javarca.engine.GameLoop;
import de.javarca.engine.GameState;
import de.javarca.engine.Renderer;
import de.javarca.engine.Spot;
import de.javarca.renderer.lwjgl.textures.TextureManagement;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static de.javarca.model.GameParameters.CELL_SIZE;
import static de.javarca.model.GameParameters.GAME_HEIGHT;
import static de.javarca.model.GameParameters.GAME_WIDTH;
import static de.javarca.renderer.lwjgl.textures.TextureManagement.saveScreenshot;
import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LWJGLRenderer implements Renderer {
    private static final Logger LOG = LoggerFactory.getLogger(LWJGLRenderer.class);
    private static final String PRESENTATION_FOLDER = System.getenv("PRESENTATION_FOLDER");

    private final GameLoop gameLoop = new GameLoop();
    private final GameState gameState = new GameState();
    private final Map<Character, Integer> images = new HashMap<>();

    private long window;

    public LWJGLRenderer() {}

    @Override
    public void run() {;
        gameLoop.start(gameState);

        init();
        loop();

        gameLoop.stop();

        glfwSwapBuffers(window);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        int SCALE = PRESENTATION_FOLDER == null ? 3 : 1;

        Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // not resizable
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE); // not top bar
        window = glfwCreateWindow(GAME_WIDTH * SCALE, GAME_HEIGHT * SCALE, "Javarcade", NULL /* glfwGetPrimaryMonitor() */, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //noinspection resource
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // this is picked up in rendering loop
            }

            if (action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_UP -> gameState.setUp(true);
                    case GLFW_KEY_DOWN -> gameState.setDown(true);
                    case GLFW_KEY_LEFT -> gameState.setLeft(true);
                    case GLFW_KEY_RIGHT -> gameState.setRight(true);
                    case GLFW_KEY_X -> gameState.action();
                }
            }
            ;

            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_UP -> gameState.setUp(false);
                    case GLFW_KEY_DOWN -> gameState.setDown(false);
                    case GLFW_KEY_LEFT -> gameState.setLeft(false);
                    case GLFW_KEY_RIGHT -> gameState.setRight(false);
                }
            }
            ;
        });

        glfwMakeContextCurrent(window); // make OpenGL context current
        glfwSwapInterval(1); // enable v-sync
        if (PRESENTATION_FOLDER == null) {
            glfwShowWindow(window); // Make window visible
        }
        createCapabilities();
        // var vidmode = requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
        // glViewport(0, 0, vidmode.height(), vidmode.height());
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, GAME_WIDTH, GAME_HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        gameState.getImages().forEach((symbol, content) -> images.put(symbol, TextureManagement.loadTexture(content)));
    }

    private void loop() {
        glClearColor(0f, 0f, 0f, 0.0f);

        final int targetFPS = PRESENTATION_FOLDER == null ? 60 : 10;
        final long frameTime = 1_000_000_000 / targetFPS;

        while (!glfwWindowShouldClose(window)) {
            long startTime = System.nanoTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            gameState.getSpots().forEach(this::drawSpot);
            glfwPollEvents();
            glfwSwapBuffers(window);

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            long sleepTime = frameTime - elapsedTime;

            saveScreenshot(PRESENTATION_FOLDER, 2 * GAME_WIDTH, 2 * GAME_HEIGHT);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void drawSpot(Spot s) {
        drawSprite(
                ensureTexture(s),
                s.getPixelPositionX(CELL_SIZE),
                s.getPixelPositionY(CELL_SIZE));
    }

    private Integer ensureTexture(Spot spot) {
        if (spot.getSymbol() == '.') {
            return null;
        }
        return images.computeIfAbsent(spot.getAssetSymbol(), TextureManagement::renderCharacter);
    }

    private void drawSprite(Integer img, float x, float y) {
        if (img == null || x == Float.MIN_VALUE) {
            return;
        }
        TextureManagement.renderTexture(img, x, y, CELL_SIZE, CELL_SIZE);
    }
}
