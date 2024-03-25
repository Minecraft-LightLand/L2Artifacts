package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2complements.content.effect.skill.SkillEffect;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FrostShield extends InherentEffect implements SkillEffect {

	public FrostShield(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
