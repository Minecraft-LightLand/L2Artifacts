package dev.xkmc.l2artifacts.compat.champions;

import dev.xkmc.l2artifacts.init.data.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.LootTableTemplate;
import dev.xkmc.playerdifficulty.compat.champions.ChampionDataGenHelper;
import dev.xkmc.playerdifficulty.init.PDRegistry;
import dev.xkmc.playerdifficulty.init.loot.PDLootModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;

public class ChampionLootGen {

	public static void onGLMGen(ArtifactGLMProvider pvd) {
		Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("champions:champion_properties"), LootItemChampionPropertyCondition.INSTANCE);
		for (int i = 1; i <= 4; i++) {
			LootPool.Builder pool = LootTableTemplate.getPool(1, 0);
			addSets(pool, i,
					ArtifactItemRegistry.SET_BERSERKER,
					ArtifactItemRegistry.SET_GAMBLER,
					ArtifactItemRegistry.SET_PERFECTION,
					ArtifactItemRegistry.SET_SAINT,
					ArtifactItemRegistry.SET_DAMOCLES,
					ArtifactItemRegistry.SET_PROTECTION);
			pool.when(LootTableTemplate.chance(i * 0.2f, i * 0.05f));
			pvd.add("champions_rank_" + i, PDRegistry.SER.get(), new PDLootModifier(LootTable.lootTable().withPool(pool).build(),
					ChampionDataGenHelper.getTierExact(i)));
		}
	}

	private static void addSets(LootPool.Builder pool, int i, ArtifactRegistrate.SetEntry<?>... sets) {
		for (ArtifactRegistrate.SetEntry<?> set : sets) {
			for (ItemEntry<?>[] item : set.items) {
				pool.add(LootTableTemplate.getItem(item[i-1].get(), 1));
			}
		}
	}

}
