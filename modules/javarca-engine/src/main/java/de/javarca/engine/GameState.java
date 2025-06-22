package de.javarca.engine;

import de.javarca.engine.impl.InhabitantStatesImpl;
import de.javarca.model.Asset;
import de.javarca.model.AssetSet;
import de.javarca.model.Inhabitant;
import de.javarca.model.InhabitantSet;
import de.javarca.model.InhabitantStates;
import de.javarca.model.Stage;
import de.javarca.model.InhabitantPropertyModifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static de.javarca.model.GameParameters.STAGE_SIZE;
import static de.javarca.model.GameParameters.SYMBOL_EMPTY_SPOT;
import static java.util.function.Function.identity;

public class GameState {
    Stage EMPTY_DEFAULT = () -> ("?".repeat(STAGE_SIZE))
            + ("?" + ".".repeat(STAGE_SIZE-2) + "?\n").repeat(STAGE_SIZE-2)
            + ("?".repeat(STAGE_SIZE));

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
        all = new InhabitantStatesImpl(spots, spots, prototypes);
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
