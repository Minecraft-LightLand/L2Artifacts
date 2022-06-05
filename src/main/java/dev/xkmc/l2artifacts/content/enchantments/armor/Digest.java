package dev.xkmc.l2artifacts.content.enchantments.armor;

import dev.xkmc.l2artifacts.content.enchantments.core.TickingEnchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

public class Digest extends ArmorEnchant implements TickingEnchantment {

	public Digest() {
		super(DIGEST);
	}

	@Override
	public void onInventoryTick(int lv, ItemStack stack, Entity entity, int slot, boolean selected) {
		if (entity instanceof Player player) {
			if (player.getItemBySlot(EquipmentSlot.CHEST) == stack) {
				FoodData food = player.getFoodData();
				if (food.getSaturationLevel() + 1 < food.getFoodLevel() / 2f - 1) {
					food.setFoodLevel(food.getFoodLevel() - 2);
					food.setSaturation(food.getSaturationLevel() + 1);
				}
			}
		}
	}
}
