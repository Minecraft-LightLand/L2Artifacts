package dev.xkmc.l2artifacts.content.enchantments.core;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class BaseEnchantment extends Enchantment {

	private final EnchConfig config;

	protected BaseEnchantment(EnchConfig config, EquipmentSlot... slot) {
		super(config.rarity(), config.category(), slot);
		this.config = config;
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return config.max_level();
	}

	@Override
	public int getMinCost(int lv) {
		return 200;
	}

	@Override
	public int getMaxCost(int lv) {
		return 150;
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return false;
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		if (other instanceof BaseEnchantment base) {
			if (base.config.group() != ConflictGroup.NONE && base.config.group() == config.group())
				return false;
		} else if (config.shouldExclude(other)) {
			return false;
		}
		return super.checkCompatibility(other);
	}

	@Override
	public Component getFullname(int lv) {
		MutableComponent component = new TranslatableComponent(this.getDescriptionId());

		if (lv != 1 || this.getMaxLevel() != 1) {
			component.append(" ").append(new TranslatableComponent("enchantment.level." + lv));
		}

		if (config.type() == Type.ORANGE) {
			component.withStyle(ChatFormatting.GOLD);
		} else if (config.type() == Type.PURPLE) {
			component.withStyle(ChatFormatting.DARK_PURPLE);
		}

		return component;
	}
}
