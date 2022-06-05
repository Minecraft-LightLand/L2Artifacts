package dev.xkmc.l2artifacts.content.enchantments.tool;

import dev.xkmc.l2artifacts.content.enchantments.core.TickingEnchantment;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class LifeSync extends ToolEnchant implements TickingEnchantment {

	public static final DamageSource SOURCE = new DamageSource("life_sync").bypassArmor().bypassMagic();

	public static final Supplier<Double> VALUE = ModConfig.COMMON.lifeSync::get;

	public LifeSync() {
		super(LIFE_SYNC);
	}

	@Override
	public void onInventoryTick(int lv, ItemStack stack, Entity entity, int slot, boolean selected) {
		if (entity instanceof Player player) {
			if (player.invulnerableTime > 0) return;
			if (stack.getDamageValue() == 0) return;
			for (EquipmentSlot e : EquipmentSlot.values()) {
				if (player.getItemBySlot(e) == stack) {
					if (player.getHealth() / player.getMaxHealth() > 1 - VALUE.get() * lv) {
						player.hurt(SOURCE, 1);
						stack.setDamageValue(stack.getDamageValue() - 1);
					}
					return;
				}
			}
		}
	}

}
