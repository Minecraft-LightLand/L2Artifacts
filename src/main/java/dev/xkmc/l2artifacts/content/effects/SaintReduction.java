package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class SaintReduction extends SetEffect {

	private final double base, slope;

	public SaintReduction(double base, double slope) {
		super(0);
		this.base = base;
		this.slope = slope;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		int damage = (int) Math.round(100 * (1 - base));
		int reduction = (int) Math.round(100 * (1 - base - slope * item.rank));
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", damage, reduction)));
	}

	@Override
	public <T extends Event> void propagateEvent(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled, T event) {
		if (!enabled) return;
		if (event instanceof CriticalHitEvent crit) {
			crit.setDamageModifier((float) (crit.getDamageModifier() * (1 - base)));
		}
		if (event instanceof LivingHurtEvent hurt) {
			if (!hurt.getSource().isBypassMagic()) {
				float amp = (float) (1 - base - slope * rank);
				hurt.setAmount(hurt.getAmount() * amp);
			}
		}
	}
}
