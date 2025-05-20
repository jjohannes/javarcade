module app.javarcade.renderer.lwjgl {
  requires transitive app.javarcade.base.engine;
  requires org.lwjgl.glfw;
  requires org.lwjgl.opengl;
  requires org.lwjgl.stb;
  requires org.lwjgl;
  requires org.slf4j;

  exports app.javarcade.renderer.lwjgl;

  provides app.javarcade.base.engine.Renderer with
      app.javarcade.renderer.lwjgl.LWJGLRenderer;
}
