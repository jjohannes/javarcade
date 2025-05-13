export INSTALL_DIR=/Users/jendrik/projects/gradle/howto/javarcade/apps/app-retro/build/install/app-retro/lib
export PRESENTATION_FOLDER=out

rm -rf lib
mkdir lib

cp ${INSTALL_DIR}/base-* lib
cp ${INSTALL_DIR}/engine-lwjgl* lib

# cp ${INSTALL_DIR}/classic-assets* lib
# cp ${INSTALL_DIR}/commons-io* lib

cp ${INSTALL_DIR}/lwjgl* lib
cp ${INSTALL_DIR}/slf4j* lib

# java --class-path  'lib/*'        app.javarcade.base.engine.Main

java --module-path 'lib' --module app.javarcade.base.engine