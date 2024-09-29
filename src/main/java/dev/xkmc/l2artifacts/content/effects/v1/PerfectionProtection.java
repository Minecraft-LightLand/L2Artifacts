package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PerfectionProtection extends SetEffect {

	private final LinearFuncEntry reduce;

	public PerfectionProtection(LinearFuncEntry reduce) {
		super(0);
		this.reduce = reduce;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int reduce = (int) Math.round(this.reduce.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", reduce));
	}

	@Override
	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
		if (player.getHealth() < player.getMaxHealth()) return;
		cache.addDealtModifier(DamageModifier.multTotal((float) (1 - reduce.getFromRank(rank)), getRegistryName()));
	}

}
