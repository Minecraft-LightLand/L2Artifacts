package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ThermalMotive extends SetEffect {

	private final LinearFuncEntry atk, duration;

	public ThermalMotive(LinearFuncEntry atk, LinearFuncEntry duration) {
		super(0);
		this.atk = atk;
		this.duration = duration;
	}

	@Override
	public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		if (source.is(DamageTypeTags.IS_FIRE)) {
			player.addEffect(new MobEffectInstance(ArtifactEffects.THERMAL_MOTIVE.get(), (int) duration.getFromRank(rank), rank - 1));
			return true;
		}
		return false;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		var ins = player.getEffect(ArtifactEffects.THERMAL_MOTIVE.get());
		if (ins == null) return;
		event.addHurtModifier(DamageModifier.multTotal((float) (1 + atk.getFromRank(rank))));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(atk.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val));
	}

}
