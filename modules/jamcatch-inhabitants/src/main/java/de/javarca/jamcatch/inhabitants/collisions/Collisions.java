package de.javarca.jamcatch.inhabitants.collisions;

import de.javarca.base.model.InhabitantCollision;

import java.util.Map;
import java.util.Random;

import static de.javarca.base.model.InhabitantProperty.POINTS;
import static de.javarca.base.model.InhabitantProperty.SPEEDY;

public interface Collisions {

    Map<Character, InhabitantCollision> p = Map.of(
            'J', (myState, otherState, allStates) -> {
                otherState.destroy();
                myState.addToValue(POINTS, otherState.getValue(POINTS));
                allStates.filter('0').print(myState.getValue(POINTS));
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
            }
    );
    Map<Character, InhabitantCollision> j = Map.of(
            'J', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - 1);
            },
            '~', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - 1);
            }

    );

    Map<Character, Map<Character, InhabitantCollision>> ALL = Map.of('p', p, 'J', j);
}
