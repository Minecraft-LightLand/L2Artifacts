package dev.xkmc.l2artifacts.compat.kubejs.builder;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.xkmc.l2artifacts.compat.kubejs.KubeJSArtifactPlugin;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class StatTypeBB extends BuilderBase<ArtifactStatType> {

	private ResourceLocation attribute = new ResourceLocation("generic.max_health");
	private AttributeModifier.Operation operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
	private boolean useMult = true;

	public StatTypeBB(ResourceLocation rl) {
		super(rl);
	}

	@Override
	public RegistryObjectBuilderTypes<? super ArtifactStatType> getRegistryType() {
		return KubeJSArtifactPlugin.STAT_TYPE;
	}

	public StatTypeBB setAttribute(String str){
		attribute = new ResourceLocation(str);
		return this;
	}

	public StatTypeBB setOperation(String str){
		operation = Enum.valueOf(AttributeModifier.Operation.class, str);
		return this;
	}

	public StatTypeBB usePercent(boolean val){
		useMult = val;
		return this;
	}

	@Override
	public ArtifactStatType createObject() {
		return new ArtifactStatType(() -> ForgeRegistries.ATTRIBUTES.getValue(attribute), operation, useMult);
	}

}
