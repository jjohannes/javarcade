package de.javarca.base.engine.impl;

import de.javarca.base.engine.Spot;
import de.javarca.base.model.InhabitantProperty;
import de.javarca.base.model.InhabitantStates;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.javarca.base.engine.GameParameters.STAGE_SIZE;

public class InhabitantStatesImpl implements InhabitantStates {

    private final List<Spot> root;
    private final List<Spot> spots;
    private final Map<Character, Spot> prototypes;

    public InhabitantStatesImpl(List<Spot> spots, List<Spot> root, Map<Character, Spot> prototypes) {
        this.spots = spots;
        this.root = root;
        this.prototypes = prototypes;
    }

    @Override
    public InhabitantStates filter(char symbol) {
        return new InhabitantStatesImpl(spots.stream().filter(s -> s.isAlive() && s.getSymbol() == symbol).collect(Collectors.toList()), root, prototypes);
    }

    @Override
    public InhabitantStates filter(InhabitantProperty p, int value) {
        return new InhabitantStatesImpl(spots.stream().filter(s -> s.isAlive() && s.getValue(p) == value).collect(Collectors.toList()), root, prototypes);
    }

    @Override
    public void print(String value) {
        for (int i = 0; i < spots.size() && i < value.length(); i++) {
            spots.get(i).setAssetSymbol(value.charAt(i));
        }
    }

    @Override
    public void print(int value) {
        String s = String.format("%1$" + spots.size() + "s", value).replace(' ', '0');
        for (int i = 0; i < spots.size() && i < s.length(); i++) {
            spots.get(i).setAssetSymbol(s.charAt(i));
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
    public void spawn(char symbol, int x, int y) {
        var prototype = prototypes.get(symbol);
        if (prototype == null) {
            root.add(new Spot(symbol, x, y));
        } else {
            root.add(prototype.clone(symbol, x, y));
        }
    }
}
