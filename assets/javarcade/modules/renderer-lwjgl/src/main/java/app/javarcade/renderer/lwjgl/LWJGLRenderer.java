package app.javarcade.renderer.lwjgl;

import static app.javarcade.base.engine.GameParameters.CELL_HEIGHT_IN_PIXEL;
import static app.javarcade.base.engine.GameParameters.CELL_WIDTH_IN_PIXEL;
import static app.javarcade.base.engine.GameParameters.HEIGHT_IN_PIXEL;
import static app.javarcade.base.engine.GameParameters.WIDTH_IN_PIXEL;
import static app.javarcade.renderer.lwjgl.textures.TextureManagement.saveScreenshot;
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
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
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
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import app.javarcade.base.engine.GameLoop;
import app.javarcade.base.engine.GameState;
import app.javarcade.base.engine.Renderer;
import app.javarcade.base.engine.Spot;
import app.javarcade.renderer.lwjgl.textures.TextureManagement;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        saveScreenshot(PRESENTATION_FOLDER, 2 * WIDTH_IN_PIXEL, 2 * HEIGHT_IN_PIXEL);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // not resizable
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE); // not top bar
        window = glfwCreateWindow(WIDTH_IN_PIXEL, HEIGHT_IN_PIXEL, "Javarcade", NULL, NULL);
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

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            var pWidth = stack.mallocInt(1); // int*
            var pHeight = stack.mallocInt(1); // int*
            glfwGetWindowSize(window, pWidth, pHeight); // pass window size to glfwCreateWindow
            var vidmode = requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor())); // resolution of primary monitor
            var centerX = (vidmode.width() - pWidth.get(0)) / 2;
            var centerY = (vidmode.height() - pHeight.get(0)) / 2;
            glfwSetWindowPos(window, centerX, centerY); // center
        }
        glfwMakeContextCurrent(window); // make OpenGL context current
        glfwSwapInterval(1); // enable v-sync
        if (PRESENTATION_FOLDER == null) {
            glfwShowWindow(window); // Make window visible
        }
        createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH_IN_PIXEL, HEIGHT_IN_PIXEL, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        gameState.getImages().forEach((symbol, content) -> images.put(symbol, TextureManagement.loadTexture(content)));
        gameState.getSpots().forEach(this::ensureTexture);
    }

    private void loop() {
        glClearColor(0.8f, 0.8f, 0.8f, 0.0f);

        final int targetFPS = 60;
        final long frameTime = 1_000_000_000 / targetFPS;

        while (!glfwWindowShouldClose(window)) {
            long startTime = System.nanoTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            gameState
                    .getSpots()
                    .forEach(s -> drawSprite(
                            images.get(s.getSymbol()),
                            s.getPixelPositionX(CELL_WIDTH_IN_PIXEL),
                            s.getPixelPositionY(CELL_HEIGHT_IN_PIXEL)));
            glfwPollEvents();
            glfwSwapBuffers(window);

            if (PRESENTATION_FOLDER != null) {
                break;
            }

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            long sleepTime = frameTime - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void ensureTexture(Spot spot) {
        if ('.' == spot.getSymbol()) {
            return;
        }
        images.computeIfAbsent(spot.getSymbol(), TextureManagement::renderCharacter);
    }

    private void drawSprite(Integer img, float x, float y) {
        if (img == null) {
            return;
        }
        TextureManagement.renderTexture(img, x, y, CELL_WIDTH_IN_PIXEL, CELL_HEIGHT_IN_PIXEL);
    }
}
