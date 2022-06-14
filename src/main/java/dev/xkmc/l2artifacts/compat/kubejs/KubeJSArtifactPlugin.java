package dev.xkmc.l2artifacts.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;

public class KubeJSArtifactPlugin extends KubeJSPlugin {

	public static final RegistryObjectBuilderTypes<SetEffect> SET_EFFECT;
	public static final RegistryObjectBuilderTypes<ArtifactSet> ARTIFACT_SET;
	public static final RegistryObjectBuilderTypes<ArtifactStatType> STAT_TYPE;

	static {
		ARTIFACT_SET = RegistryObjectBuilderTypes.add(ArtifactRegistry.SET.getRegistryKey(), ArtifactSet.class);
		SET_EFFECT = RegistryObjectBuilderTypes.add(ArtifactRegistry.SET_EFFECT.getRegistryKey(), SetEffect.class);
		STAT_TYPE = RegistryObjectBuilderTypes.add(ArtifactRegistry.STAT_TYPE.getRegistryKey(), ArtifactStatType.class);
	}

	@Override
	public void init() {
		

	}

	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
	}
}