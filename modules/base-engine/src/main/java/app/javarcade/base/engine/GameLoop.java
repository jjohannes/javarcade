package app.javarcade.base.engine;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private ScheduledExecutorService exec;
    private GameState gameState;

    public GameLoop() {}

    public ScheduledFuture<?> start(GameState gameState) {
        this.gameState = gameState;
        exec = Executors.newSingleThreadScheduledExecutor();
        return exec.scheduleAtFixedRate(this::update, 0, 20, TimeUnit.MILLISECONDS);
    }

    private void update() {
        if (gameState.isUp()) {
            gameState.getPlayer().moveUp(gameState.getSpots());
        }
        if (gameState.isDown()) {
            gameState.getPlayer().moveDown(gameState.getSpots());
        }
        if (gameState.isLeft()) {
            gameState.getPlayer().moveLeft(gameState.getSpots());
        }
        if (gameState.isRight()) {
            gameState.getPlayer().moveRight(gameState.getSpots());
        }
    }

    public void stop() {
        exec.shutdown();
    }
}
