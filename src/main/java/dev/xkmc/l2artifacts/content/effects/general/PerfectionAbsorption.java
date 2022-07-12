package dev.xkmc.l2artifacts.content.effects.general;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PerfectionAbsorption extends SetEffect {

	private final int period, max_base, max_slope;

	public PerfectionAbsorption(int max_base, int max_slope) {
		super(0);
		this.period = 100;
		this.max_base = max_base;
		this.max_slope = max_slope;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled)
			return;
		int max = max_base + (rank - 1) * max_slope;
		if (player.tickCount % period == 0) {
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
		int max = max_base + (item.rank - 1) * max_slope;
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", max)));
	}

}
