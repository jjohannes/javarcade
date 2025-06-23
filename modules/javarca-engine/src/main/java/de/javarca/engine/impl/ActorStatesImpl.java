package de.javarca.engine.impl;

import de.javarca.engine.Spot;
import de.javarca.model.ActorProperty;
import de.javarca.model.ActorStates;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.javarca.model.GameParameters.STAGE_SIZE;

public class ActorStatesImpl implements ActorStates {

    private final List<Spot> root;
    private final List<Spot> spots;
    private final Map<Character, Spot> prototypes;

    public ActorStatesImpl(List<Spot> spots, List<Spot> root, Map<Character, Spot> prototypes) {
        this.spots = spots;
        this.root = root;
        this.prototypes = prototypes;
    }

    @Override
    public ActorStates filter(char symbol) {
        return new ActorStatesImpl(spots.stream().filter(s -> s.isAlive() && s.getSymbol() == symbol).collect(Collectors.toList()), root, prototypes);
    }

    @Override
    public ActorStates filter(ActorProperty p, int value) {
        return new ActorStatesImpl(spots.stream().filter(s -> s.isAlive() && s.getValue(p) == value).collect(Collectors.toList()), root, prototypes);
    }

    @Override
    public void print(String value) {
        for (int i = 0; i < spots.size() && i < value.length(); i++) {
            spots.get(i).setSkin(value.charAt(i));
        }
    }

    @Override
    public void print(int value) {
        String s = String.format("%1$" + spots.size() + "s", value).replace(' ', '0');
        for (int i = 0; i < spots.size() && i < s.length(); i++) {
            spots.get(i).setSkin(s.charAt(i));
        }
    }

    @Override
    public int setX(int x) {
        spots.forEach(s -> s.setX(x));
        return x;
    }

    @Override
    public int setY(int y) {
        spots.forEach(s -> s.setY(y));
        return y;
    }

    @Override
    public int getMinY() {
        return spots.stream().map(Spot::getY).min(Integer::compareTo).orElse(0);
    }

    @Override
    public int getMaxY() {
        return spots.stream().map(Spot::getY).max(Integer::compareTo).orElse(STAGE_SIZE);
    }

    @Override
    public int getMinX() {
        return spots.stream().map(Spot::getX).min(Integer::compareTo).orElse(0);
    }

    @Override
    public int getMaxX() {
        return spots.stream().map(Spot::getX).max(Integer::compareTo).orElse(STAGE_SIZE);
    }


    @Override
    public void spawn(char symbol, int x, int y) {
        spawn(symbol, x, y, symbol);
    }

    @Override
    public void spawn(char symbol, int x, int y, char skin) {
        var prototype = prototypes.get(symbol);
        Spot newSpot;
        if (prototype == null) {
            newSpot = new Spot(symbol, x, y);
        } else {
            newSpot = prototype.clone(symbol, x, y);
        };
        newSpot.setSkin(skin);
        root.add(newSpot);
    }
}
