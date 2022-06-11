package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.init.events.AttackEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

public class CritHandler implements AttackEventHandler.AttackListener {

	@Override
	public void onCriticalHit(AttackEventHandler.AttackCache cache) {
		CriticalHitEvent event = cache.crit;
		Player player = event.getPlayer();
		double cr = player.getAttributeValue(ArtifactRegistry.CRIT_RATE.get());
		double cd = player.getAttributeValue(ArtifactRegistry.CRIT_DMG.get());
		if (player.getRandom().nextDouble() < cr) {
			event.setDamageModifier((float) (event.getDamageModifier() * (1 + cd)));
			event.setResult(Event.Result.ALLOW);
		}

	}
}
