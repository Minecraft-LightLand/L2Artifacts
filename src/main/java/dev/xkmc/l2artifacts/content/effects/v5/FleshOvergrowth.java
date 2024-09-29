package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FleshOvergrowth extends SetEffect {

	private final LinearFuncEntry duration;

	public FleshOvergrowth(LinearFuncEntry duration) {
		super(0);
		this.duration = duration;
	}

	private MobEffectInstance eff(int rank) {
		return new MobEffectInstance(ArtifactEffects.FLESH_OVERGROWTH, (int) duration.getFromRank(rank), rank - 1);
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		var e = event.getTarget();
		if (player == e) return;
		if (e instanceof Mob || e instanceof Player)
			e.addEffect(eff(rank), player);
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		return List.of(Component.translatable(getDescriptionId() + ".desc", EffectDesc.getDesc(eff(rank), true)));
	}

}
