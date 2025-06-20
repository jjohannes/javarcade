package de.javarca.base.model;

public interface InhabitantStates {

    InhabitantStates filter(char symbol);

    void print(String value);

    void print(int value);

    int setX(int x);

    int setY(int y);

    void spawn(char symbole, int x, int y);
}
