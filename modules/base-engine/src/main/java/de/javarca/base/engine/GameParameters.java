package de.javarca.base.engine;

import de.javarca.base.model.Stage;

public interface GameParameters {

    char SYMBOL_EMPTY_SPOT = '.';
    int TRUE = 1;

    int CELL_SIZE = 16;
    int STAGE_SIZE = 16;
    int GAME_WIDTH = STAGE_SIZE * CELL_SIZE;
    int GAME_HEIGHT = STAGE_SIZE * CELL_SIZE;

    Stage EMPTY_DEFAULT = () -> ("1" + ".".repeat(STAGE_SIZE) + "3")
            + (".".repeat(STAGE_SIZE) + "\n").repeat(STAGE_SIZE)
            + ("4" + ".".repeat(STAGE_SIZE) + "2");
}
