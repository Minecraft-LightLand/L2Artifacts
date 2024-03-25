package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class LAItemMisc {

	public static final ItemEntry<Item> EXPLOSIVE_FUNGUS, PETRIFIED_FUNGUS, NUTRITIOUS_FUNGUS;

	static {

		PETRIFIED_FUNGUS = REGISTRATE.item("petrified_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(3).saturationMod(0.4f)
						.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2), 1)
						.alwaysEat().build()))).tag(Tags.Items.MUSHROOMS).register();

		EXPLOSIVE_FUNGUS = REGISTRATE.item("explosive_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f)
						.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2), 1)
						.alwaysEat().fast().build()))).tag(Tags.Items.MUSHROOMS).register();

		NUTRITIOUS_FUNGUS = REGISTRATE.item("nutritious_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(12).saturationMod(1.2f)
						.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 2), 1)
						.alwaysEat().fast().build()))).tag(Tags.Items.MUSHROOMS).register();

	}

	public static void register() {

	}

}
