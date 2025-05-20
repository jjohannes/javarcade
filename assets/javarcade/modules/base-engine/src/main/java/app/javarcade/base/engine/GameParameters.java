package app.javarcade.base.engine;

import app.javarcade.base.model.Level;

public interface GameParameters {

    int CELL_WIDTH = 16;
    int CELL_HEIGHT = 16;
    int MATRIX_WIDTH = 14;
    int MATRIX_HEIGHT = 14;
    int GAME_WIDTH = MATRIX_WIDTH * CELL_WIDTH;
    int GAME_HEIGHT = MATRIX_HEIGHT * CELL_HEIGHT;

    int SCALE = 2;

    int CELL_WIDTH_IN_PIXEL = SCALE * CELL_WIDTH;
    int CELL_HEIGHT_IN_PIXEL = SCALE * CELL_HEIGHT;
    int WIDTH_IN_PIXEL = SCALE * GAME_WIDTH;
    int HEIGHT_IN_PIXEL = SCALE * GAME_HEIGHT;

    Level EMPTY_DEFAULT = () -> ("1" + ".".repeat(MATRIX_WIDTH - 2) + "3")
            + (".".repeat(MATRIX_WIDTH) + "\n").repeat(MATRIX_HEIGHT - 2)
            + ("4" + ".".repeat(MATRIX_WIDTH - 2) + "2");
}
