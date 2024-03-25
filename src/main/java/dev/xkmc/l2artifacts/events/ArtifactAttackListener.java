package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import static dev.xkmc.l2artifacts.events.ArtifactEffectEvents.postEvent;

public class ArtifactAttackListener implements AttackListener {

	@Override
	public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
		return postEvent(event.getEntity(), event, SetEffect::playerAttackModifyEvent);
	}

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		if (cache.getAttacker() instanceof Player player)
			postEvent(player, cache, SetEffect::playerHurtOpponentEvent);
	}

	@Override
	public void onDamage(AttackCache cache, ItemStack weapon) {
		if (cache.getAttackTarget() instanceof Player player)
			postEvent(player, cache, SetEffect::playerHurtEvent);
	}

	@Override
	public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
		if (cache.getAttacker() instanceof Player player)
			postEvent(player, cache, SetEffect::playerDamageOpponentEvent);
	}


}
