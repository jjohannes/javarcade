package app.javarcade.base.engine;

import app.javarcade.base.model.Level;

public interface GameParameters {

    char SYMBOL_EMPTY_SPOT = '.';
    char SYMBOL_PLAYER = 'p';

    int CELL_WIDTH = 16;
    int CELL_HEIGHT = 16;
    int MATRIX_WIDTH = 16;
    int MATRIX_HEIGHT = 16;
    int GAME_WIDTH = MATRIX_WIDTH * CELL_WIDTH;
    int GAME_HEIGHT = MATRIX_HEIGHT * CELL_HEIGHT;

    int CELL_WIDTH_IN_PIXEL = CELL_WIDTH;
    int CELL_HEIGHT_IN_PIXEL = CELL_HEIGHT;
    int WIDTH_IN_PIXEL = GAME_WIDTH;
    int HEIGHT_IN_PIXEL = GAME_HEIGHT;

    Level EMPTY_DEFAULT = () -> ("1" + ".".repeat(MATRIX_WIDTH) + "3")
            + (".".repeat(MATRIX_WIDTH) + "\n").repeat(MATRIX_HEIGHT)
            + ("4" + ".".repeat(MATRIX_WIDTH) + "2");
}
