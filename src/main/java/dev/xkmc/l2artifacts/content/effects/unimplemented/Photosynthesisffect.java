package dev.xkmc.l2artifacts.content.effects.unimplemented;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.world.entity.player.Player;

public class Photosynthesisffect extends SetEffect {

	public Photosynthesisffect() {
		super(0);
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		if (player.getLevel().isClientSide()) return;
		if (player.tickCount % 60 != 0) return;
		int light = player.getLevel().getLightEngine().getRawBrightness(player.getOnPos(), 0);
		if (light >= 12) {
			float sat = player.getFoodData().getSaturationLevel();
			int food = player.getFoodData().getFoodLevel();
			if (sat < food) {
				player.getFoodData().setSaturation(Math.min(food, sat + 1));
			} else {
				player.getFoodData().setFoodLevel(Math.min(20, food + 1));
			}
		} else if (light <= 3) {
			player.getFoodData().addExhaustion(1);
		}
	}
}
