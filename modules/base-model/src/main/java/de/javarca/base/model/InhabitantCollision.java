package de.javarca.base.model;

public interface InhabitantCollision {

    void collide(InhabitantState myState, InhabitantState otherState, InhabitantStates allStates);
}
