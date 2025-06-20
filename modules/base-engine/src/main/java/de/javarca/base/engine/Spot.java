package de.javarca.base.engine;

import de.javarca.base.model.InhabitantCollision;
import de.javarca.base.model.InhabitantProperty;
import de.javarca.base.model.InhabitantState;
import de.javarca.base.model.InhabitantStates;
import de.javarca.base.model.Stage;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.javarca.base.engine.GameParameters.STAGE_SIZE;
import static de.javarca.base.model.InhabitantProperty.BLOCKING;
import static de.javarca.base.model.InhabitantProperty.DESTRUCTIBLE;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Spot implements InhabitantState {
    public static final int PRECISION = 10000;

    private final Map<Character, InhabitantCollision> collisionFunctions = new LinkedHashMap<>();
    private final Map<InhabitantProperty, Integer> initialValues = new LinkedHashMap<>();
    private final Map<InhabitantProperty, Integer> values = new LinkedHashMap<>();

    private final char symbol;
    private char assetSymbol;
    private boolean alive;
    private int x;
    private int y;

    public static Stream<Spot> render(Stage stage) {
        List<Integer> symbols = Arrays.stream(stage.define().replace(" ", "").split("\n"))
                .flatMap(line -> line.chars().boxed())
                .toList();

        return IntStream.range(0, STAGE_SIZE * STAGE_SIZE).mapToObj(p ->
                new Spot((char) symbols.get(p).intValue(), p));
    }

    public Spot(char symbol, int x, int y) {
        this.symbol = symbol;
        this.assetSymbol = symbol;
        this.alive = true;
        this.x = PRECISION * x;
        this.y = PRECISION * y;
    }

    public Spot(char symbol, int posInStream) {
        this(
                symbol,
                posInStream - (posInStream / STAGE_SIZE) * STAGE_SIZE,
                posInStream / STAGE_SIZE);
    }

    public Spot clone(char newSymbol) {
        Spot clone = new Spot(newSymbol, x / PRECISION, y / PRECISION);
        clone.init(initialValues, collisionFunctions);
        return clone;
    }

    public Spot clone(char newSymbol, int x, int y) {
        Spot clone = new Spot(newSymbol, x, y);
        clone.init(initialValues, collisionFunctions);
        return clone;
    }


    @Override
    public String toString() {
        return symbol + "(" + (x * 1f) / PRECISION + "|" + (y * 1f) / PRECISION + ")";
    }

    public void init(Map<InhabitantProperty, Integer> initialValues, Map<Character, InhabitantCollision> collisionFunctions) {
        this.initialValues.putAll(initialValues);
        this.collisionFunctions.putAll(collisionFunctions);
    }

    public char getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(char assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public int getValue(InhabitantProperty p) {
        return values.getOrDefault(p, initialValues.getOrDefault(p, 0));
    }

    @Override
    public int setValue(InhabitantProperty p, int value) {
        values.put(p, value);
        return getValue(p);
    }

    @Override
    public int resetValue(InhabitantProperty p) {
        values.remove(p);
        return getValue(p);
    }

    @Override
    public int addToValue(InhabitantProperty p, int increment) {
        values.put(p, getValue(p) + increment);
        return getValue(p);
    }

    @Override
    public int multiplyValue(InhabitantProperty p, int multiplier) {
        values.put(p, getValue(p) * multiplier);
        return getValue(p);
    }


    @Override
    public int getX() {
        return x / PRECISION;
    }

    @Override
    public int setX(int x) {
        this.x = x * PRECISION;
        return x;
    }

    @Override
    public int getY() {
        return y / PRECISION;
    }

    @Override
    public int setY(int y) {
        this.y = y * PRECISION;
        return y;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void destroy() {
        alive = false;
    }

    public float getPixelPositionX(int cellWidthInPixel) {
        if (!alive) {
            return Float.MIN_VALUE;
        }
        return (x * cellWidthInPixel) * 1f / PRECISION;
    }

    public float getPixelPositionY(int cellHeightInPixel) {
        if (!alive) {
            return Float.MIN_VALUE;
        }
        return (y * cellHeightInPixel) * 1f / PRECISION;
    }

    private boolean blocks() {
        return getValue(BLOCKING) == 1 || getValue(DESTRUCTIBLE) == 1;
    }

    public void move(int deltaX, int deltaY, List<Spot> allSpots, InhabitantStates all) {
        if (!alive) {
            return;
        }
        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        int newX = min(max(0, x + deltaX), (STAGE_SIZE - 1) * PRECISION);
        int newY = min(max(0, y + deltaY), (STAGE_SIZE - 1) * PRECISION);

        List<Spot> collisions = allSpots.stream().filter(s -> s.alive && s.blocks() && s != this && doesCollide(deltaX, deltaY, s)).toList();
        if (!collisions.isEmpty()) {
            collisions.forEach(s -> collides(s, all));

            // maybe we can still move part of the way
            if (deltaX != 0) {
                x = deltaX < 0 ? snapFloor(x) : snapCeil(x);
            }
            if (deltaY != 0) {
                y = deltaX < 0 ? snapFloor(y) : snapCeil(y);
            }
            return;
        }

        x = newX;
        y = newY;
    }

    private int snapFloor(int value) {
        return (value / PRECISION) * PRECISION;
    }

    private int snapCeil(int value) {
        int ceil = ((value + PRECISION) / PRECISION) * PRECISION;
        if (ceil == value + PRECISION) {
            return value;
        }
        return ceil;
    }

    private boolean doesCollide(int deltaX, int deltaY, Spot other) {
        return x + deltaX < other.x + PRECISION &&
                x + deltaX + PRECISION > other.x &&
                y + deltaY < other.y + PRECISION &&
                y + deltaY + PRECISION > other.y;
    }

    private void collides(Spot other, InhabitantStates all) {
        if (collisionFunctions.containsKey(other.symbol)) {
            collisionFunctions.get(other.symbol).collide(this, other, all);
            if (symbol == other.symbol) {
                return; // do not compute same collision logic twice
            }
        }
        if (other.collisionFunctions.containsKey(symbol)) {
            other.collisionFunctions.get(symbol).collide(other, this, all);
        }
    }
}
