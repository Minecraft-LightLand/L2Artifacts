package dev.xkmc.l2artifacts.compat.kubejs.builder;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.xkmc.l2artifacts.compat.kubejs.KubeJSArtifactPlugin;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.resources.ResourceLocation;

public class AttributeSetEffectBB extends BuilderBase<SetEffect> {

	public AttributeSetEffectBB(ResourceLocation rl) {
		super(rl);
	}

	@Override
	public RegistryObjectBuilderTypes<? super SetEffect> getRegistryType() {
		return KubeJSArtifactPlugin.SET_EFFECT;
	}

	@Override
	public SetEffect createObject() {
		return new AttributeSetEffect();
	}
}
