package dev.xkmc.l2artifacts.init.data.loot;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ArtifactGLMProvider extends GlobalLootModifierProvider {

	public ArtifactGLMProvider(DataGenerator gen) {
		super(gen.getPackOutput(), L2Artifacts.MODID);
	}

	@Override
	protected void start() {
		this.add("health_based", new ArtifactLootModifier(LootTableTemplate.byPlayer().build()));
	}
}
