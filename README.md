# Welcome to the JavaRCAde!

- 🤔 What is this? ➡ [javarca.de](https://javarca.de)

## Compose modules to run

### Module Path

```shell
cd apps/app-retro/build/install/app-retro
java --module-path 'lib' --module app.javarcade.base.engine
```

### Class Path

```shell
cd apps/app-retro/build/install/app-retro
java --class-path 'lib/*' app.javarcade.base.engine.Engine
```

## Compile a Module

### Module Path

```shell
cd classic/classic-levels
cp ../../apps/app-retro/build/install/app-retro/lib/slf4j-api-2.0.13.jar .
cp ../../apps/app-retro/build/install/app-retro/lib/base-model-1.2.jar .
javac --module-path base-model-1.2.jar:slf4j-api-2.0.13.jar -d out \
  src/main/java/module-info.java src/main/java/app/javarcade/classic/levels/ClassicLevel.java
jar --create --file classic-levels-1.2.jar -C out . -C src/main/resources .
```

### Class Path

```shell
cd classic/classic-levels
cp ../../apps/app-retro/build/install/app-retro/lib/slf4j-api-2.0.13.jar .
cp ../../apps/app-retro/build/install/app-retro/lib/base-model-1.2.jar .
javac --class-path base-model-1.2.jar:slf4j-api-2.0.13.jar -d out \
  src/main/java/app/javarcade/classic/levels/ClassicLevel.java
jar --create --file classic-levels-1.2.jar -C out . -C src/main/resources .
```

## Using Tools

### Build with Gradle

```shell
./gradlew assemble check
```

### Run with Gradle

```shell
./gradlew :app-retro:run
```


### Build with Maven

```shell
./mvnw clean verify
```

### Run with Maven

> [!NOTE] With Maven, make sure you build 👆 first.

```shell
./mvnw -pl apps/app-retro exec:exec
```
