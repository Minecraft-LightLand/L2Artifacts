package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class DamoclesSword extends SetEffect {

	private final LinearFuncEntry amplify;

	public DamoclesSword(LinearFuncEntry amplify) {
		super(0);
		this.amplify = amplify;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled || player.level().isClientSide())
			return;
		if (player.getHealth() > 0 && player.getHealth() < player.getMaxHealth() / 2 && player.hurtTime == 0) {
			if (ConditionalData.HOLDER.get(player).tickSinceDeath > 60)
				player.hurt(player.level().damageSources().fellOutOfWorld(), player.getMaxHealth());
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amplify = this.amplify.getFromRank(rank);
		int amount = (int) Math.round(amplify * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", amount));
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (player.getHealth() < player.getMaxHealth()) return;
		event.addHurtModifier(DamageModifier.multBase((float) amplify.getFromRank(rank)));
	}
}
