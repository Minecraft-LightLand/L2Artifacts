package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FrozeBreakEffect extends SetEffect {

	private final LinearFuncEntry factor;

	public FrozeBreakEffect(LinearFuncEntry factor) {
		super(0);
		this.factor = factor;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getAttackTarget().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
			event.setDamageModified((float) (factor.getFromRank(rank) * event.getDamageModified()));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amount = (this.factor.getFromRank(rank) - 1) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(amount)));
	}

}
