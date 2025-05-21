module app.javarcade.renderer.lwjgl {
  requires transitive app.javarcade.base.engine;
  requires org.lwjgl.glfw;
  requires org.lwjgl.opengl;
  requires org.lwjgl.stb;
  requires org.lwjgl;
  requires org.slf4j;
  requires /*runtime*/ org.lwjgl.natives;
  requires /*runtime*/ org.lwjgl.glfw.natives;
  requires /*runtime*/ org.lwjgl.opengl.natives;
  requires /*runtime*/ org.lwjgl.stb.natives;

  exports app.javarcade.renderer.lwjgl;

  provides app.javarcade.base.engine.Renderer with
      app.javarcade.renderer.lwjgl.LWJGLRenderer;
}
