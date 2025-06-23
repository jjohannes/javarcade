package de.javarca.model;

public interface ActorState {

    boolean isAlive();

    void destroy();

    int getX();

    int getY();

    int getValue(ActorProperty p);

    int setX(int x);

    int setY(int y);

    int setValue(ActorProperty p, int value);

    int resetValue(ActorProperty p);

    int addToValue(ActorProperty p, int increment);

    int multiplyValue(ActorProperty p, int multiplier);

    char setSkin(char skin);
}
