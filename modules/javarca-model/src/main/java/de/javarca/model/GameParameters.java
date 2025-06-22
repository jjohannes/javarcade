package de.javarca.model;

public interface GameParameters {

    char SYMBOL_EMPTY_SPOT = '.';
    int TRUE = 1;
    int PRECISION = 10000;

    int CELL_SIZE = 16;
    int STAGE_SIZE = 16;
    int GAME_WIDTH = STAGE_SIZE * CELL_SIZE;
    int GAME_HEIGHT = STAGE_SIZE * CELL_SIZE;
}
