package app.javarcade.base.engine;

import static app.javarcade.base.engine.GameParameters.EMPTY_DEFAULT;
import static java.util.function.Function.identity;

import app.javarcade.base.model.Asset;
import app.javarcade.base.model.AssetSet;
import app.javarcade.base.model.Item;
import app.javarcade.base.model.ItemSet;
import app.javarcade.base.model.Level;
import app.javarcade.base.model.Player;
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

    private Spot player;
    private final Map<Character, Player> players = Map.of(
            '1', new Player('1'),
            '2', new Player('2'),
            '3', new Player('3'),
            '4', new Player('4'));
    private final Map<Character, Item> items = new LinkedHashMap<>();
    private final List<Spot> spots = new LinkedList<>();
    private final Map<Character, byte[]> images = new HashMap<>();

    public GameState() {
        init();
    }

    private void init() {
        ServiceLoader.load(AssetSet.class)
                .forEach(set ->
                        images.putAll(set.assets().stream().collect(Collectors.toMap(Asset::symbol, Asset::image))));
        var level = ServiceLoader.load(Level.class).findFirst().orElse(EMPTY_DEFAULT);
        Spot.render(level).forEach(spot -> {
            if (players.containsKey(spot.getSymbol())) {
                spots.addFirst(spot.clone('.'));
                spots.add(spot);
                player = spot; // TODO multi-player
            } else {
                spots.addFirst(spot);
            }
        });
        ServiceLoader.load(ItemSet.class).forEach(set -> {
            items.putAll(set.items().stream().collect(Collectors.toMap(Item::symbol, identity())));
        });
    }

    public List<Spot> getSpots() {
        return spots;
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

    public Spot getPlayer() {
        return player;
    }

    public void action() {
        System.out.println("Boom!");
    }
}
