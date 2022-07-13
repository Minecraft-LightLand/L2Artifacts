package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ExecutorLimitEffect extends SetEffect {

	private final LinearFuncEntry factor;

	public ExecutorLimitEffect(LinearFuncEntry factor) {
		super(0);
		this.factor = factor;
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		if (event.getSource().getEntity() == player) {
			event.setAmount((float) (Math.min(player.getMaxHealth(), event.getAmount()) * factor.getFromRank(rank)));
		}
	}
}
