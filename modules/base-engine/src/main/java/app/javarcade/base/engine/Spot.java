package app.javarcade.base.engine;

import static app.javarcade.base.engine.GameParameters.MATRIX_HEIGHT;
import static app.javarcade.base.engine.GameParameters.MATRIX_WIDTH;

import app.javarcade.base.model.Level;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Spot {
    public static final int PRECISION = 1000;

    private final char symbol;
    private final boolean blocking;
    private final boolean destructible;

    private int x;
    private int y;

    private int speed = 150;

    public static Stream<Spot> render(Level level) {
        List<Integer> symbols = Arrays.stream(level.define().replace(" ", "").split("\n"))
                .flatMap(line -> line.chars().boxed())
                .toList();

        return IntStream.range(0, MATRIX_WIDTH * MATRIX_HEIGHT).mapToObj(p -> {
            char symbol = p < symbols.size() ? (char) symbols.get(p).intValue() : ']';
            return new Spot(
                    symbol,
                    p,
                    level.blocking().contains(symbol),
                    level.destructible().contains(symbol));
        });
    }

    public Spot(char symbol, int x, int y, boolean blocking, boolean destructible) {
        this.symbol = symbol;
        this.x = PRECISION * x;
        this.y = PRECISION * y;
        this.blocking = blocking;
        this.destructible = destructible;
    }

    public Spot(char symbol, int posInStream, boolean blocking, boolean destructible) {
        this(
                symbol,
                posInStream - (posInStream / MATRIX_WIDTH) * MATRIX_WIDTH,
                posInStream / MATRIX_WIDTH,
                blocking,
                destructible);
    }

    public char getSymbol() {
        return symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getPixelPositionX(int cellWidthInPixel) {
        return (x * cellWidthInPixel) * 1f / PRECISION;
    }

    public float getPixelPositionY(int cellHeightInPixel) {
        return (y * cellHeightInPixel) * 1f / PRECISION;
    }

    private int scale(int s) {
        return s / PRECISION;
    }

    private boolean blocks() {
        return blocking || destructible;
    }

    public void moveRight(List<Spot> allSpots) {
        if (allSpots.stream()
                .anyMatch(s -> scale(s.getX()) == scale(x) + 1 && scale(s.getY()) == scale(y) && s.blocks())) {
            return;
        }
        x += speed;
        if (x >= (MATRIX_WIDTH - 1) * PRECISION) {
            x = (MATRIX_WIDTH - 1) * PRECISION;
        }
    }

    public void moveLeft(List<Spot> allSpots) {
        if (allSpots.stream().anyMatch(s -> scale(s.getX()) == scale(x) && scale(s.getY()) == scale(y) && s.blocks())) {
            return;
        }
        x -= speed;
        if (x < 0) {
            x = 0;
        }
    }

    public void moveDown(List<Spot> allSpots) {
        if (allSpots.stream()
                .anyMatch(s -> scale(s.getX()) == scale(x) && scale(s.getY()) == scale(y) + 1 && s.blocks())) {
            return;
        }
        y += speed;
        if (y >= (MATRIX_HEIGHT - 1) * PRECISION) {
            y = (MATRIX_HEIGHT - 1) * PRECISION;
        }
    }

    public void moveUp(List<Spot> allSpots) {
        if (allSpots.stream().anyMatch(s -> scale(s.getX()) == scale(x) && scale(s.getY()) == scale(y) && s.blocks())) {
            return;
        }
        y -= speed;
        if (y < 0) {
            y = 0;
        }
    }

    public Spot clone(char newSymbol) {
        return new Spot(newSymbol, x / PRECISION, y / PRECISION, blocking, destructible);
    }
}
