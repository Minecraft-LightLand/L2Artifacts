package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import dev.xkmc.l2library.init.events.attack.AttackListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CritHandler implements AttackListener {

	@Override
	public void onHurt(AttackCache cache, ItemStack weapon) {
		if (cache.getAttacker() instanceof Player player)
			ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerHurtOpponentEvent);
	}

	@Override
	public void onDamageFinalized(AttackCache cache, ItemStack weapon) {
		if (cache.getAttacker() instanceof Player player)
			ArtifactEffectEvents.postEvent(player, cache, SetEffect::playerDamageOpponentEvent);
	}


}
