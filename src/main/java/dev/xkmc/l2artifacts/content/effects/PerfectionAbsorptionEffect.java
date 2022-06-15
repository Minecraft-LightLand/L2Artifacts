package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class PerfectionAbsorptionEffect extends SetEffect {

	private final int period, max_base, max_slope;

	public PerfectionAbsorptionEffect(int max_base, int max_slope) {
		super(0);
		this.period = 20;
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
		return List.of(new TranslatableComponent(getDescriptionId() + ".desc", max));
	}

}
