package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.InherentEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FleshOvergrowth extends InherentEffect implements IOverlayRenderEffect {

	public FleshOvergrowth(MobEffectCategory category, int color) {
		super(category, color);
		String str = "l2artifacts:flesh_overgrowth";
		var id = MathHelper.getUUIDFromString(str);
		addAttributeModifier(Attributes.MAX_HEALTH, id.toString(), 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
		addAttributeModifier(L2DamageTracker.REDUCTION.get(), id.toString(), 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

	public DelayedEntityRender getIcon(LivingEntity entity, int lv) {
		return DelayedEntityRender.icon(entity, new ResourceLocation(L2Artifacts.MODID, "textures/effect_overlay/flesh_" + (lv + 1) + ".png"));
	}

}
