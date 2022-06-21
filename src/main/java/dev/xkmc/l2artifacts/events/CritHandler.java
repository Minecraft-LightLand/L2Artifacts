package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.init.events.AttackEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CritHandler implements AttackEventHandler.AttackListener {

	@Override
	public void onCriticalHit(AttackEventHandler.AttackCache cache) {
		CriticalHitEvent event = cache.crit;
		Player player = event.getPlayer();
		double cr = player.getAttributeValue(ArtifactTypeRegistry.CRIT_RATE.get());
		double cd = player.getAttributeValue(ArtifactTypeRegistry.CRIT_DMG.get());
		if (player.getRandom().nextDouble() < cr) {
			event.setDamageModifier((float) (event.getDamageModifier() * (1 + cd)));
			event.setResult(Event.Result.ALLOW);
		}

	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractArrow arrow) {
			if (arrow.getOwner() instanceof Player player) {
				double cr = player.getAttributeValue(ArtifactTypeRegistry.CRIT_RATE.get());
				double cd = player.getAttributeValue(ArtifactTypeRegistry.CRIT_DMG.get());
				if (player.getRandom().nextDouble() < cr) {
					arrow.setBaseDamage((float) (arrow.getBaseDamage() * (1 + cd)));
				}
			}
		}
	}


}
