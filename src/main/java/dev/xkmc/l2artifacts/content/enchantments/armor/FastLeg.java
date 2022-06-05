package dev.xkmc.l2artifacts.content.enchantments.armor;

import dev.xkmc.l2artifacts.content.enchantments.core.TickingEnchantment;
import dev.xkmc.l2library.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FastLeg extends ArmorEnchant implements TickingEnchantment {

	public FastLeg() {
		super(FAST_LEG);
	}

	@Override
	public void onInventoryTick(int lv, ItemStack stack, Entity entity, int slot, boolean selected) {
		if (entity instanceof Player player) {
			if (player.getItemBySlot(EquipmentSlot.LEGS) == stack) {
				if (player.getFoodData().getFoodLevel() >= 16) {
					EffectUtil.refreshEffect(player, new MobEffectInstance(
							MobEffects.MOVEMENT_SPEED, 40,lv - 1,  false, false, true
					), EffectUtil.AddReason.SELF, player);
				}
			}
		}
	}
}
