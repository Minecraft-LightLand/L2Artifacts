package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ExecutorLimitEffect extends SetEffect {

	public ExecutorLimitEffect() {
		super(0);
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		if (event.getSource().getEntity() == player) {
			double factor = 0.3 - rank * 0.05;//TODO config
			event.setAmount((float) (Math.min(player.getMaxHealth(), event.getAmount()) * factor));
		}
	}
}
