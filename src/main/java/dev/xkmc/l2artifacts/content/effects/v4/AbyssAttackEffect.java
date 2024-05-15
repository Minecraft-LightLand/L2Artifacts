package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;


public class AbyssAttackEffect extends SetEffect {

	private final LinearFuncEntry duration, level, hurt;

	public AbyssAttackEffect(LinearFuncEntry duration, LinearFuncEntry level, LinearFuncEntry hurt, int ids) {
		super(ids);
		this.duration = duration;
		this.level = level;
		this.hurt = hurt;
	}

	private MobEffectInstance weak(int rank) {
		return new MobEffectInstance(MobEffects.WEAKNESS, (int) duration.getFromRank(rank), (int) level.getFromRank(rank));
	}

	private MobEffectInstance wither(int rank) {
		return new MobEffectInstance(MobEffects.WITHER, (int) duration.getFromRank(rank), (int) level.getFromRank(rank));
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		event.getAttackTarget().addEffect(weak(rank), player);
		event.getAttackTarget().addEffect(wither(rank), player);
	}

	@Override
	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		if (source.is(L2DamageTypes.MAGIC)) {
			cache.addDealtModifier(DamageModifier.multTotal(1 - (float) hurt.getFromRank(rank)));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(hurt.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc",
				EffectDesc.getDesc(weak(rank), true),
				EffectDesc.getDesc(wither(rank), true), val));
	}

}
