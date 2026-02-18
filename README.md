# Verday's Hytale Worldgen Additions
## Installation
In order to install this plugin, you will need to build it from source. After cloning the repository, run `./gradlew build`. This will produce a `.jar` file in `build/libs/`, simply place this file into your mods folder and restart your world/server.

To get the Node Asset Editor to recognize the custom nodes added by this plugin, you will need to copy the provided node config directory from `src/main/resources/NodeEditor/` to your game's installation folder, specifically into `Hytale/install/release/package/game/latest/Client/NodeEditor/Workspaces/HytaleGenerator Java/`. After doing this, you should be able to start the Node Editor and see the custom nodes.

**NOTE:** Installing any custom Node Editor nodes will cause the launcher to treat your game installation as corrupted, preventing you from updating. In order to update Hytale, you will either need to uninstall the game, then reinstall (you will need to reinstall the custom nodes after doing this), or revert all custom node config files manually (delete the `CustomNodes` folder, and restore `_Workspace.json` from a backup.)