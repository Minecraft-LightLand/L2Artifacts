package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerLight {

	public static int playerUnderSun(LivingEntity entity) {
		if (entity instanceof Player player)
			if (ArtifactData.HOLDER.get(player).hasData(ArtifactItemRegistry.EFF_SUN_BLOCK.get())) {
				return 0;
			}
		return entity.getLevel().getLightEngine().getRawBrightness(entity.getOnPos(), -64) - 64;
	}
	public static int playerLight(LivingEntity entity) {
		return entity.getLevel().getLightEngine().getRawBrightness(entity.getOnPos(), 0);
	}
}
