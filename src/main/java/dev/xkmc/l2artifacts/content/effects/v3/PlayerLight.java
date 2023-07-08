package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.init.registrate.items.LAItem3;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;

public class PlayerLight {

	public static int playerUnderSun(LivingEntity entity) {
		if (entity instanceof Player player)
			if (ConditionalData.HOLDER.get(player).hasData(LAItem3.EFF_SUN_BLOCK.get().getKey())) {
				return 0;
			}
		return entity.level().getBrightness(LightLayer.SKY, entity.getOnPos().above()) - entity.level().getSkyDarken();
	}

	public static int playerLight(LivingEntity entity) {
		return entity.level().getMaxLocalRawBrightness(entity.getOnPos().above());
	}
}
