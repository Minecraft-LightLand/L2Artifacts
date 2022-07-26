package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ProtectionResistance extends SetEffect {

	public ProtectionResistance() {
		super(0);
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent hurt) {
		hurt.setAmount((float) (hurt.getAmount() * Math.exp(player.getHealth() / player.getMaxHealth() - 1)));
	}

}
