package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Predicate;

public class WrathEffect extends SetEffect {

	private final Predicate<LivingEntity> pred;
	private final LinearFuncEntry dec, inc;

	public WrathEffect(Predicate<LivingEntity> pred, LinearFuncEntry dec, LinearFuncEntry inc) {
		super(0);
		this.pred = pred;
		this.dec = dec;
		this.inc = inc;
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		boolean bool = pred.test(event.getAttackTarget());
		double factor = bool ? inc.getFromRank(rank) : dec.getFromRank(rank);
		event.addHurtModifier(DamageModifier.multTotal((float) factor));// multiplicative
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double inc = this.inc.getFromRank(rank) * 100;
		double dec = this.dec.getFromRank(rank) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(inc), (int) Math.round(dec)));
	}

}
