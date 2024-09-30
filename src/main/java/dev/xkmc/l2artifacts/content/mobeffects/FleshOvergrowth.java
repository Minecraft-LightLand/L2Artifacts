package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2core.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FleshOvergrowth extends InherentEffect implements IconOverlayEffect {

	public FleshOvergrowth(MobEffectCategory category, int color) {
		super(category, color);
		var id = L2Artifacts.loc("flesh_overgrowth");
		addAttributeModifier(Attributes.MAX_HEALTH, id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(L2DamageTracker.REDUCTION, id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
		return DelayedEntityRender.icon(entity, L2Artifacts.loc("textures/effect_overlay/flesh_" + (lv + 1) + ".png"));
	}

}
