package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2core.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FungusInfection extends InherentEffect implements IconOverlayEffect {

	public FungusInfection(MobEffectCategory category, int color) {
		super(category, color);
		var id = L2Artifacts.loc("fungus_infection");
		addAttributeModifier(Attributes.MAX_HEALTH, id, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
				lv -> Math.pow(0.92, lv + 2) - 1);
	}


	public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
		return DelayedEntityRender.icon(entity, L2Artifacts.loc("textures/effect_overlay/fungus_infection.png"));
	}
}
