package dev.xkmc.l2artifacts.content.enchantments.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface TickingEnchantment {

	void onInventoryTick(int lv, ItemStack stack, Entity entity, int slot, boolean selected);

}
