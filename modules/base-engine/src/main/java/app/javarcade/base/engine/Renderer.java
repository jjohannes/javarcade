package app.javarcade.base.engine;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Renderer {
    static void launch() {
        Logger logger = LoggerFactory.getLogger(Renderer.class);
        logger.info("Welcome to Javarcade!");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            System.exit(1);
        });

        var impl = ServiceLoader.load(Renderer.class).findFirst();
        impl.ifPresentOrElse(Renderer::run, () -> {
            logger.warn("No renderer implementation available");
        });
    }

    void run();
}
