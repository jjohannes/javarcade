package de.javarca.model;

public interface ActorCollision {

    void collide(ActorState myState, ActorState otherState, ActorStates allStates);
}
