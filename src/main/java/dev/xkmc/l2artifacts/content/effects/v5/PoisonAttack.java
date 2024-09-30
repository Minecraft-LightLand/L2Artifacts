package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PoisonAttack extends SetEffect {

	private final LinearFuncEntry atk;

	public PoisonAttack(LinearFuncEntry atk) {
		super();
		this.atk = atk;
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		long count = event.getTarget().getActiveEffectsMap().keySet().stream()
				.filter(e -> e.value().getCategory() == MobEffectCategory.HARMFUL).count();
		event.addHurtModifier(DamageModifier.multBase((float) (count * atk.getFromRank(rank)), getRegistryName()));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(atk.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val));
	}

}
