package app.javarcade.base.engine;

import static app.javarcade.base.engine.GameParameters.EMPTY_DEFAULT;
import static app.javarcade.base.engine.GameParameters.SYMBOL_EMPTY_SPOT;
import static app.javarcade.base.engine.GameParameters.SYMBOL_PLAYER;
import static java.util.function.Function.identity;

import app.javarcade.base.model.Asset;
import app.javarcade.base.model.AssetSet;
import app.javarcade.base.model.Item;
import app.javarcade.base.model.ItemSet;
import app.javarcade.base.model.Level;
import app.javarcade.base.model.Player;
import app.javarcade.base.model.PlayerPropertyModifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class GameState {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private final Map<Character, Item> itemInfo = new LinkedHashMap<>();

    private final List<Spot> items = new LinkedList<>();
    private final List<Spot> spots = new LinkedList<>();
    private final Map<Character, byte[]> images = new HashMap<>();

    public GameState() {
        init();
    }

    private void init() {
        ServiceLoader.load(ItemSet.class).forEach(set -> {
            itemInfo.putAll(set.items().stream().collect(Collectors.toMap(Item::symbol, identity())));
        });
        ServiceLoader.load(AssetSet.class)
                .forEach(set ->
                        images.putAll(set.assets().stream().collect(Collectors.toMap(Asset::symbol, Asset::image))));
        var level = ServiceLoader.load(Level.class).findFirst().orElse(EMPTY_DEFAULT);
        Spot.render(level).forEach(spot -> {
            if (itemInfo.containsKey(spot.getSymbol())) {
                spots.addFirst(spot.clone(SYMBOL_EMPTY_SPOT));
                var item = itemInfo.get(spot.getSymbol());
                spot.getModifiers().putAll(item.modifiers().stream().collect(Collectors.toMap(PlayerPropertyModifier::p, PlayerPropertyModifier::value)));
                spots.add(spot);
                items.add(spot);
            } else {
                spots.addFirst(spot);
            }
        });
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public List<Spot> getItems() {
        return items;
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
