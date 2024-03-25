package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FungusExplode extends SetEffect {

	private final LinearFuncEntry range, rate;

	public FungusExplode(LinearFuncEntry range, LinearFuncEntry rate) {
		super(0);
		this.range = range;
		this.rate = rate;
	}

	@Override
	public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
		var event = cache.getLivingDamageEvent();
		assert event != null;
		if (event.getSource().is(L2DamageTypes.MAGIC)) return;
		int r = (int) range.getFromRank(rank);
		float dmg = cache.getDamageDealt() * (float) rate.getFromRank(rank);
		GeneralEventHandler.schedule(() -> explode(player, cache.getAttackTarget(), r, dmg));

	}

	private void explode(Player player, LivingEntity target, int r, float dmg) {
		for (var e : target.level().getEntities(target, target.getBoundingBox().inflate(r))) {
			if (e instanceof LivingEntity le && le.hasEffect(ArtifactEffects.FUNGUS.get())) {
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
