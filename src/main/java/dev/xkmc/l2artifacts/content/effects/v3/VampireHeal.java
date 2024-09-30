package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class VampireHeal extends SetEffect {

	private final LinearFuncEntry light, percent;

	public VampireHeal(LinearFuncEntry light, LinearFuncEntry percent) {
		super();
		this.light = light;
		this.percent = percent;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int l = (int) Math.round(light.getFromRank(rank));
		int p = (int) Math.round(percent.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", l, p));
	}

	@Override
	public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.DefenceMax event) {
		if (player.level().isClientSide()) return;
		int light = PlayerLight.playerUnderSun(player);
		if (light <= this.light.getFromRank(rank)) {
			player.heal((float) (event.getDamageFinal() * percent.getFromRank(rank)));
		}
	}

}
