package dev.xkmc.l2artifacts.content.enchantments.tool;

import dev.xkmc.l2artifacts.content.enchantments.core.BaseEnchantment;
import dev.xkmc.l2artifacts.content.enchantments.core.ConflictGroup;
import dev.xkmc.l2artifacts.content.enchantments.core.EnchConfig;
import dev.xkmc.l2artifacts.content.enchantments.core.Type;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ToolEnchant extends BaseEnchantment {

	public static final EnchConfig REMNANT = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, ConflictGroup.NONE, Type.ORANGE, 1);
	public static final EnchConfig ROBUST = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, ConflictGroup.NONE, Type.ORANGE, 1);
	public static final EnchConfig REACH = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, ConflictGroup.NONE, Type.ORANGE, 1);
	public static final EnchConfig LIFE_SYNC = new EnchConfig(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, ConflictGroup.NONE, Type.PURPLE, 5);

	protected ToolEnchant(EnchConfig config) {
		super(config, EquipmentSlot.values());
	}
}
