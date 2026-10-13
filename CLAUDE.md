# CLAUDE.md

Guidance for working in this repository.

## Project

**Multi World Borders** (`multiworldborders`) — a Fabric mod by Potatoboy that makes
Minecraft world borders work per-dimension instead of being shared across all worlds.

- Language: Java 21
- Loader: Fabric (fabric-loom)
- Main entrypoint: `us.potatoboy.worldborderfix.MultiWorldBorder`
- Most logic lives in mixins under `src/main/java/us/potatoboy/worldborderfix/mixin/`.

## Scope — IMPORTANT

**We work ONLY on the Minecraft 1.21.1 Fabric version.** Do not target or modify
support for other Minecraft versions or other mod loaders (Forge/NeoForge).

Version pins (`gradle.properties` / `fabric.mod.json`):

- `minecraft_version=1.21.1`
- `yarn_mappings=1.21.1+build.3`
- `loader_version=0.18.3`
- `fabric_version=0.103.0+1.21.1`
- `fabric.mod.json` depends on `minecraft: "~1.21.1"`

## Related project — Multiworld

A separate, related mod **Multiworld** is available locally at:

```
D:\Dev\Minecraft\multiworld
```

It is a multi-platform (Common / Fabric / Forge / NeoForge), multi-version project.
The relevant Fabric module for our target is:

```
D:\Dev\Minecraft\multiworld\fabric\Multiworld-Fabric-1.21.1
```

Use it as a reference when something needs to stay consistent between the two mods.
That repo has its own `CLAUDE.md`.

## Build & run

```bash
./gradlew build        # build the mod jar
./gradlew runClient    # launch a dev client
./gradlew runServer    # launch a dev server
```

Build output is remapped by Loom; the resulting jar lands in `build/libs/`.

## Layout

- `src/main/java/us/potatoboy/worldborderfix/` — mod sources
  - `MultiWorldBorder.java` — `ModInitializer` entrypoint
  - `BorderWithWorld`, `MultiWorldBorder`, `PerWorldBorderListener`, `WorldBorderState` — core logic
  - `mixin/` — all mixins (registered in `multiworldborders.mixins.json`)
- `src/main/resources/`
  - `fabric.mod.json` — mod metadata & dependencies
  - `multiworldborders.mixins.json` — mixin registration (compatibilityLevel JAVA_21)