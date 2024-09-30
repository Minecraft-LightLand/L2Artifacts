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

public class ThermalMotive extends SetEffect {

	private final LinearFuncEntry atk, duration;

	public ThermalMotive(LinearFuncEntry atk, LinearFuncEntry duration) {
		super();
		this.atk = atk;
		this.duration = duration;
	}

	@Override
	public boolean playerAttackedCancel(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Attack cache) {
		if (source.is(DamageTypeTags.IS_FIRE)) {
			player.addEffect(new MobEffectInstance(ArtifactEffects.THERMAL_MOTIVE, (int) duration.getFromRank(rank), rank - 1));
			return true;
		}
		return false;
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		var ins = player.getEffect(ArtifactEffects.THERMAL_MOTIVE);
		if (ins == null) return;
		event.addHurtModifier(DamageModifier.multBase((float) (1 + atk.getFromRank(rank)), getRegistryName()));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(atk.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val));
	}

}
