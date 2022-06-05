package dev.xkmc.l2artifacts.content.enchantments.sword;

import dev.xkmc.l2artifacts.content.enchantments.core.BaseEnchantment;
import dev.xkmc.l2artifacts.content.enchantments.core.ConflictGroup;
import dev.xkmc.l2artifacts.content.enchantments.core.EnchConfig;
import dev.xkmc.l2artifacts.content.enchantments.core.Type;
import dev.xkmc.l2artifacts.events.AttackEventHandler;
import dev.xkmc.l2artifacts.init.AllEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class SwordEnchant extends BaseEnchantment {

	public static final EnchConfig ANTI_MAGIC = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.PENETRATION, Type.ORANGE, 3);
	public static final EnchConfig SOUL_SLASH = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.PENETRATION, Type.ORANGE, 3);
	public static final EnchConfig STACK_DMG = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.STACKING, Type.ORANGE, 3);
	public static final EnchConfig TRACK_ENT = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.STACKING, Type.ORANGE, 3);
	public static final EnchConfig FRAGILE = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.NONE, Type.PURPLE, 5,
			() -> Enchantments.UNBREAKING, AllEnchantments.ROBUST::get);
	public static final EnchConfig LIGHT_SWING = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.WEIGHT, Type.PURPLE, 5);
	public static final EnchConfig HEAVY_SWING = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.WEIGHT, Type.PURPLE, 5);
	public static final EnchConfig WIND_SWEEP = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, ConflictGroup.WEIGHT, Type.ORANGE, 3);


	public static final Random RANDOM = new Random();

	protected SwordEnchant(EnchConfig config) {
		super(config, EquipmentSlot.MAINHAND);
	}

	public void onTargetAttacked(int lv, LivingAttackEvent event, AttackEventHandler.AttackCache attackCache) {
	}

	public int getAdditionalDamage(int lv, LivingHurtEvent event, AttackEventHandler.AttackCache attackCache) {
		return 0;
	}

	public void onTargetHurt(int lv, LivingHurtEvent event, AttackEventHandler.AttackCache attackCache) {
	}

	public void onTargetDamage(int lv, LivingDamageEvent event, AttackEventHandler.AttackCache attackCache) {
	}

}
