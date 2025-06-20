import de.javarca.base.engine.Renderer;
import de.javarca.renderer.lwjgl.LWJGLRenderer;

module de.javarca.renderer.lwjgl {
  requires transitive de.javarca.base.engine;
  requires de.javarca.base.model;
  requires org.lwjgl.glfw;
  requires org.lwjgl.opengl;
  requires org.lwjgl.stb;
  requires org.lwjgl;
  requires org.slf4j;
  requires /*runtime*/ org.lwjgl.glfw.natives;
  requires /*runtime*/ org.lwjgl.natives;
  requires /*runtime*/ org.lwjgl.opengl.natives;
  requires /*runtime*/ org.lwjgl.stb.natives;

  exports de.javarca.renderer.lwjgl;

  provides Renderer with
          LWJGLRenderer;
}
