package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class LAItemMisc {

	public static final ItemEntry<Item> EXPLOSIVE_FUNGUS, PETRIFIED_FUNGUS, NUTRITIOUS_FUNGUS;

	static {

		PETRIFIED_FUNGUS = REGISTRATE.item("petrified_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f)
						.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1), 1)
						.alwaysEdible().build()))).tag(Tags.Items.MUSHROOMS).register();

		EXPLOSIVE_FUNGUS = REGISTRATE.item("explosive_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8f)
						.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1), 1)
						.alwaysEdible().fast().build()))).tag(Tags.Items.MUSHROOMS).register();

		NUTRITIOUS_FUNGUS = REGISTRATE.item("nutritious_fungus", p -> new Item(
				p.food(new FoodProperties.Builder().nutrition(12).saturationModifier(1.2f)
						.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1)
						.alwaysEdible().fast().build()))).tag(Tags.Items.MUSHROOMS).register();

	}

	public static void register() {

	}

}
