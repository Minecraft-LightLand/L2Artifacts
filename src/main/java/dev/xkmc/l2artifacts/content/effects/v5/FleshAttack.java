package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class FleshAttack extends SetEffect {

	private final LinearFuncEntry thr, atk;

	public FleshAttack(LinearFuncEntry thr, LinearFuncEntry atk) {
		super(0);
		this.thr = thr;
		this.atk = atk;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getAttackTarget().getHealth() <= event.getAttackTarget().getMaxHealth() * thr.getFromRank(rank))
			event.addHurtModifier(DamageModifier.multBase((float) atk.getFromRank(rank)));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int php = (int) Math.round(thr.getFromRank(rank) * 100);
		int val = (int) Math.round(atk.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", php, val));
	}

}
