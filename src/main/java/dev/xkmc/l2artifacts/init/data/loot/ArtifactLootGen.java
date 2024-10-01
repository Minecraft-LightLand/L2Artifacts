package dev.xkmc.l2artifacts.init.data.loot;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.items.LAItemMisc;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ArtifactLootGen {

	public static final ResourceKey<LootTable> DROP_FUNGUS = ResourceKey.create(Registries.LOOT_TABLE, L2Artifacts.loc("drop_fungus"));

	public static void onLootGen(RegistrateLootTableProvider pvd) {
		pvd.addLootAction(LootContextParamSets.EMPTY, cons ->
				cons.accept(DROP_FUNGUS, LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootTableTemplate.getItem(LAItemMisc.EXPLOSIVE_FUNGUS.get(), 1))
						.add(LootTableTemplate.getItem(LAItemMisc.PETRIFIED_FUNGUS.get(), 1))
						.add(LootTableTemplate.getItem(LAItemMisc.NUTRITIOUS_FUNGUS.get(), 1))
				)));
	}


}
