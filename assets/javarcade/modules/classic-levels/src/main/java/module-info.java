module app.javarcade.classic.levels {
  requires transitive app.javarcade.base.model;
  requires org.slf4j;

  exports app.javarcade.classic.levels;

  provides app.javarcade.base.model.Level with
      app.javarcade.classic.levels.ClassicLevel;
}
