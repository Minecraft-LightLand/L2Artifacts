package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class WrathSlow extends SetEffect {

	public static final double FACTOR = 0.8, BASE = 1.2, SLOPE = 0.1;

	public WrathSlow() {
		super(0);
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		boolean bool = event.getAttackTarget().hasEffect(MobEffects.MOVEMENT_SLOWDOWN);
		double factor = bool ? BASE + rank * SLOPE : FACTOR;
		event.setDamageModified((float) (event.getDamageModified() * factor));
	}
	
}
