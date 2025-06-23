package de.javarca.model;

public interface ActorStates {

    ActorStates filter(char symbol);

    ActorStates filter(ActorProperty p, int value);

    void print(String value);

    void print(int value);

    int setX(int x);

    int setY(int y);

    int getMinY();

    int getMaxY();

    int getMinX();

    int getMaxX();

    void spawn(char symbole, int x, int y);

}
