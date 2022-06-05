package dev.xkmc.l2artifacts.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.content.enchantments.core.DurabilityEnchantment;
import dev.xkmc.l2artifacts.content.enchantments.core.TickingEnchantment;
import dev.xkmc.l2artifacts.init.AllEnchantments;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract boolean isDamageableItem();

	@Shadow
	public abstract int getDamageValue();

	@Shadow
	public abstract void setDamageValue(int p_41722_);

	@Shadow
	public abstract int getMaxDamage();

	@Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
	public void hurt(int damage, Random random, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
		if (!isDamageableItem()) {
			cir.setReturnValue(false);
			return;
		}

		ItemStack self = (ItemStack) (Object) this;

		if (damage > 0) {
			int lv = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, self);
			double pre = damage / (1d + lv);
			for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(self).entrySet()) {
				if (ent.getKey() instanceof DurabilityEnchantment dur) {
					pre *= dur.durabilityFactor(ent.getValue(), damage);
				}
			}
			damage = (int) Math.floor(pre) + (random.nextFloat() < pre - Math.floor(pre) ? 1 : 0);
		}

		if (damage >= 1 && getDamageValue() + damage >= getMaxDamage()) {
			if (EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.REMNANT.get(), self) > 0) {//TODO
				setDamageValue(getMaxDamage());
				cir.setReturnValue(false);
				return;
			}
		}

		if (damage <= 0) {
			cir.setReturnValue(false);
		}

		if (player != null && damage != 0) {
			CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, self, getDamageValue() + damage);
		}

		int l = getDamageValue() + damage;
		setDamageValue(l);
		cir.setReturnValue(l >= getMaxDamage());

	}

	@Inject(at = @At("HEAD"), method = "getAttributeModifiers", cancellable = true)
	public void getDefaultModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.isEnchanted()) {
			if (EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.REMNANT.get(), self) > 0) {//TODO
				if (self.getDamageValue() >= self.getMaxDamage()) {
					cir.setReturnValue(HashMultimap.create());
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "inventoryTick")
	public void onInventoryTick(Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
		ItemStack self = (ItemStack) (Object) this;
		if (self.isEnchanted()) {
			for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(self).entrySet()) {
				if (ent.getKey() instanceof TickingEnchantment ticker) {
					ticker.onInventoryTick(ent.getValue(), self, entity, slot, selected);
				}
			}
		}
	}

	public AABB getSweepHitBox(Player player, Entity target) {
		ItemStack self = (ItemStack) (Object) this;
		int lv = EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.WIND_SWEEP.get(), self);
		double amount = ModConfig.COMMON.windSweepIncrement.get();
		AABB box = self.getItem().getSweepHitBox(self, player, target);
		if (lv > 0) {
			box = box.inflate(amount * lv, 0, amount * lv);
		}
		return box;

	}


}
