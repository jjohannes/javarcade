package app.javarcade.presentation;

import java.io.File;
import java.util.List;

public class SlideShow {

    private int current = 0;

    private List<SlideShowStep> steps;

    void prev() {
        if (current == 1) {
            return;
        }
        current--;
        reload();
    }

    void next() {
        if (steps.size() - 1 == current) {
            return;
        }
        current++;
        reload();
    }

    private void reload() {

    }
}
