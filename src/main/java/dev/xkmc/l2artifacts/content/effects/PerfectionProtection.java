package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class PerfectionProtection extends SetEffect {

	private final double reduce_base, reduce_slope;

	public PerfectionProtection(double reduce_base, double reduce_slope) {
		super(0);
		this.reduce_base = reduce_base;
		this.reduce_slope = reduce_slope;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		int reduce = (int) Math.round((reduce_base + (item.rank - 1) * reduce_slope) * 100);
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", reduce)));
	}

	@Override
	public <T extends Event> void propagateEvent(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled, T event) {
		if (!enabled) return;
		if (player.getHealth() < player.getMaxHealth()) return;
		if (event instanceof LivingHurtEvent hurt) {
			if (!hurt.getSource().isBypassMagic()) {
				hurt.setAmount((float) (hurt.getAmount() * (1 - reduce_base - reduce_slope * (rank - 1))));
			}
		}
	}

}
