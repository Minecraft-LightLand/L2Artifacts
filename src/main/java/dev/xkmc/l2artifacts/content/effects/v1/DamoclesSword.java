package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.util.PlayerTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class DamoclesSword extends SetEffect {

	private final LinearFuncEntry amplify;

	public DamoclesSword(LinearFuncEntry amplify) {
		super();
		this.amplify = amplify;
	}

	@Override
	public void tick(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled || le.level().isClientSide())
			return;
		if (le.getHealth() > 0 && le.getHealth() < le.getMaxHealth() / 2 && le.hurtTime == 0) {
			if (!(le instanceof Player pl) || PlayerTracker.get(pl).getTickSinceDeath() > 60)
				le.hurt(le.level().damageSources().fellOutOfWorld(), le.getMaxHealth());
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amplify = this.amplify.getFromRank(rank);
		int amount = (int) Math.round(amplify * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", amount));
	}

	@Override
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		if (player.getHealth() < player.getMaxHealth()) return;
		event.addHurtModifier(DamageModifier.multBase((float) amplify.getFromRank(rank), getRegistryName()));
	}
}
