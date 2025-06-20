package de.javarca.base.model;

public interface InhabitantState {

    boolean isAlive();

    void destroy();

    int getX();

    int getY();

    int getValue(InhabitantProperty p);

    int setX(int x);

    int setY(int y);

    int setValue(InhabitantProperty p, int value);

    int resetValue(InhabitantProperty p);

    int addToValue(InhabitantProperty p, int increment);

    int multiplyValue(InhabitantProperty p, int multiplier);

}
