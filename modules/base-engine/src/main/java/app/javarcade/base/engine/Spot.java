package app.javarcade.base.engine;

import app.javarcade.base.model.Level;
import app.javarcade.base.model.PlayerProperty;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static app.javarcade.base.engine.GameParameters.MATRIX_HEIGHT;
import static app.javarcade.base.engine.GameParameters.MATRIX_WIDTH;
import static app.javarcade.base.engine.GameParameters.SYMBOL_PLAYER;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Spot {
    public static final int PRECISION = 10000;

    private final char symbol;
    private final Map<PlayerProperty, Integer> modifiers = new LinkedHashMap<>();

    private int x;
    private int y;

    public static Stream<Spot> render(Level level) {
        List<Integer> symbols = Arrays.stream(level.define().replace(" ", "").split("\n"))
                .flatMap(line -> line.chars().boxed())
                .toList();

        return IntStream.range(0, MATRIX_WIDTH * MATRIX_HEIGHT).mapToObj(p -> {
            char symbol = p < symbols.size() ? (char) symbols.get(p).intValue() : ']';
            return new Spot(symbol, p);
        });
    }

    public Spot(char symbol, int x, int y) {
        this.symbol = symbol;
        this.x = PRECISION * x;
        this.y = PRECISION * y;
    }

    public Spot(char symbol, int posInStream) {
        this(
                symbol,
                posInStream - (posInStream / MATRIX_WIDTH) * MATRIX_WIDTH,
                posInStream / MATRIX_WIDTH);
    }

    public Spot clone(char newSymbol) {
        return new Spot(newSymbol, x / PRECISION, y / PRECISION);
    }

    public Map<PlayerProperty, Integer> getModifiers() {
        return modifiers;
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

    private int scaleFloor(int s) {
        return s / PRECISION;
    }

    private int scaleCeil(int s) {
        return (s + PRECISION - 1) / PRECISION;
    }

    private boolean blocks() {
        return modifiers.getOrDefault(PlayerProperty.BLOCKING, 0) == 1 || modifiers.getOrDefault(PlayerProperty.DESTRUCTIBLE, 0) == 1;
    }

    public void moveX(List<Spot> allSpots) {
        int speed = modifiers.getOrDefault(PlayerProperty.XSPEED, 0);
        if (speed == 0) {
            return;
        }
        move(speed, 0, allSpots);
    }

    public void moveY(List<Spot> allSpots) {
        int speed = modifiers.getOrDefault(PlayerProperty.YSPEED, 0);
        if (speed == 0) {
            return;
        }
        move(0, speed, allSpots);
    }

    public void moveLeft(List<Spot> allSpots) {
        int speed = modifiers.getOrDefault(PlayerProperty.XSPEED, 0);
        if (speed == 0) {
            return;
        }
        move(-speed, 0, allSpots);
    }

    public void moveUp(List<Spot> allSpots) {
        int speed = modifiers.getOrDefault(PlayerProperty.YSPEED, 0);
        if (speed == 0) {
            return;
        }
        move(0, -speed, allSpots);
    }

    private void move(int deltaX, int deltaY, List<Spot> allSpots) {
        int newX = min(max(0, x + deltaX), (MATRIX_WIDTH - 1) * PRECISION);
        int newY = min(max(0, y + deltaY), (MATRIX_HEIGHT - 1) * PRECISION);

        int offsetX = deltaX > 0 ? -1 : 0;
        int offsetY = deltaY > 0 ? -1 : 0;

        if (deltaY == 0) { // x-direction check
            if (allSpots.stream().anyMatch(s -> s.blocks() && (
                            scaleFloor(s.getX()) + offsetX == scaleFloor(newX) && scaleFloor(s.getY()) + offsetY == scaleFloor(newY) ||
                                    scaleFloor(s.getX()) + offsetX == scaleFloor(newX) && scaleFloor(s.getY()) + offsetY == scaleCeil(newY)))) {
                return;
            }
        }
        if (deltaX == 0) { // y-direction check
            if (allSpots.stream().anyMatch(s -> s.blocks() && (
                    scaleFloor(s.getX()) + offsetX == scaleFloor(newX) && scaleFloor(s.getY()) + offsetY == scaleFloor(newY) ||
                            scaleFloor(s.getX()) + offsetX == scaleCeil(newX) && scaleFloor(s.getY()) + offsetY == scaleFloor(newY)))) {
                return;
            }
        }

        x = newX;
        y = newY;
    }

    public boolean playerControlled() {
        return symbol == SYMBOL_PLAYER; // TODO move to table
    }
}
