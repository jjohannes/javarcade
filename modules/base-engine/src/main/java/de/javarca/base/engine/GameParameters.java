package de.javarca.base.engine;

import de.javarca.base.model.Stage;

public interface GameParameters {

    char SYMBOL_EMPTY_SPOT = '.';
    int TRUE = 1;

    int CELL_SIZE = 16;
    int MATRIX_SIZE = 16;
    int GAME_WIDTH = MATRIX_SIZE * CELL_SIZE;
    int GAME_HEIGHT = MATRIX_SIZE * CELL_SIZE;

    Stage EMPTY_DEFAULT = () -> ("1" + ".".repeat(MATRIX_SIZE) + "3")
            + (".".repeat(MATRIX_SIZE) + "\n").repeat(MATRIX_SIZE)
            + ("4" + ".".repeat(MATRIX_SIZE) + "2");
}
