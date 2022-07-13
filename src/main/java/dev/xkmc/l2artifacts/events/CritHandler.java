package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import dev.xkmc.l2library.init.events.attack.AttackListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CritHandler implements AttackListener {

	@Override
	public void onCriticalHit(AttackCache cache) {
		CriticalHitEvent event = cache.getCriticalHitEvent();
		assert event != null;
		Player player = event.getEntity();
		double cr = player.getAttributeValue(ArtifactTypeRegistry.CRIT_RATE.get());
		double cd = player.getAttributeValue(ArtifactTypeRegistry.CRIT_DMG.get());
		if (player.getRandom().nextDouble() < cr) {
			event.setDamageModifier((float) (event.getDamageModifier() * (1 + cd)));
			event.setResult(Event.Result.ALLOW);
		}
	}

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		if (cache.getAttacker() instanceof Player player)
			ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerHurtOpponentEvent);
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof AbstractArrow arrow) {
			if (arrow.getOwner() instanceof Player player) {
				double cr = player.getAttributeValue(ArtifactTypeRegistry.CRIT_RATE.get());
				double cd = player.getAttributeValue(ArtifactTypeRegistry.CRIT_DMG.get());
				double strength = player.getAttributeValue(ArtifactTypeRegistry.BOW_STRENGTH.get());
				if (player.getRandom().nextDouble() < cr) {
					strength *= (1 + cd);
				}
				arrow.setBaseDamage((float) (arrow.getBaseDamage() * strength));
			}
		}
	}


}
