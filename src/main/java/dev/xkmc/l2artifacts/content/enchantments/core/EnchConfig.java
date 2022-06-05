package dev.xkmc.l2artifacts.content.enchantments.core;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Supplier;

public final class EnchConfig {

	private final Enchantment.Rarity rarity;
	private final EnchantmentCategory category;
	private final ConflictGroup group;
	private final Type type;
	private final int max_level;
	private final Supplier<Enchantment>[] exclude;

	@SafeVarargs
	public EnchConfig(Enchantment.Rarity rarity, EnchantmentCategory category, ConflictGroup group, Type type, int max_level,
					  Supplier<Enchantment>... exclude) {
		this.rarity = rarity;
		this.category = category;
		this.group = group;
		this.type = type;
		this.max_level = max_level;
		this.exclude = exclude;
	}

	public boolean shouldExclude(Enchantment other) {
		for (Supplier<Enchantment> e : exclude)
			if (e.get() == other) return true;
		return false;
	}

	public Enchantment.Rarity rarity() {
		return rarity;
	}

	public EnchantmentCategory category() {
		return category;
	}

	public ConflictGroup group() {
		return group;
	}

	public Type type() {
		return type;
	}

	public int max_level() {
		return max_level;
	}

}
