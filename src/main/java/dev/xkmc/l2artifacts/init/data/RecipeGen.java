package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.init.AllEnchantments;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.ANTI_MAGIC.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.COPPER_INGOT)
				.define('1', new EnchantmentIngredient(Enchantments.SMITE, 3))
				.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.SOUL_SLASH.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.SOUL_SAND)
				.define('1', new EnchantmentIngredient(Enchantments.BANE_OF_ARTHROPODS, 3))
				.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.STACK_DMG.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.GLOWSTONE_DUST)
				.define('1', new EnchantmentIngredient(Enchantments.THORNS, 1))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.TRACK_ENT.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.REDSTONE)
				.define('1', new EnchantmentIngredient(Enchantments.SHARPNESS, 3))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.FRAGILE.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.GLASS)
				.define('1', new EnchantmentIngredient(Enchantments.SHARPNESS, 3))
				.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.LIGHT_SWING.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.IRON_INGOT)
				.define('1', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 3))
				.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.HEAVY_SWING.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.GOLD_INGOT)
				.define('1', new EnchantmentIngredient(Enchantments.UNBREAKING, 1))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.REMNANT.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("3A4").pattern("C2C")
				.define('A', Items.BOOK).define('C', Items.PHANTOM_MEMBRANE)
				.define('1', new EnchantmentIngredient(Enchantments.UNBREAKING, 3))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.define('3', new EnchantmentIngredient(Enchantments.MENDING, 1))
				.define('4', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.ROBUST.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("1A1").pattern("C2C")
				.define('A', Items.BOOK).define('C', Items.DIAMOND_CHESTPLATE)
				.define('1', new EnchantmentIngredient(Enchantments.UNBREAKING, 3))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.REACH.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("B1C").pattern("1A1").pattern("D2E")
				.define('A', Items.BOOK)
				.define('B', Items.DIAMOND_PICKAXE).define('C', Items.DIAMOND_SWORD)
				.define('D', Items.DIAMOND_AXE).define('E', Items.DIAMOND_SHOVEL)
				.define('1', new EnchantmentIngredient(Enchantments.POWER_ARROWS, 3))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.LIFE_SYNC.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.CACTUS)
				.define('1', new EnchantmentIngredient(Enchantments.MENDING, 1))
				.define('2', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.WIND_SWEEP.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.LAPIS_LAZULI).define('C', Items.DIAMOND_SWORD)
				.define('1', new EnchantmentIngredient(Enchantments.SWEEPING_EDGE, 1))
				.define('2', new EnchantmentIngredient(Enchantments.KNOCKBACK, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.STABLE_BODY.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.NETHERITE_INGOT).define('C', Items.DIAMOND_CHESTPLATE)
				.define('1', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.FAST_LEG.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.NETHER_STAR).define('C', Items.RABBIT_FOOT)
				.define('1', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 5))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.INVISIBLE_ARMOR.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.NETHER_STAR).define('C', Items.PHANTOM_MEMBRANE)
				.define('1', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1))
				.save(pvd);

		unlock(pvd, new EnchantmentRecipeBuilder(AllEnchantments.DIGEST.get(), 1)::unlockedBy, Items.ENCHANTED_BOOK)
				.pattern("C1C").pattern("BAB").pattern("C2C")
				.define('A', Items.BOOK).define('B', Items.GOLDEN_APPLE).define('C', Items.COOKED_RABBIT)
				.define('1', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 5))
				.define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1))
				.save(pvd);

	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

}
