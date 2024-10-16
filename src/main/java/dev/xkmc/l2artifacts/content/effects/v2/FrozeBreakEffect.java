package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FrozeBreakEffect extends SetEffect {

	private final LinearFuncEntry factor;

	public FrozeBreakEffect(LinearFuncEntry factor) {
		super(0);
		this.factor = factor;
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getAttackTarget().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
			event.addHurtModifier(DamageModifier.multBase((float) (factor.getFromRank(rank) - 1)));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amount = (this.factor.getFromRank(rank) - 1) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(amount)));
	}

}
