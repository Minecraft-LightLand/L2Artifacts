package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2core.base.effects.EffectUtil;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class FrozeSlowEffect extends SetEffect {

	private final LinearFuncEntry factor, period, level;

	public FrozeSlowEffect(LinearFuncEntry factor, LinearFuncEntry period, LinearFuncEntry level) {
		super(0);
		this.factor = factor;
		this.period = period;
		this.level = level;
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		if (player.isOnFire()) return;
		EffectUtil.addEffect(event.getTarget(), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
				(int) period.getFromRank(rank), (int) level.getFromRank(rank)), player);
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double dmg = this.factor.getFromRank(rank) * 100;
		double period = this.period.getFromRank(rank) / 20;
		Component level = Component.translatable("potion.potency." + (int) this.level.getFromRank(rank));
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(dmg), level, period));
	}

	@Override
	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
		if (source.is(DamageTypeTags.IS_FIRE)) {
			cache.addDealtModifier(DamageModifier.multTotal((float) factor.getFromRank(rank), getRegistryName()));
		}
	}

}
