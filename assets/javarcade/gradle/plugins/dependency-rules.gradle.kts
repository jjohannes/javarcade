plugins { id("org.gradlex.jvm-dependency-conflict-resolution") }

jvmDependencyConflicts {
    logging { enforceSlf4JSimple() }

    patch {
        listOf("", "-glfw", "-opengl", "-stb").forEach { lwjglModule ->
            module("org.lwjgl:lwjgl$lwjglModule") {
                addTargetPlatformVariant("natives", "natives-linux", LINUX, X86_64)
                addTargetPlatformVariant("natives", "natives-linux-arm64", LINUX, ARM64)
                addTargetPlatformVariant("natives", "natives-macos", MACOS, X86_64)
                addTargetPlatformVariant("natives", "natives-macos-arm64", MACOS, ARM64)
                addTargetPlatformVariant("natives", "natives-windows", WINDOWS, X86_64)
                addTargetPlatformVariant("natives", "natives-windows-arm64", WINDOWS, ARM64)
            }
            module("org.lwjgl:lwjgl$lwjglModule") {
        }
    }
}