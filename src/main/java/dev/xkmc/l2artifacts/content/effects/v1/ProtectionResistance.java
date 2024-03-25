package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class ProtectionResistance extends SetEffect {

	public ProtectionResistance() {
		super(0);
	}

	@Override
	public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		cache.addDealtModifier(DamageModifier.multTotal((float) (Math.exp(player.getHealth() / player.getMaxHealth() - 1))));
	}

}
