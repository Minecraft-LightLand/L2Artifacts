package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

public class ProtectionResistance extends SetEffect {

	public ProtectionResistance() {
		super(0);
	}

	@Override
	public <T extends Event> void propagateEvent(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enable, T event) {
		if (enable) return;
		if (event instanceof LivingHurtEvent hurt) {
			hurt.setAmount((float) (hurt.getAmount() * Math.exp(player.getHealth() / player.getMaxHealth() - 1)));
		}
	}

}
