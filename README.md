# Behind The Scenery Engine: Core
A public GPLv3 licensed version of the repository of the library mod for the Behind The Scenery Engine. This repository is synced with public releases of the BTSEngine: Core project.

## What is this?
Behind The Scenery Engine: Core is a library mod for Minecraft 1.21.1 for the NeoForge ecosystem, required for the Behind The Scenery Engine components. Contains all the required libraries and our code to maintain the project.

## Features

### Profilers 
We all know that Spark is not ideal when it comes to concurrent situations and many other Minecraft modpacks. Behind The Scenery Engine implements 3 new profilers into the game.
- **Tracy**, the most useful one. If you have Tracy installed in the system, you can use `/concurrent_profiler` in-game and Tracy will be able to hook into your modpack instance. It will show everything that hangs the game or lags it. Mostly useful for servers or singleplayer worlds. (TPS)
- **Remotery**, useful when comes to game frames. A bit complicated, requires LWJGL 3.3.3 to be installed. Can show useful data when it comes to client frames. (FPS/TPS)
- **Simple Console Profiler**, simple-yet-useful profiler that comes from the Behind The Scenery Engine out of the box. WIP though, but a small tutorial will appear, when it releases.

Used libraries or bindings:
- [Tracy](https://github.com/wolfpld/tracy) (1.11.0, Licensed and used under 3-clause BSD license)
- [Remotery](https://github.com/Celtoys/Remotery) (Licensed and used under Apache License 2.0)

### Multithreading & Async
Our most complex mod, Behind The Scenery Engine: Concurrent requires some useful boilerplate code from the Core and opensource libraries to operate. We provide a bunch of utilities for multithreading tasks implementation into Minecraft.

We implemented lock-free and locked Collections, thread-safe Codecs and Random as a part of the Core. Feel free to use them for your projects.

Used libraries or bindings:
- [ConcurrentUtil by Spottedleaf](https://github.com/Tuinity/ConcurrentUtil) (Licensed and used under GNU GPLv3)
- [LMAX Disruptor](https://github.com/LMAX-Exchange/disruptor) (Licensed and used under Apache License 2.0)
- [Caffeine](https://github.com/ben-manes/caffeine) (Licensed and used under Apache License 2.0)

### 3D Rendering
Always wanted to put your favourite Blender model into Minecraft without sacrificing your FPS? We've got you covered.

You can use Assimp to load your models and operate it using the Assimp methods.

Used libraries or bindings:
- [Assimp](https://github.com/assimp/assimp) (Licensed and used under Open Asset Import Library license)
- [Tootle](https://github.com/GPUOpen-Archive/amd_tootle) (Licensed and used under GNU GPLv3)
- [Libdivide](https://github.com/ridiculousfish/libdivide) (Licensed and used under zlib license)
- [Meshoptimizer](https://github.com/zeux/meshoptimizer) (Licensed and used under GNU GPLv3)

## How to use?
For players, just download a mod file called `btsengine-core-x.x.x.jar`, put into the `mods` folder and run the game. That's it!

For mod developers, import the artifacts using CurseMaven and feel free to use the project under GNU General Public License v3.0. While most of the methods are for internal use and may be changed any time this library is supported.

## Ports/Backports?
This project is so huge, so it is very hard to maintain this across Minecraft versions. At the moment, 1.21.1 and NeoForge are the only versions/modloaders supported.

- **Fabric?** Maybe if there will be demand.
- **1.7.10**, **1.12.2** or any other version <1.21.1? No. It will require a lot of work from us to port over these versions. While other components are ARR, the core is licensed under GPLv3, so go ahead to fork it if you need BTSEngine: Core.
- - The exclusion may be 1.20.1, but it will be discussed later by the whole team, when we will feel that the project is on the state we would like to see.
- **1.22, 1.23?** We're not sure if we want to update the project to newer versions. The time will see. However, if something changes, we'll announce it here and there.
