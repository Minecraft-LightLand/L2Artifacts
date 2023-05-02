package dev.xkmc.l2artifacts.init.data.loot;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ArtifactGLMProvider extends GlobalLootModifierProvider {

	public ArtifactGLMProvider(DataGenerator gen) {
		super(gen.getPackOutput(), L2Artifacts.MODID);
	}

	@Override
	protected void start() {
		this.add("health_based_1", new ArtifactLootModifier(100, 200, 1, ArtifactItemRegistry.RANDOM[0].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_2", new ArtifactLootModifier(200, 300, 1, ArtifactItemRegistry.RANDOM[1].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_3", new ArtifactLootModifier(300, 400, 1, ArtifactItemRegistry.RANDOM[2].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_4", new ArtifactLootModifier(400, 500, 1, ArtifactItemRegistry.RANDOM[3].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_5", new ArtifactLootModifier(500, Integer.MAX_VALUE, 1, ArtifactItemRegistry.RANDOM[4].asStack(), LootTableTemplate.byPlayer().build()));
	}
}
