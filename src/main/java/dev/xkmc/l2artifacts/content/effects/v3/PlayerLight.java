package dev.xkmc.l2artifacts.content.effects.v3;

import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;

public class PlayerLight {

	public static int playerUnderSun(LivingEntity player){
		return player.getLevel().getLightEngine().getRawBrightness(player.getOnPos(), -64) - 64;
	}

}
