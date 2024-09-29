package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import static dev.xkmc.l2artifacts.events.ArtifactEffectEvents.postEvent;

public class ArtifactAttackListener implements AttackListener {

	@Override
	public boolean onCriticalHit(PlayerAttackCache cache, CriticalHitEvent event) {
		return postEvent(event.getEntity(), event, SetEffect::playerAttackModifyEvent);
	}

	@Override
	public boolean onAttack(DamageData.Attack cache) {
		return postEvent(cache.getTarget(), cache, SetEffect::playerAttackedEvent);
	}

	@Override
	public void onHurt(DamageData.Offence cache) {
		if (cache.getAttacker() != null)
			postEvent(cache.getAttacker(), cache, SetEffect::playerHurtOpponentEvent);
	}

	@Override
	public void onDamage(DamageData.Defence cache) {
		postEvent(cache.getTarget(), cache, SetEffect::playerHurtEvent);
	}

	@Override
	public void onDamageFinalized(DamageData.DefenceMax cache) {
		if (cache.getAttacker() != null)
			postEvent(cache.getAttacker(), cache, SetEffect::playerDamageOpponentEvent);
	}

}
