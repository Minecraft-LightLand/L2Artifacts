package dev.xkmc.l2artifacts.content.enchantments.armor;

import dev.xkmc.l2artifacts.content.enchantments.core.BaseEnchantment;
import dev.xkmc.l2artifacts.content.enchantments.core.ConflictGroup;
import dev.xkmc.l2artifacts.content.enchantments.core.EnchConfig;
import dev.xkmc.l2artifacts.content.enchantments.core.Type;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ArmorEnchant extends BaseEnchantment {

	public static final EnchConfig STABLE_BODY = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, ConflictGroup.NONE, Type.ORANGE, 5);
	public static final EnchConfig FAST_LEG = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, ConflictGroup.NONE, Type.ORANGE, 5);
	public static final EnchConfig INVISIBLE_ARMOR = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, ConflictGroup.APPEARANCE, Type.ORANGE, 1);
	public static final EnchConfig DIGEST = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, ConflictGroup.NONE, Type.PURPLE, 1);

	private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	private static EquipmentSlot[] getSlots(EnchantmentCategory category) {
		return switch (category) {
			case ARMOR_HEAD -> new EquipmentSlot[]{EquipmentSlot.HEAD};
			case ARMOR_CHEST -> new EquipmentSlot[]{EquipmentSlot.CHEST};
			case ARMOR_LEGS -> new EquipmentSlot[]{EquipmentSlot.LEGS};
			case ARMOR_FEET -> new EquipmentSlot[]{EquipmentSlot.FEET};
			default -> ARMOR_SLOTS;
		};
	}

	protected ArmorEnchant(EnchConfig config) {
		super(config, getSlots(config.category()));
	}

}
