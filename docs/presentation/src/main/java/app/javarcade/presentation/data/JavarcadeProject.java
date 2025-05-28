package app.javarcade.presentation.data;

import app.javarcade.presentation.components.ModuleGraph;
import app.javarcade.presentation.components.model.Module;
import app.javarcade.presentation.components.model.Topic;

import java.nio.file.Path;
import java.util.Set;

public interface JavarcadeProject {
    Path APP_ROOT_FOLDER = Path.of("/Users/jendrik/projects/gradle/howto/javarcade");
    Path APP_MODULES_FOLDER = APP_ROOT_FOLDER.resolve("modules");
    Path ASSET_LOCATION = APP_ROOT_FOLDER.resolve("docs/presentation/src/main/assets");
    Path EXTRA_INSTALL_FOLDER = ASSET_LOCATION.resolve("jars");
    Path WORK_FOLDER = ASSET_LOCATION.resolve("work");

    String NO_MODULE_PROJECT_SUFFIX = "-no-modules";

    String RUN_MODULE_PATH_CMD = "java --module-path lib --module app.javarcade.base.engine";
    String RUN_CLASS_PATH_CMD  = "java --class-path  lib/* app.javarcade.base.engine.Engine";

    static Set<Module> modules() {
        return Set.of(
                new Module("base-model.jar", 1, 0),
                new Module("base-engine.jar", 3, 0, Set.of("base-model.jar", "slf4j-api-2.0.17.jar", "slf4j-simple-2.0.17.jar")),
                new Module("classic-assets.jar", 0, 1, Set.of("base-model.jar", "commons-io-2.16.1.jar")),
                new Module("classic-levels.jar", 1, 1, Set.of("base-model.jar")),
                new Module("classic-items.jar", 2, 1, Set.of("base-model.jar", "commons-csv-1.14.0.jar")),
                new Module("renderer-lwjgl.jar", 3, 1, Set.of("base-engine.jar", "slf4j-api-2.0.17.jar", "slf4j-jdk14-2.0.17.jar", "lwjgl-3.3.6.jar")),
                new Module("slf4j-api-2.0.17.jar", 1, 2),
                new Module("slf4j-simple-2.0.17.jar", 0, 2, Set.of("slf4j-api-2.0.17.jar")),
                new Module("slf4j-jdk14-2.0.17.jar", 2, 2, Set.of("slf4j-api-2.0.17.jar")),
                new Module("commons-io-2.16.1.jar", 0, 3),
                new Module("commons-codec-1.18.0.jar", 1, 3),
                new Module("commons-csv-1.14.0.jar", 2, 3, Set.of("commons-io-2.18.0.jar", "commons-codec-1.18.0.jar")),
                new Module("commons-io-2.18.0.jar", 3, 3),
                new Module("lwjgl-3.3.6.jar", 3, 4, Set.of("lwjgl-3.3.6-natives-macos-arm64.jar", "lwjgl-3.3.6-natives-windows-x86.jar")),
                new Module("lwjgl-3.3.6-natives-macos-arm64.jar", 2, 4),
                new Module("lwjgl-3.3.6-natives-windows-x86.jar", 1, 4)
        );
    }

    static Set<Module> initialState(ModuleGraph moduleGraph) {
        return Set.of(
                new Module("lwjgl-glfw-3.3.6.jar"),
                new Module("lwjgl-glfw-3.3.6-natives-macos-arm64.jar"),
                new Module("lwjgl-opengl-3.3.6.jar"),
                new Module("lwjgl-opengl-3.3.6-natives-macos-arm64.jar"),
                new Module("lwjgl-stb-3.3.6.jar"),
                new Module("lwjgl-stb-3.3.6-natives-macos-arm64.jar"),
                moduleGraph.get("base-model.jar"),
                moduleGraph.get("base-engine.jar"),
                moduleGraph.get("renderer-lwjgl.jar"),
                moduleGraph.get("slf4j-api-2.0.17.jar"),
                moduleGraph.get("slf4j-simple-2.0.17.jar"),
                moduleGraph.get("lwjgl-3.3.6.jar"),
                moduleGraph.get("lwjgl-3.3.6-natives-macos-arm64.jar")
        );
    }

    static Set<Topic> topics() {
        return Set.of(
                new Topic("Dependency Definition", 0, 0),
                new Topic("Module Version Management", 1, 0),
                new Topic("Retrieving and Building JARs", 2, 0),
                new Topic("Version Conflict Management", 0, 1),
                new Topic("Variant Conflict Management", 1, 1),
                new Topic("Version Update Management", 2, 1)
        );
    }
}
