package app.javarcade.base.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameLoop {

    private ScheduledExecutorService exec;
    private GameState gameState;

    public GameLoop() {
    }

    public ScheduledFuture<?> start(GameState gameState) {
        this.gameState = gameState;
        exec = Executors.newSingleThreadScheduledExecutor();
        return exec.scheduleAtFixedRate(this::update, 0, 20, TimeUnit.MILLISECONDS);
    }

    private void update() {
        for (Spot item : gameState.getItems()) {
            if (item.playerControlled()) {
                if (gameState.isUp()) {
                    item.moveUp(gameState.getSpots());
                }
                if (gameState.isDown()) {
                    item.moveY(gameState.getSpots());
                }
                if (gameState.isLeft()) {
                    item.moveLeft(gameState.getSpots());
                }
                if (gameState.isRight()) {
                    item.moveX(gameState.getSpots());
                }
            } else {
                item.moveY(gameState.getSpots());
                item.moveX(gameState.getSpots());
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }
}
