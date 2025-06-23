package de.javarca.jamcatch.actors.collisions;

import de.javarca.model.ActorCollision;

import java.util.Map;
import java.util.Random;

import static de.javarca.model.GameParameters.PRECISION;
import static de.javarca.model.ActorProperty.POINTS;
import static de.javarca.model.ActorProperty.SPEEDY;

public interface Collisions {

    Map<Character, ActorCollision> p = Map.of(
            'J', (myState, otherState, allStates) -> {
                otherState.destroy();
                myState.addToValue(POINTS, otherState.getValue(POINTS));
                allStates.filter('0').print(myState.getValue(POINTS));
                char skin = 'J';
                skin += (char) new Random().nextInt(6);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0, skin);
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
    Map<Character, ActorCollision> j = Map.of(
            'J', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - PRECISION);
            },
            'X', (myState, otherState, allStates) -> {
                myState.setValue(SPEEDY, 0);
                allStates.spawn('J', new Random().nextInt(14) + 1, 0);
                allStates.filter('p').setY(allStates.filter('J').filter(SPEEDY, 0).getMinY() - PRECISION);
            }
    );

    Map<Character, Map<Character, ActorCollision>> ALL = Map.of('p', p, 'J', j);
}
