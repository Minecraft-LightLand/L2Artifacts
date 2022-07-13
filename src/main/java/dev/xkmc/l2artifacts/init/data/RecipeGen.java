package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Objects;
import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		ITagManager<Item> manager = Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
		for (int i = 0; i < 5; i++) {
			int rank = i + 1;
			TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation(L2Artifacts.MODID, "rank_" + rank));
			ItemEntry<?> output = ArtifactItemRegistry.RANKED_ITEMS[i];
			pvd.singleItem(DataIngredient.tag(rank_tag), output, 1, 1);
			if (i >= 1) {
				ItemEntry<?> input = ArtifactItemRegistry.RANKED_ITEMS[i - 1];
				pvd.singleItemUnfinished(DataIngredient.items(input), output, 2, 1).save(pvd, new ResourceLocation(L2Artifacts.MODID, "rank_up_" + i));
				pvd.singleItemUnfinished(DataIngredient.items(output), input, 1, 2).save(pvd, new ResourceLocation(L2Artifacts.MODID, "rank_down_" + i));
			}
		}

		// rank up recipes
		for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
			ItemEntry<BaseArtifact>[][] items = set.items;
			for (ItemEntry<BaseArtifact>[] slot : items) {
				int n = slot.length;
				for (int i = 1; i < n; i++) {
					BaseArtifact input = slot[i - 1].get();
					Item corner = ArtifactItemRegistry.RANKED_ITEMS[input.rank - 1].get();
					BaseArtifact output = slot[i].get();
					Item center = ArtifactItemRegistry.RANKED_ITEMS[output.rank - 1].get();
					craft(pvd, output, center, input, corner);
				}
			}
		}
	}

	public static void craft(RegistrateRecipeProvider pvd, Item output, Item center, Item input, Item corner) {
		unlock(pvd, new ShapedRecipeBuilder(output, 1)::unlockedBy, input)
				.pattern("CCC").pattern("BAB").pattern("CCC")
				.define('A', center)
				.define('B', input)
				.define('C', corner)
				.save(pvd);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
