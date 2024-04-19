package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.IconOverlayEffect;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public interface IOverlayRenderEffect extends IconOverlayEffect {

	default void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> adder) {
		if (entity != Proxy.getClientPlayer()) {
			IconOverlayEffect.super.render(entity, lv, adder);
		}
	}

}
