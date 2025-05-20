package app.javarcade.base.engine;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Renderer {
    static void launch() {
        var impl = ServiceLoader.load(Renderer.class).findFirst();
        impl.ifPresentOrElse(Renderer::run, () -> {
            LoggerFactory.getLogger(Renderer.class).warn("No renderer implementation available");
        });
    }

    void run();
}
