package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class IceBreakEffect extends SetEffect {

	public static final double BASE = 0.2, SLOPE = 0.1;//TODO config

	public IceBreakEffect() {
		super(0);
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getAttackTarget().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
			double factor = BASE + rank * SLOPE;
			event.setDamageModified((float) (factor * event.getDamageModified()));
		}
	}
}
