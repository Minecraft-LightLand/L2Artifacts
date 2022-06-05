package dev.xkmc.l2artifacts.mixin;

import dev.xkmc.l2artifacts.init.AllEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract Iterable<ItemStack> getArmorSlots();

	/**
	 * @author lcy0x1
	 */
	@Overwrite
	public float getArmorCoverPercentage() {
		Iterable<ItemStack> iterable = getArmorSlots();
		int total = 0;
		int visible = 0;
		for (ItemStack itemstack : iterable) {
			if (!itemstack.isEmpty()) {
				if (EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.INVISIBLE_ARMOR.get(), itemstack) == 0)
					++visible;
			}
			++total;
		}
		return total > 0 ? (float) visible / (float) total : 0.0F;
	}

}
