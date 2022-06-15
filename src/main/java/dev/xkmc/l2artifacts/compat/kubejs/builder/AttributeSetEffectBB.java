package dev.xkmc.l2artifacts.compat.kubejs.builder;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.xkmc.l2artifacts.compat.kubejs.KubeJSArtifactPlugin;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AttributeSetEffectBB extends BuilderBase<SetEffect> {

	private final List<AttributeSetEffect.AttrSetEntry> list = new ArrayList<>();

	public AttributeSetEffectBB(ResourceLocation rl) {
		super(rl);
	}

	public AttributeSetEffectBB addEntry(String attr, String op, double base, double slope, boolean usePercent) {
		list.add(new AttributeSetEffect.AttrSetEntry(() -> ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attr)),
				Enum.valueOf(AttributeModifier.Operation.class, op), base, slope, usePercent));
		return this;
	}

	@Override
	public RegistryObjectBuilderTypes<? super SetEffect> getRegistryType() {
		return KubeJSArtifactPlugin.SET_EFFECT;
	}

	@Override
	public SetEffect createObject() {
		return new AttributeSetEffect(list.toArray(new AttributeSetEffect.AttrSetEntry[0]));
	}
}
