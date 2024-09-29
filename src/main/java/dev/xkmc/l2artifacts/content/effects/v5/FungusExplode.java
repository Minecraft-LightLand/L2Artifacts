package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class FungusExplode extends SetEffect {

	private final LinearFuncEntry range, rate;

	public FungusExplode(LinearFuncEntry range, LinearFuncEntry rate) {
		super(0);
		this.range = range;
		this.rate = rate;
	}

	@Override
	public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.DefenceMax cache) {
		if (cache.getSource().is(Tags.DamageTypes.IS_MAGIC)) return;
		int r = (int) range.getFromRank(rank);
		float dmg = cache.getDamageFinal() * (float) rate.getFromRank(rank);
		SchedulerHandler.schedule(() -> explode(player, cache.getTarget(), r, dmg));

	}

	private void explode(LivingEntity player, LivingEntity target, int r, float dmg) {
		for (var e : target.level().getEntities(target, target.getBoundingBox().inflate(r))) {
			if (e instanceof LivingEntity le && le.hasEffect(ArtifactEffects.FUNGUS)) {
				le.hurt(target.damageSources().indirectMagic(target, player), dmg);
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int r = (int) range.getFromRank(rank);
		int p = (int) Math.round(rate.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", p, r));
	}

}
