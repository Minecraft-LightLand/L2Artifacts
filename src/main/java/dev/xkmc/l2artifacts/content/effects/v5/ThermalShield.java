package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ThermalShield extends SetEffect {

	private final LinearFuncEntry def, duration;

	public ThermalShield(LinearFuncEntry def, LinearFuncEntry duration) {
		super();
		this.def = def;
		this.duration = duration;
	}

	@Override
	public boolean playerAttackedCancel(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Attack cache) {
		if (source.is(DamageTypeTags.IS_FREEZING)) {
			player.addEffect(new MobEffectInstance(ArtifactEffects.FROST_SHIELD, (int) duration.getFromRank(rank), rank - 1));
			return true;
		}
		return false;
	}

	@Override
	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
		var ins = player.getEffect(ArtifactEffects.FROST_SHIELD);
		if (ins == null) return;
		cache.addDealtModifier(DamageModifier.multTotal((float) (1 - def.getFromRank(rank)), getRegistryName()));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(def.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val));
	}

}
