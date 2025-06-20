package de.javarca.base.model;

public interface InhabitantStates {

    InhabitantStates filter(char symbol);

    InhabitantStates filter(InhabitantProperty p, int value);

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
