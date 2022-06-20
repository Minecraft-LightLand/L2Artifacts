package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.compat.champions.ChampionLootGen;
import dev.xkmc.playerdifficulty.compat.CompatManager;
import dev.xkmc.playerdifficulty.init.PlayerDifficulty;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ArtifactGLMProvider extends GlobalLootModifierProvider {

	public ArtifactGLMProvider(DataGenerator gen) {
		super(gen, PlayerDifficulty.MODID);
	}

	@Override
	protected void start() {
		ChampionLootGen.onGLMGen(this);
	}
}
