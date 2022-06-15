1. Event Propagation
2. Artifact Tick reduce loop iteration
3. BaseArtifact Item builder for KubeJS


As in you have x item that you want to let users register during a KubeJS event?

You’d start by making a KubeJSPlugin class (a class that extends KubeJSPlugin).

https://github.com/KubeJS-Mods/KubeJS-Thermal/blob/42ae39fb55a194808e2162c4a71c61a3945780db/src/main/java/dev/latvian/mods/kubejs/thermal/KubeJSThermalPlugin.java#L10

You’d then make a text file in your resource folder named kubejs.plugins.txt that gives the location of the plug-in class.

(Example from KubeJS Thermal)

 https://github.com/KubeJS-Mods/KubeJS-Thermal/blob/42ae39fb55a194808e2162c4a71c61a3945780db/src/main/resources/kubejs.plugins.txt#L1

Then you need to make a "builder class" for the registry type you want to support.

This is just a class that extends BuilderBase:

https://github.com/KubeJS-Mods/KubeJS/blob/ab13a2bfdd5af14f7b94722ec401f8f2e52f568d/common/src/main/java/dev/latvian/mods/kubejs/BuilderBase.java#L26

Then inside the init function you can do things like this:

https://github.com/KubeJS-Mods/KubeJS/blob/ab13a2bfdd5af14f7b94722ec401f8f2e52f568d/common/src/main/java/dev/latvian/mods/kubejs/BuiltinKubeJSPlugin.java#L170

Where you register your Builder class for that registry type.