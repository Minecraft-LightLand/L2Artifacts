package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ExecutorLimitEffect extends SetEffect {

	private final LinearFuncEntry factor;

	public ExecutorLimitEffect(LinearFuncEntry factor) {
		super(0);
		this.factor = factor;
	}

	@Override
	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		if (source.getEntity() == player) {
			cache.addDealtModifier(DamageModifier.nonlinearPre(546,
					f -> Math.min(f, player.getMaxHealth()) * (float) factor.getFromRank(rank)));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amount = this.factor.getFromRank(rank) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(amount)));
	}

}
