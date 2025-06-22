package de.javarca.jamcatch.inhabitants.collisions;

import de.javarca.model.InhabitantCollision;

import java.util.Map;
import java.util.Random;

import static de.javarca.model.GameParameters.PRECISION;
import static de.javarca.model.InhabitantProperty.POINTS;
import static de.javarca.model.InhabitantProperty.SPEEDY;

public interface Collisions {

    Map<Character, InhabitantCollision> p = Map.of(
            'J', (myState, otherState, allStates) -> {
                otherState.destroy();
                myState.addToValue(POINTS, otherState.getValue(POINTS));
                allStates.filter('0').print(myState.getValue(POINTS));
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
            },
            '.', (myState, otherState, allStates) -> {
                var target = allStates.filter('J').filter(SPEEDY, 1000).getMaxX();
                if (myState.getX() > target) {
                    myState.setX(myState.getX() - 1500);
                } else if (myState.getX() < target) {
                    myState.setX(myState.getX() + 1500);
                }
            }
    );
    Map<Character, InhabitantCollision> j = Map.of(
            'J', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - PRECISION);
            },
            '~', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - PRECISION);
            }
    );

    Map<Character, Map<Character, InhabitantCollision>> ALL = Map.of('p', p, 'J', j);
}
