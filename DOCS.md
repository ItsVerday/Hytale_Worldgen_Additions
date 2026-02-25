# Verday's Worldgen Additions Documentation

## Installation
To begin installing this plugin, you will need a `.jar` build of the plugin. You can download this from the [Releases](https://github.com/ItsVerday/Hytale_Worldgen_Additions/releases/) on GitHub, or you can build it yourself by cloning the repository and running the `gradlew build` command.

If you are going to use the Asset Node Editor with the custom nodes added by this plugin, you will also need to separately install the Asset Node Editor configs for those. You can either download a `.zip` file with these from the [Releases](https://github.com/ItsVerday/Hytale_Worldgen_Additions/releases/) on GitHub, or copy them from your cloned repository. You will need to place these in the `Hytale/install/release/package/game/latest/Client/NodeEditor/Workspaces/HytaleGenerator Java/` in your game's folder, replacing the `_Workspace.json` file (making a copy as a backup is recommended) and adding the new `CustomNodes` directory. After doing this, the Asset Node Editor should have the custom nodes upon being launched.

**NOTE:** Installing any custom Asset Node Editor nodes will cause the launcher to treat your game installation as corrupted, preventing you from updating. In order to update Hytale, you will either need to uninstall and reinstall the game, or revert all custom node config files manually (delete the `CustomNodes` folder, and restore `_Workspace.json` from a backup). Either way, you will need to reinstall all custom nodes after updating.

## Custom Nodes
### Density Nodes
#### StaticNoise2D Density
Produces "random" values according to the input position in the range (0-1), but always produces the same value for the same position and seed. For the 2D version of this node, the input position's Y-coordinate is ignored.

**Inputs:**
 - **Seed:** The seed for this Noise function. Different seeds will result in completely separate sets of values for different positions, but the distribution of values is always the same.

#### StaticNoise3D Density
Produces "random" values according to the input position in the range (0-1), but always produces the same value for the same position and seed. For the 3D version of this node, the input position's Y-coordinate is included in the calculation.

**Inputs:**
- **Seed:** The seed for this Noise function. Different seeds will result in completely separate sets of values for different positions, but the distribution of values is always the same.

#### FWidth Density
An advanced node, which is analogous to the `fwidth` function in shader programming. It measures the gradient of the input Density function, and returns the magnitude of that gradient at the given position.
Note that this node samples its input 4 times per position, so it is recommended to use as simple of a Density function as possible for the child of this node. If your input to this node is complex, consider caching the input to this node and using a SampleDistance of `1`.

**Inputs:**
- **SampleDistance (Default 1):** The distance between samples of the input Density function. Lower values will result in theoretically more precise values, but it is recommended to leave this at `1`, especially if you are caching the input to this node.
- **Inputs:** The density input to this node.

#### Boundary Density
A node which isolates an *approximately* constant width band of the input density, mapping values below that band to `0`, values above to `1`, and values inside into the range between `0` and `1`. This can be used to isolate certain sections of a Density function that are on a given threshold value.
This node works best if the input Density function to this node has a roughly constant magnitude of gradient, meaning the slope of the input function (ignoring direction) is roughly constant. Additionally, larger Width values will cause this node to become less accurate (the boundary will not be as close to constant width).
Note that this node samples its input 4 times per position, so it is recommended to use as simple of a Density function as possible for the child of this node. If your input to this node is complex, consider caching the input to this node.

**Inputs:**
- **Cutoff:** The value to place the boundary at. For example, setting this to `1` will isolate values sufficiently close to 1 from values too far above or below 1.
- **Width:** The approximate width of the boundary, in blocks. Lower values will result in boundaries that are more constant in width.
- **Bias (Default 0.5):** Controls whether the isolated boundary is entirely above, below, or in the middle of the cutoff value. If this node is isolating positions that you don't want isolated, you may need to adjust this value.
- **Inputs:** The density input to this node.

### Curve Nodes
#### Threshold Curve
A curve which returns a "high" value at or above a given cutoff, and a "low" value otherwise.

**Inputs:**
- **Low:** The "low" value to return if the input is below the cutoff.
- **High:** The "high" value to return if the input is at or above the cutoff.
- **Cutoff:** The cutoff value around which to return the low vs high values.

#### Steps Curve
A curve which has a number of "steps", allowing for the easy creation of "stepped" effects in terrain.

**Inputs:**
- **FromMin:** The minimum input to the stepping function.
- **FromMax:** The maximum input to the stepping function.
- **ToMin:** The minimum output from the stepping function.
- **ToMax:** The maximum output from the stepping function.
- **CurveExponent:** Applies a "bias" to the input of the stepping function, causing higher or lower steps to be wider/narrower.
- **StepCount:** The number of steps to include in the stepping function.
- **WallWidth:** Affects the slope of the steep part of steps, where higher values lead to wider/less steep walls.
- **StepSlope:** The slope of the flat part of steps, between 0 and 1.

