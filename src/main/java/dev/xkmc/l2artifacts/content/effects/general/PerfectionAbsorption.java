package dev.xkmc.l2artifacts.content.effects.general;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PerfectionAbsorption extends SetEffect {

	private final LinearFuncEntry period, max;

	public PerfectionAbsorption(LinearFuncEntry period, LinearFuncEntry max) {
		super(0);
		this.period = period;
		this.max = max;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled)
			return;
		int max = (int) this.max.getFromRank(rank);
		if (player.tickCount % period.getFromRank(rank) == 0) {
			if (player.getHealth() >= player.getMaxHealth()) {
				double current = player.getAbsorptionAmount();
				if (current < max) {
					player.setAbsorptionAmount((float) Math.min(max, current + 1));
				}
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		double period = this.period.getFromRank(item.rank) / 20;
		int max = (int) this.max.getFromRank(item.rank);
		return List.of(Component.translatable(getDescriptionId() + ".desc", period, max));
	}

}
