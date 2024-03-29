package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2complements.content.effect.skill.SkillEffect;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FungusInfection extends InherentEffect implements SkillEffect {

	public FungusInfection(MobEffectCategory category, int color) {
		super(category, color);
		String str = "l2artifacts:fungus_infection";
		var id = MathHelper.getUUIDFromString(str);
		addAttributeModifier(Attributes.MAX_HEALTH, id.toString(), -0.16, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public double getAttributeModifierValue(int lv, AttributeModifier mod) {
		lv += 2;
		double base = 1 + mod.getAmount() / 2;
		return Math.pow(base, lv) - 1;
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
