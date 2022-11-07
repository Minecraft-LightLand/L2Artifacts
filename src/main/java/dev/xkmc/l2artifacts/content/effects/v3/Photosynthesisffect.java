package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Photosynthesisffect extends SetEffect {

	private final LinearFuncEntry period, lightLow, lightHigh;

	public Photosynthesisffect(LinearFuncEntry period, LinearFuncEntry lightLow, LinearFuncEntry lightHigh) {
		super(0);
		this.period = period;
		this.lightLow = lightLow;
		this.lightHigh = lightHigh;
	}


	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double time = period.getFromRank(rank) / 20d;
		int lo = (int) Math.round(lightLow.getFromRank(rank));
		int hi = (int) Math.round(lightHigh.getFromRank(rank));
		return List.of(Component.translatable(getDescriptionId() + ".desc", time, hi, lo));
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		if (player.getLevel().isClientSide()) return;
		if (player.tickCount % period.getFromRank(rank) != 0) return;
		int sun = PlayerLight.playerUnderSun(player);

		int light = PlayerLight.playerLight(player);
		if (sun >= lightHigh.getFromRank(rank)) {
			float sat = player.getFoodData().getSaturationLevel();
			int food = player.getFoodData().getFoodLevel();
			if (sat < food) {
				player.getFoodData().setSaturation(Math.min(food, sat + 1));
			} else {
				player.getFoodData().setFoodLevel(Math.min(20, food + 1));
			}
		} else if (light < lightLow.getFromRank(rank)) {
			player.getFoodData().addExhaustion(0.1f);
		}
	}
}
