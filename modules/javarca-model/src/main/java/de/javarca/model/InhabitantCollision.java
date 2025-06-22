package de.javarca.model;

public interface InhabitantCollision {

    void collide(InhabitantState myState, InhabitantState otherState, InhabitantStates allStates);
}
