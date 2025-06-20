package de.javarca.base.engine;

import de.javarca.base.engine.impl.InhabitantStatesImpl;
import de.javarca.base.model.Asset;
import de.javarca.base.model.AssetSet;
import de.javarca.base.model.Inhabitant;
import de.javarca.base.model.InhabitantSet;
import de.javarca.base.model.InhabitantStates;
import de.javarca.base.model.Stage;
import de.javarca.base.model.InhabitantPropertyModifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static de.javarca.base.engine.GameParameters.EMPTY_DEFAULT;
import static de.javarca.base.engine.GameParameters.SYMBOL_EMPTY_SPOT;
import static java.util.function.Function.identity;

public class GameState {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private final Map<Character, Inhabitant> itemInfo = new LinkedHashMap<>();

    private final List<Spot> spots = new CopyOnWriteArrayList<>();
    private final Map<Character, byte[]> images = new HashMap<>();
    private InhabitantStates all;

    public GameState() {
        init();
    }

    private void init() {
        ServiceLoader.load(InhabitantSet.class).forEach(set -> {
            itemInfo.putAll(set.items().stream().collect(Collectors.toMap(Inhabitant::symbol, identity())));
        });
        ServiceLoader.load(AssetSet.class)
                .forEach(set ->
                        images.putAll(set.assets().stream().collect(Collectors.toMap(Asset::symbol, Asset::image))));
        var stage = ServiceLoader.load(Stage.class).findFirst().orElse(EMPTY_DEFAULT);
        List<Spot> renderedStage = Spot.render(stage).toList();
        Map<Character, Spot> prototypes = new LinkedHashMap<>();
        renderedStage.forEach(spot -> {
            if (itemInfo.containsKey(spot.getSymbol())) {
                spots.addFirst(spot.clone(SYMBOL_EMPTY_SPOT));
                var item = itemInfo.get(spot.getSymbol());
                spot.init(
                        item.modifiers().stream().collect(Collectors.toMap(InhabitantPropertyModifier::p, InhabitantPropertyModifier::value)),
                        item.collisionFunctions()
                );
                spots.add(spot);
                prototypes.put(spot.getSymbol(), spot);
            } else {
                spots.addFirst(spot);
            }
        });
        all = new InhabitantStatesImpl(renderedStage, spots, prototypes);
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public InhabitantStates getAll() {
        return all;
    }

    public Map<Character, byte[]> getImages() {
        return images;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void action() {
        System.out.println("Boom!");
    }
}
