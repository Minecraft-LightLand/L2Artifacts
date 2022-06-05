package dev.xkmc.l2artifacts.content.enchantments.armor;

import dev.xkmc.l2artifacts.content.enchantments.core.AttributeEnchantment;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import dev.xkmc.l2library.util.MathHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class StableBody extends ArmorEnchant implements AttributeEnchantment {

	public static final UUID ID = MathHelper.getUUIDfromString("stable_body");

	public static final Supplier<Double> VALUE = ModConfig.COMMON.stableBodyResistance::get;

	public StableBody() {
		super(STABLE_BODY);
	}

	@Override
	public void addAttributes(int lv, ItemAttributeModifierEvent event) {
		if (event.getSlotType() == EquipmentSlot.CHEST) {
			event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ID, "stable_body", VALUE.get() * lv, AttributeModifier.Operation.ADDITION));
		}
	}
}
