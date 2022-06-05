package dev.xkmc.l2artifacts.mixin;

import dev.xkmc.l2artifacts.init.AllEnchantments;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

	@Inject(at = @At("TAIL"), method = "hurtTo")
	public void hurtToMixin(float v, CallbackInfo ci) {
		LocalPlayer self = (LocalPlayer) (Object) this;
		ItemStack stack = self.getItemBySlot(EquipmentSlot.CHEST);
		int lv = EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.STABLE_BODY.get(), stack);
		if (lv > 0) {
			if (self.getHealth() > self.getMaxHealth() * (1 - ModConfig.COMMON.stableBodyThreshold.get() * lv)) {
				self.hurtTime = 0;
			}
		}
	}

}
