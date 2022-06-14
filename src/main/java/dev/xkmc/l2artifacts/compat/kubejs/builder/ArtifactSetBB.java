package dev.xkmc.l2artifacts.compat.kubejs.builder;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.xkmc.l2artifacts.compat.kubejs.KubeJSArtifactPlugin;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.resources.ResourceLocation;

public class ArtifactSetBB extends BuilderBase<ArtifactSet> {

	public ArtifactSetBB(ResourceLocation rl) {
		super(rl);
	}

	@Override
	public RegistryObjectBuilderTypes<? super ArtifactSet> getRegistryType() {
		return KubeJSArtifactPlugin.ARTIFACT_SET;
	}

	@Override
	public ArtifactSet createObject() {
		return new ArtifactSet();
	}
}
