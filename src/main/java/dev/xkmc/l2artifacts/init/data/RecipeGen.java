package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.LASets;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2library.serial.recipe.NBTRecipe;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class RecipeGen {

	private static void craftCombine(RegistrateRecipeProvider pvd, ItemEntry<?>[] arr) {
		for (int i = 1; i < 5; i++) {
			ItemEntry<?> input = arr[i - 1];
			ItemEntry<?> output = arr[i];
			pvd.singleItemUnfinished(DataIngredient.items(input.get()), RecipeCategory.MISC, output, 2, 1)
					.save(pvd, output.getId().withPrefix("upgrades/rank_up_"));
			pvd.singleItemUnfinished(DataIngredient.items(output.get()), RecipeCategory.MISC, input, 1, 2)
					.save(pvd, output.getId().withPrefix("upgrades/rank_down_"));
		}
	}

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		ITagManager<Item> manager = Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
		for (int i = 0; i < 5; i++) {
			int rank = i + 1;
			TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation(L2Artifacts.MODID, "rank_" + rank));
			ItemEntry<?> output = ArtifactItems.ITEM_EXP[i];
			pvd.singleItem(DataIngredient.tag(rank_tag), RecipeCategory.MISC, output, 1, 1);
		}

		craftCombine(pvd, ArtifactItems.ITEM_EXP);
		craftCombine(pvd, ArtifactItems.ITEM_STAT);
		craftCombine(pvd, ArtifactItems.ITEM_BOOST_MAIN);
		craftCombine(pvd, ArtifactItems.ITEM_BOOST_SUB);

		// rank up recipes
		for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
			ItemEntry<BaseArtifact>[][] items = set.items;
			for (ItemEntry<BaseArtifact>[] slot : items) {
				int n = slot.length;
				for (int i = 1; i < n; i++) {
					BaseArtifact input = slot[i - 1].get();
					BaseArtifact output = slot[i].get();
					unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, output, 1)::unlockedBy, input)
							.requires(input, 2)
							.save(ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(ArtifactConfig.COMMON_PATH,
											ArtifactConfig.COMMON.enableArtifactRankUpRecipe, true)),
									slot[i].getId().withPrefix("rank_up/"));
				}
			}
		}

		TagKey<Item> artifact = manager.createTagKey(new ResourceLocation(L2Artifacts.MODID, "artifact"));
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.FILTER.get(), 1)::unlockedBy, Items.ENDER_PEARL)
				.pattern(" A ").pattern("LEL").pattern(" L ")
				.define('E', Items.ENDER_PEARL).define('L', Items.LEATHER).define('A', artifact)
				.save(pvd);

		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.SWAP.get(), 1)::unlockedBy, Items.ENDER_PEARL)
				.pattern(" E ").pattern("LAL").pattern(" L ")
				.define('E', Items.ENDER_PEARL).define('L', Items.LEATHER).define('A', artifact)
				.save(pvd);

		unlock(pvd, SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ArtifactItems.FILTER.get()),
						Ingredient.of(Items.NETHERITE_INGOT),
						RecipeCategory.MISC,
						ArtifactItems.UPGRADED_POCKET.get())::unlocks,
				Items.NETHERITE_INGOT).save(pvd, L2Artifacts.MODID + ":upgraded_pocket");

		// conditionals
		{
			List<SetEntry<?>> set0 = List.of(LASets.SET_GAMBLER, LASets.SET_ARCHER, LASets.SET_BERSERKER,
					LASets.SET_PHYSICAL, LASets.SET_MAGE, LASets.SET_PIRATE, LASets.SET_DAMOCLES, LASets.SET_LUCKCLOVER);
			List<SetEntry<?>> set1 = List.of(LASets.SET_PERFECTION, LASets.SET_SAINT, LASets.SET_EXECUTOR,
					LASets.SET_VAMPIRE, LASets.SET_GLUTTONY, LASets.SET_FALLEN, LASets.SET_SUN_BLOCK, LASets.SET_LUCKCLOVER);
			List<SetEntry<?>> set2 = List.of(LASets.SET_FROZE, LASets.SET_WRATH, LASets.SET_ANCIENT,
					LASets.SET_FLESH, LASets.SET_FUNGUS, LASets.SET_SLIMY, LASets.SET_PHOTOSYN, LASets.SET_LUCKCLOVER);
			List<SetEntry<?>> set3 = List.of(LASets.SET_LONGSHOOTER, LASets.SET_ABYSSMEDAL, LASets.SET_CELL,
					LASets.SET_GILDED, LASets.SET_POISONOUS, LASets.SET_THERMAL, LASets.SET_PROTECTION, LASets.SET_LUCKCLOVER);

			for (int i = 0; i < 5; i++) {
				final int rank = i + 1;
				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::unlockedBy, LCItems.RESONANT_FEATHER.get())
						.pattern("1A2").pattern("AXA").pattern("2A1")
						.define('A', ArtifactItems.RANDOM[i])
						.define('X', ArtifactItems.ITEM_EXP[i])
						.define('1', LCItems.RESONANT_FEATHER.get())
						.define('2', LCItems.EXPLOSION_SHARD.get())
						.save(e -> ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID)
										.accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set0))),
								new ResourceLocation(L2Artifacts.MODID, "directed/set_0_rank_" + rank));

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::unlockedBy, LCItems.STORM_CORE.get())
						.pattern("1A2").pattern("AXA").pattern("2A1")
						.define('A', ArtifactItems.RANDOM[i])
						.define('X', ArtifactItems.ITEM_EXP[i])
						.define('1', LCItems.CAPTURED_BULLET.get())
						.define('2', LCItems.STORM_CORE.get())
						.save(e -> ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID)
										.accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set1))),
								new ResourceLocation(L2Artifacts.MODID, "directed/set_1_rank_" + rank));

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::unlockedBy, LCItems.SOUL_FLAME.get())
						.pattern("1A2").pattern("AXA").pattern("2A1")
						.define('A', ArtifactItems.RANDOM[i])
						.define('X', ArtifactItems.ITEM_EXP[i])
						.define('1', LCItems.HARD_ICE.get())
						.define('2', LCItems.SOUL_FLAME.get())
						.save(e -> ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID)
										.accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set2))),
								new ResourceLocation(L2Artifacts.MODID, "directed/set_2_rank_" + rank));

				unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.RANDOM[i], 4)::unlockedBy, LCItems.CAPTURED_WIND.get())
						.pattern("1A2").pattern("AXA").pattern("2A1")
						.define('A', ArtifactItems.RANDOM[i])
						.define('X', ArtifactItems.ITEM_EXP[i])
						.define('1', LCItems.WARDEN_BONE_SHARD.get())
						.define('2', LCItems.CAPTURED_WIND.get())
						.save(e -> ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID)
										.accept(new NBTRecipe(e, RandomArtifactItem.setList(rank, set3))),
								new ResourceLocation(L2Artifacts.MODID, "directed/set_3_rank_" + rank));
			}

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.ITEM_STAT[0].get(), 4)::unlockedBy, LCItems.RESONANT_FEATHER.get())
					.pattern("AAA").pattern("BCB").pattern("AAA")
					.define('A', Items.GOLD_INGOT)
					.define('B', LCItems.SOUL_FLAME.get())
					.define('C', LCItems.RESONANT_FEATHER.get())
					.save(ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID));

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.ITEM_BOOST_MAIN[0].get(), 4)::unlockedBy, LCItems.FORCE_FIELD.get())
					.pattern("ABA").pattern("BCB").pattern("ABA")
					.define('A', Items.GOLD_INGOT)
					.define('B', LCItems.STORM_CORE.get())
					.define('C', LCItems.FORCE_FIELD.get())
					.save(ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID));

			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, ArtifactItems.ITEM_BOOST_SUB[0].get(), 4)::unlockedBy, LCItems.EMERALD.get())
					.pattern("ABA").pattern("BCB").pattern("ABA")
					.define('A', Items.GOLD_INGOT)
					.define('B', LCItems.CAPTURED_WIND.get())
					.define('C', LCItems.EMERALD.get())
					.save(ConditionalRecipeWrapper.mod(pvd, L2Complements.MODID));
		}
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
