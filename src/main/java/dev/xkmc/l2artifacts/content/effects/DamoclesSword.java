package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class DamoclesSword extends SetEffect {

	private final double amplify_base, amplify_slope;

	public DamoclesSword(double amplify_base, double amplify_slope) {
		super(0);
		this.amplify_base = amplify_base;
		this.amplify_slope = amplify_slope;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled)
			return;
		if (player.getHealth() < player.getMaxHealth() / 2 && player.hurtTime == 0) {
			player.hurt(DamageSource.OUT_OF_WORLD, player.getMaxHealth());
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		double amplify = amplify_base + (item.rank - 1) * amplify_slope;
		int amount = (int) Math.round(amplify * 100);
		return List.of(new TranslatableComponent(getDescriptionId() + ".desc", amount));
	}

	@Override
	public <T extends Event> void propagateEvent(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled, T event) {
		if (!enabled) return;
		if (player.getHealth() < player.getMaxHealth()) return;
		if (event instanceof CriticalHitEvent crit) {
			double amplify = 1 + amplify_base + (rank - 1) * amplify_slope;
			crit.setDamageModifier((float) (crit.getDamageModifier() * amplify));
		}
	}

}
